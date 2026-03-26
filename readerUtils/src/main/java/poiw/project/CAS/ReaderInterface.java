package poiw.project.CAS;

import javax.smartcardio.CardTerminal;
import java.util.List;

public interface ReaderInterface {
	/**
	 * Initializes connection with a smart card reader
	 */
	void initialize();

	/**
	 * Returns a list of available card terminals
	 * @return list of avaliable terminals, when no readers are detected an empty list
	 * is returned
	 */
	List<CardTerminal> getReaders();

	/**
	 * Selects a specific reader by its index in the list obtained by calling getReaders()
	 * @param index of terminal returned by getReaders()
	 */
	void setReader(int index);

	/**
	 * Retrieves the UID of MiFare Card
	 * @return string containing UID in hex
	 */
	String getUID();

	/**
	 * Reads the Electronic Student ID card
	 * @return ElsData object containing student details.
	 */
	elsData getElsData();
}
