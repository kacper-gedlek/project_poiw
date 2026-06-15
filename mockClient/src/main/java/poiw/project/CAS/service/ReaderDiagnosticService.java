package poiw.project.CAS.service;

import poiw.project.CAS.Reader;
import poiw.project.CAS.ReaderInterface;
import poiw.project.CAS.elsData;

import javax.smartcardio.CardTerminal;
import java.util.List;

/**
 * Diagnostyczny interfejs
 */
public class ReaderDiagnosticService {
    private final ReaderInterface physicalReader;

    public ReaderDiagnosticService() {
        this.physicalReader = new Reader();
    }

    public void initialize() {
        physicalReader.initialize();
    }

    public List<CardTerminal> getAvailableReaders() {
        return physicalReader.getReaders();
    }

    public void selectReader(int index) {
        physicalReader.setReader(index);
    }

    public String readCardUID() {
        return physicalReader.getUID();
    }

    public elsData readStudentIdData() {
        return physicalReader.getElsData();
    }

    public String getDiagnosticReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Physical Reader Diagnostics ---\n");
        List<CardTerminal> readers = getAvailableReaders();
        if (readers == null || readers.isEmpty()) {
            sb.append("Status: NO READERS DETECTED\n");
        } else {
            sb.append("Status: DETECTED ").append(readers.size()).append(" READER(S)\n");
            for (int i = 0; i < readers.size(); i++) {
                try {
                    CardTerminal t = readers.get(i);
                    sb.append(" [").append(i).append("] ").append(t.getName())
                            .append(" (Card present: ").append(t.isCardPresent()).append(")\n");
                } catch (Exception e) {
                    sb.append(" [").append(i).append("] Error reading terminal status\n");
                }
            }
        }
        return sb.toString();
    }
}
