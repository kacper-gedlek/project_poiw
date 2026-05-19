package poiw.project.CAS;

import poiw.project.CAS.config.ServerConfig;
import poiw.project.CAS.model.CommunicationLog;
import poiw.project.CAS.model.ValidationResult;
import poiw.project.CAS.service.HealthCheckService;
import poiw.project.CAS.service.ValidationService;
import poiw.project.CAS.simulator.RfidReaderSimulator;

/**
 * MockClient - narzędzie do testowania Card Authentication System (CAS).
 * Zapewnia symulację czytnika RFID i pełną przejrzystość komunikacji HTTP.
 *
 * Ta klasa inicjalizuje wszystkie usługi i udostępnia je dla warstwy frontend.
 */
public class MockClient {
    private final ServerConfig serverConfig;
    private final RfidReaderSimulator rfidSimulator;
    private final ValidationService validationService;
    private final HealthCheckService healthCheckService;

    public MockClient() {
        this.serverConfig = new ServerConfig();
        this.rfidSimulator = new RfidReaderSimulator();
        this.validationService = new ValidationService(serverConfig);
        this.healthCheckService = new HealthCheckService(serverConfig);
    }

    public MockClient(String serverUrl) {
        this.serverConfig = new ServerConfig(serverUrl, 5000);
        this.rfidSimulator = new RfidReaderSimulator();
        this.validationService = new ValidationService(serverConfig);
        this.healthCheckService = new HealthCheckService(serverConfig);
    }

    // Getters for services

    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    public RfidReaderSimulator getRfidSimulator() {
        return rfidSimulator;
    }

    public ValidationService getValidationService() {
        return validationService;
    }

    public HealthCheckService getHealthCheckService() {
        return healthCheckService;
    }

    /**
     * Symuluje pełny przepływ skanowania RFID → walidacji. Symulacja skanowania
     * karty i wysłanie żądania walidacji do serwera.
     */
    public CommunicationLog simulateScanAndValidate(String cardNumber) {
        if (!rfidSimulator.isInitialized()) {
            CommunicationLog errorLog = new CommunicationLog();
            errorLog.setError("RFID Reader not initialized. Call getRfidSimulator().initialize(readerNumber) first.");
            return errorLog;
        }

        rfidSimulator.scanCard(cardNumber);
        return validationService.validate(rfidSimulator.getReaderNumber(), cardNumber);
    }

    /**
     * Symuluje skanowanie losowej karty i walidację.
     */
    public CommunicationLog simulateRandomScanAndValidate() {
        if (!rfidSimulator.isInitialized()) {
            CommunicationLog errorLog = new CommunicationLog();
            errorLog.setError("RFID Reader not initialized. Call getRfidSimulator().initialize(readerNumber) first.");
            return errorLog;
        }

        String randomCard = rfidSimulator.scanRandomCard();
        return validationService.validate(rfidSimulator.getReaderNumber(), randomCard);
    }

    // Demo

    public static void main(String[] args) {
        System.out.println("+==================================================+");
        System.out.println("|     CAS MockClient - Debug CLI                   |");
        System.out.println("+==================================================+");

        MockClient client = new MockClient();
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Server Health Check");
            System.out.println("2. Initialize RFID Reader (manual ID)");
            System.out.println("3. Initialize RFID Reader (random ID)");
            System.out.println("4. Scan specific Card");
            System.out.println("5. Scan random Card");
            System.out.println("6. Show requests count");
            System.out.println("0. Exit");
            System.out.print("Select option: ");

            String choice = scanner.nextLine();
            System.out.println();

            switch (choice) {
                case "1":
                    System.out.println("[>] Health Check...");
                    System.out.println(client.getHealthCheckService().getHealthSummary());
                    break;
                case "2":
                    System.out.print("Enter 16-character Reader Number: ");
                    String manualReaderId = scanner.nextLine();
                    client.getRfidSimulator().initialize(manualReaderId, "Manual Reader");
                    System.out.println("[>] Reader initialized to: " + manualReaderId);
                    break;
                case "3":
                    String randomReaderId = client.getRfidSimulator().generateRandomReaderNumber();
                    client.getRfidSimulator().initialize(randomReaderId, "Random Reader");
                    System.out.println("[>] Reader initialized to: " + randomReaderId);
                    break;
                case "4":
                    System.out.print("Enter 16-character Card Number: ");
                    String manualCardId = scanner.nextLine();
                    CommunicationLog log = client.simulateScanAndValidate(manualCardId);
                    System.out.println(log.toDebugString());
                    ValidationResult res = client.getValidationService().parseResult(log);
                    if (res != null)
                        System.out.println("[>] Validation: " + res.getValidation());
                    break;
                case "5":
                    CommunicationLog randomLog = client.simulateRandomScanAndValidate();
                    System.out.println(randomLog.toDebugString());
                    ValidationResult ranRes = client.getValidationService().parseResult(randomLog);
                    if (ranRes != null)
                        System.out.println("[>] Validation: " + ranRes.getValidation());
                    break;
                case "6":
                    System.out.println("[>] Total requests made: " + client.getValidationService().getRequestCount());
                    break;
                case "0":
                    running = false;
                    System.out.println("Exiting MockClient...");
                    break;
                default:
                    System.out.println("[!] Invalid option. Please try again.");
            }
        }
        scanner.close();
    }
}
