package poiw.project.CAS.simulator;

import java.util.Random;

/**
 * Symuluje czytnik kart RFID bez fizycznego sprzętu.
 * Pozwala na ustawienie tożsamości czytnika i "skanowanie" kart przez podanie
 * lub wygenerowanie numerów kart.
 */
public class RfidReaderSimulator {
    private String readerNumber;
    private String readerName;
    private String lastScannedCard;
    private boolean initialized;
    private final Random random;

    public RfidReaderSimulator() {
        this.random = new Random();
        this.initialized = false;
    }

    /**
     * Inicjalizuje symulowany czytnik z konkretnym numerem czytnika i nazwą.
     */
    public void initialize(String readerNumber, String readerName) {
        this.readerNumber = readerNumber;
        this.readerName = readerName;
        this.initialized = true;
    }

    public void initialize(String readerNumber) {
        initialize(readerNumber, "Simulated RFID Reader");
    }

    /**
     * Symuluje skanowanie karty RFID. Numer karty jest podawany przez użytkownika.
     */
    public String scanCard(String cardNumber) {
        this.lastScannedCard = cardNumber;
        return cardNumber;
    }

    public String scanRandomCard() {
        String cardNumber = generateRandomNumber();
        this.lastScannedCard = cardNumber;
        return cardNumber;
    }

    /**
     * Generuje losowy 16-znakowy ciąg szesnastkowy (symulujący UID karty).
     * 
     * @return 16-znakowy ciąg szesnastkowy
     */
    public String generateRandomNumber() {
        StringBuilder sb = new StringBuilder(16);
        for (int i = 0; i < 16; i++) {
            sb.append(Integer.toHexString(random.nextInt(16)).toUpperCase());
        }
        return sb.toString();
    }

    public String generateRandomReaderNumber() {
        return generateRandomNumber();
    }

    // Getters

    public String getReaderNumber() {
        return readerNumber;
    }

    public String getReaderName() {
        return readerName;
    }

    public String getLastScannedCard() {
        return lastScannedCard;
    }

    public boolean isInitialized() {
        return initialized;
    }

    // Setters

    public void setReaderNumber(String readerNumber) {
        this.readerNumber = readerNumber;
    }

    public void setReaderName(String readerName) {
        this.readerName = readerName;
    }

    /**
     * Resetuje symulator do stanu nieinicjalizowanego.
     */
    public void reset() {
        this.readerNumber = null;
        this.readerName = null;
        this.lastScannedCard = null;
        this.initialized = false;
    }

    @Override
    public String toString() {
        return "RfidReaderSimulator{readerNumber='" + readerNumber
                + "', readerName='" + readerName
                + "', initialized=" + initialized
                + ", lastScannedCard='" + lastScannedCard + "'}";
    }
}
