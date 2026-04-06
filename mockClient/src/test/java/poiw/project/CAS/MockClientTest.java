package poiw.project.CAS;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MockClientTest {
    @Test
    public void testGetMessage() {
        MockClient client = new MockClient();
        assertEquals("Hello, World!", client.getMessage());
    }
}
