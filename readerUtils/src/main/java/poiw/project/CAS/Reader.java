package poiw.project.CAS;

import javax.smartcardio.*;
import java.util.List;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import org.bouncycastle.asn1.*;
import org.bouncycastle.cms.CMSSignedData;

public class Reader implements ReaderInterface {
	private TerminalFactory factory;
	private List<CardTerminal> terminals;
	private CardTerminal terminal;
	
	@Override
	public void initialize() {
		factory = TerminalFactory.getDefault();
	}
	
	@Override
	public List<CardTerminal> getReaders() {
		try {
			terminals = factory.terminals().list();
			return terminals;
		}
		catch(CardException e) {
			System.err.println(e.getMessage());
			return Collections.emptyList();
		}
		
	}
	
	@Override
	public void setReader(int reader) {
		if(terminals != null && reader >= 0 && reader < terminals.size()) {
			terminal = terminals.get(reader);
		}
		else {
			System.err.println("Error when selecting reader");
		}
	}
	
	@Override
	public String getUID() {
		if(terminal == null) {
			System.err.println("No terminal selected");
			return "";
		}
		
		byte[] GET_UID = {(byte)0xff, (byte)0xca, (byte)0x00, (byte)0x00, (byte)0x00};
		
		try {
			Card card = terminal.connect("T=1");
			CardChannel channel = card.getBasicChannel();
			ResponseAPDU r = channel.transmit(new CommandAPDU(GET_UID));
			if(r.getSW() == 0x9000) {
				return UID.b2hStr(r.getData());
			}
			else {
				System.err.println("Failed to get UID");
				return "";
			}
		}
		catch(CardException e) {
			System.err.println(e.getMessage());
			return "";
		}        
	}
	
	@Override
	public elsData getElsData() {
		byte[] SELECT_DF = {(byte) 0x00, (byte) 0xA4, (byte) 0x04, (byte) 0x04};
		byte[] SELECT_ANY = {(byte) 0x00, (byte) 0xA4, (byte) 0x00, (byte) 0x04};
		byte[] DF_SELS = {(byte) 0xD6, (byte) 0x16, (byte) 0x00, (byte) 0x00, (byte) 0x30, (byte) 0x01, (byte) 0x01};
		byte[] EF_ELS = {(byte) 0x00, (byte) 0x02};
		byte[] EF_CERT = {(byte) 0x00, (byte) 0x01};
		
		if(terminal == null) {
			System.err.println("No terminal selected");
			return null;
		}


		Card card = null;
        try {
            card = terminal.connect("*");
            CardChannel channel = card.getBasicChannel();

            transmitCommand(channel, SELECT_DF, DF_SELS);
            
            ResponseAPDU selectElsResp = transmitCommand(channel, SELECT_ANY, EF_ELS);
            int elsFileSize = parseFileSizeFromFCI(selectElsResp.getData());
            System.out.println("EF_ELS File Size: " + elsFileSize + " bytes");
            
            byte[] rawelsData = readBinaryBounded(channel, elsFileSize);

            ResponseAPDU selectCertResp = transmitCommand(channel, SELECT_ANY, EF_CERT);
            int certFileSize = parseFileSizeFromFCI(selectCertResp.getData());
            System.out.println("EF_CERT File Size: " + certFileSize + " bytes");
            
            readBinaryBounded(channel, certFileSize);

            return parseelsData(rawelsData);

        } catch (Exception e) {
            System.err.println("Error reading ELS Card: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            if (card != null) {
                try {
                    card.disconnect(false);
                } catch (CardException e) {
                    System.err.println("Failed to disconnect card: " + e.getMessage());
                }
            }
        }
	}

	
    private static int parseFileSizeFromFCI(byte[] fciData) {
        if (fciData == null || fciData.length < 2) {
            return 4096; 
        }
        try (ASN1InputStream asn1 = new ASN1InputStream(new ByteArrayInputStream(fciData))) {
            ASN1Primitive obj = asn1.readObject();
            if (obj instanceof ASN1Sequence) { 
                ASN1Sequence seq = (ASN1Sequence) obj;
                for (int i = 0; i < seq.size(); i++) {
                    ASN1Encodable item = seq.getObjectAt(i);
                    if (item instanceof ASN1TaggedObject) {
                        ASN1TaggedObject tagged = (ASN1TaggedObject) item;
                        if (tagged.getTagNo() == 0 || tagged.getTagNo() == 1) {
                            byte[] ext = ASN1OctetString.getInstance(tagged.getExplicitBaseObject()).getOctets();
                            int size = 0;
                            for (byte b : ext) {
                                size = (size << 8) | (b & 0xFF);
                            }
                            return size;
                        }
                    }
                }
            }
        } catch (Exception e) {
            for (int i = 0; i < fciData.length - 3; i++) {
                if ((fciData[i] == (byte)0x80 || fciData[i] == (byte)0x81) && fciData[i+1] == 2) {
                    return ((fciData[i+2] & 0xFF) << 8) | (fciData[i+3] & 0xFF);
                }
            }
        }
        return 2048; 
    }

