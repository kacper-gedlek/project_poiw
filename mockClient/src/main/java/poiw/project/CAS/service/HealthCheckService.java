package poiw.project.CAS.service;

import poiw.project.CAS.config.ServerConfig;
import poiw.project.CAS.http.HttpService;
import poiw.project.CAS.model.CommunicationLog;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Serwis do sprawdzania stanu serwera CAS i dostępności endpointów.
 */
public class HealthCheckService {
    private final HttpService httpService;
    private final ServerConfig config;

    public HealthCheckService(ServerConfig config) {
        this.config = config;
        this.httpService = new HttpService(config);
    }

    /**
     * Wysyła ping do serwera.
     */
    public CommunicationLog pingServer() {
        return httpService.sendGet(config.getServerUrl());
    }

    /**
     * Sprawdza wszystkie znane endpointy i zwraca ich status.
     */
    public Map<String, CommunicationLog> checkAllEndpoints() {
        Map<String, CommunicationLog> results = new LinkedHashMap<>();
        String[] endpoints = { "/", "/cards", "/readers" };

        for (String endpoint : endpoints) {
            results.put(endpoint, httpService.sendGet(config.getServerUrl() + endpoint));
        }

        return results;
    }

    /**
     * Zwraca podsumowanie stanu serwera.
     */
    public String getHealthSummary() {
        CommunicationLog ping = pingServer();
        StringBuilder sb = new StringBuilder();
        sb.append("Server: ").append(config.getServerUrl()).append("\n");
        sb.append("Status: ").append(ping.isSuccess() ? "ONLINE" : "OFFLINE").append("\n");
        if (ping.isSuccess()) {
            sb.append("Response Time: ").append(ping.getResponseTimeMs()).append(" ms\n");
            sb.append("HTTP Status: ").append(ping.getResponseStatusCode()).append("\n");
        } else {
            sb.append("Error: ").append(ping.getErrorMessage()).append("\n");
        }
        return sb.toString();
    }
}
