package poiw.project.CAS;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import poiw.project.CAS.config.ServerConfig;
import poiw.project.CAS.model.CommunicationLog;
import poiw.project.CAS.model.ValidationResult;
import poiw.project.CAS.simulator.RfidReaderSimulator;

import static org.junit.jupiter.api.Assertions.*;

class MockClientTest {

    private MockClient client;

    @BeforeEach
    void setUp() {
        client = new MockClient();
    }

    // ServerConfig test
    @Test
    @DisplayName("Default server config should use localhost:8080")
    void testDefaultServerConfig() {
        ServerConfig config = client.getServerConfig();
        assertEquals("http://localhost:8080", config.getServerUrl());
        assertEquals(5000, config.getTimeoutMs());
        assertEquals("http://localhost:8080/validation", config.getValidationUrl());
    }

    @Test
    @DisplayName("Custom server URL should be applied")
    void testCustomServerUrl() {
        MockClient customClient = new MockClient("http://192.168.1.100:9090");
        assertEquals("http://192.168.1.100:9090", customClient.getServerConfig().getServerUrl());
    }

    // RfidReaderSimulator test

    @Test
    @DisplayName("Simulator should not be initialized by default")
    void testSimulatorNotInitializedByDefault() {
        RfidReaderSimulator sim = client.getRfidSimulator();
        assertFalse(sim.isInitialized());
        assertNull(sim.getReaderNumber());
    }

    @Test
    @DisplayName("Simulator should initialize with reader number and name")
    void testSimulatorInitialize() {
        RfidReaderSimulator sim = client.getRfidSimulator();
        sim.initialize("1111222233334444", "Test Reader");
        assertTrue(sim.isInitialized());
        assertEquals("1111222233334444", sim.getReaderNumber());
        assertEquals("Test Reader", sim.getReaderName());
    }

    @Test
    @DisplayName("Simulator should scan a card and store it")
    void testSimulatorScanCard() {
        RfidReaderSimulator sim = client.getRfidSimulator();
        sim.initialize("1111222233334444");
        String card = sim.scanCard("AABB112233445566");
        assertEquals("AABB112233445566", card);
        assertEquals("AABB112233445566", sim.getLastScannedCard());
    }

    @Test
    @DisplayName("Simulator should generate random 16-char card numbers")
    void testSimulatorRandomCard() {
        RfidReaderSimulator sim = client.getRfidSimulator();
        sim.initialize("1111222233334444");
        String card = sim.scanRandomCard();
        assertNotNull(card);
        assertEquals(16, card.length());
        assertTrue(card.matches("[0-9A-F]{16}"), "Card should be 16 hex characters");
    }

    @Test
    @DisplayName("Simulator reset should clear all state")
    void testSimulatorReset() {
        RfidReaderSimulator sim = client.getRfidSimulator();
        sim.initialize("1111222233334444", "Test");
        sim.scanCard("AABBCCDD11223344");
        sim.reset();
        assertFalse(sim.isInitialized());
        assertNull(sim.getReaderNumber());
        assertNull(sim.getLastScannedCard());
    }

    // CommunicationLog test

    @Test
    @DisplayName("CommunicationLog should capture error state")
    void testCommunicationLogError() {
        CommunicationLog log = new CommunicationLog();
        log.setError("Connection refused");
        assertFalse(log.isSuccess());
        assertEquals("Connection refused", log.getErrorMessage());
        assertNotNull(log.getTimestamp());
    }

    @Test
    @DisplayName("CommunicationLog toDebugString should contain request and error info")
    void testCommunicationLogDebugString() {
        CommunicationLog log = new CommunicationLog();
        log.setRequest("GET", "http://localhost:8080/validation",
                java.util.Map.of("Content-Type", "application/json"),
                "{\"readerNumber\":\"1111\",\"cardNumber\":\"2222\"}");
        log.setError("Connection refused");

        String debug = log.toDebugString();
        assertTrue(debug.contains("GET"));
        assertTrue(debug.contains("http://localhost:8080/validation"));
        assertTrue(debug.contains("Connection refused"));
    }

    // ValidationResult test

    @Test
    @DisplayName("ValidationResult should correctly identify access status")
    void testValidationResultStatus() {
        ValidationResult granted = new ValidationResult("John", "Lobby", "ACCESS_GRANTED");
        assertTrue(granted.isAccessGranted());
        assertFalse(granted.isAccessDenied());
        assertFalse(granted.isError());

        ValidationResult denied = new ValidationResult("Jane", "Server Room", "ACCESS_DENIED");
        assertFalse(denied.isAccessGranted());
        assertTrue(denied.isAccessDenied());

        ValidationResult error = new ValidationResult(null, null, "ERROR");
        assertTrue(error.isError());
    }

    // Integration: simulateScanAndValidate without server

    @Test
    @DisplayName("simulateScanAndValidate should fail gracefully without initialized reader")
    void testScanWithoutInit() {
        CommunicationLog log = client.simulateScanAndValidate("5555666677778888");
        assertFalse(log.isSuccess());
        assertTrue(log.getErrorMessage().contains("not initialized"));
    }

    @Test
    @DisplayName("simulateScanAndValidate should produce a properly formed request log")
    void testScanProducesValidLog() {
        client.getRfidSimulator().initialize("1111222233334444", "Test Reader");
        CommunicationLog log = client.simulateScanAndValidate("5555666677778888");
        assertNotNull(log);
        assertNotNull(log.getRequestBody());
        assertTrue(log.getRequestBody().contains("1111222233334444"));
        assertTrue(log.getRequestBody().contains("5555666677778888"));
        assertEquals("GET", log.getRequestMethod());
        assertTrue(log.getRequestUrl().contains("/validation"));
    }

    // Historia

    @Test
    @DisplayName("ValidationService should track request history")
    void testValidationHistory() {
        assertEquals(0, client.getValidationService().getRequestCount());
        client.getRfidSimulator().initialize("1111222233334444");
        client.simulateScanAndValidate("AAAA111122223333");
        client.simulateScanAndValidate("BBBB444455556666");
        assertEquals(2, client.getValidationService().getRequestCount());
        assertNotNull(client.getValidationService().getLastLog());
        client.getValidationService().clearHistory();
        assertEquals(0, client.getValidationService().getRequestCount());
    }
}
