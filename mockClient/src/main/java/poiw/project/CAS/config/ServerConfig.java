package poiw.project.CAS.config;

/**
 * Konfiguracja połączenia z serwerem CAS.
 */
public class ServerConfig {
    private String serverUrl;
    private int timeoutMs;

    public ServerConfig() {
        this.serverUrl = "http://localhost:8080";
        this.timeoutMs = 5000;
    }

    public ServerConfig(String serverUrl, int timeoutMs) {
        this.serverUrl = serverUrl;
        this.timeoutMs = timeoutMs;
    }

    public String getServerUrl() {
        return serverUrl;
    }


    public int getTimeoutMs() {
        return timeoutMs;
    }


    public String getValidationUrl() {
        return serverUrl + "/validation";
    }

    @Override
    public String toString() {
        return "ServerConfig{url='" + serverUrl + "', timeoutMs=" + timeoutMs + "}";
    }
}