    /**
     * Reads provided number of bytes from the card
     */
    private static byte[] readBinaryBounded(CardChannel channel, int totalBytesToRead) throws CardException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int offset = 0;

        while (offset < totalBytesToRead) {
            int bytesLeft = totalBytesToRead - offset;
            int blockSize = Math.min(bytesLeft, 0x80); 

            byte p1 = (byte) ((offset >> 8) & 0x7F);
            byte p2 = (byte) (offset & 0xFF);
            
            CommandAPDU cmd = new CommandAPDU(0x00, 0xB0, p1 & 0xFF, p2 & 0xFF, blockSize);
            ResponseAPDU response = channel.transmit(cmd);
            
            if (response.getSW() != 0x9000) {
                break; 
            }
            
            byte[] data = response.getData();
            if (data != null && data.length > 0) {
                int bytesToCopy = Math.min(data.length, blockSize);
                outputStream.write(data, 0, bytesToCopy);
                offset += bytesToCopy;
            } else {
                break; 
            }
        }
        return outputStream.toByteArray();
    }

    private static ResponseAPDU transmitCommand(CardChannel channel, byte[] instruction, byte[] data) throws CardException {
        byte[] apdu = new byte[instruction.length + 1 + data.length];
        System.arraycopy(instruction, 0, apdu, 0, instruction.length);
        apdu[instruction.length] = (byte) data.length;
        System.arraycopy(data, 0, apdu, instruction.length + 1, data.length);

        CommandAPDU command = new CommandAPDU(apdu);
        ResponseAPDU response = channel.transmit(command);
        //System.out.printf("Tx: %s -> SW: %04X\n", bytesToHex(data), response.getSW());
        return response;
    }

	/**
     * Parses ASN.1 data from ELS
     */
    private static elsData parseelsData(byte[] rawData) throws Exception {
        CMSSignedData signedData = new CMSSignedData(rawData);
        byte[] selsInfoBytes = (byte[]) signedData.getSignedContent().getContent();
        
        try (ASN1InputStream innerAsn1 = new ASN1InputStream(new ByteArrayInputStream(selsInfoBytes))) {
            ASN1Sequence selsInfoSeq = ASN1Sequence.getInstance(innerAsn1.readObject());
            
            ASN1Sequence surnameSeq = ASN1Sequence.getInstance(selsInfoSeq.getObjectAt(3));
            String surname = ((ASN1String) surnameSeq.getObjectAt(0)).getString();

            ASN1Sequence nameSeq = ASN1Sequence.getInstance(selsInfoSeq.getObjectAt(4));
            String name = ((ASN1String) nameSeq.getObjectAt(0)).getString();

            String albumNumber = ((ASN1String) selsInfoSeq.getObjectAt(5)).getString();

            return new elsData(name, surname, albumNumber);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
