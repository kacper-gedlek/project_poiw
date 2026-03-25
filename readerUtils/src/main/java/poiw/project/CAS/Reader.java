package poiw.project.CAS;

import javax.smartcardio.*;
import java.util.List;
import java.util.Collections;

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
		return new elsData("","","");
	}
}
