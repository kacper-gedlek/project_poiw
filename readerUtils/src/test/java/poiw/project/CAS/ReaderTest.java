package poiw.project.CAS;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class ReaderTest {

    private Reader reader;

    @BeforeEach
    void setUp() {
        reader = new Reader();
        reader.initialize();
    }

    @Test
    @DisplayName("Should initialize and return reader list")
    void testInitializeAndGetReaders() {
        var readers = reader.getReaders();
        assertNotNull(readers, "Reader list should not be null");
    }

    @Test
    @DisplayName("Should return empty UID when no reader is selected")
    void testGetUIDWithoutSelection() {
        String uid = reader.getUID();
        assertEquals("", uid, "UID should be empty string when no reader is selected");
    }
}

