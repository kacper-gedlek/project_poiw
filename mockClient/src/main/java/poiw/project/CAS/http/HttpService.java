package poiw.project.CAS.http;

import poiw.project.CAS.config.ServerConfig;
import poiw.project.CAS.model.CommunicationLog;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * HTTP client
 * Przechwytuje pełne informacje o wysyłanej oraz otrzymanej odpowiedzi.
 * Każdy HTTP call zwraca CommunicationLog z kompletnymi informacjami o wysłanej
 * oraz otrzymanej odpowiedzi.
 */
public class HttpService {
    private final ServerConfig config;
    private final HttpClient httpClient;

    public HttpService(ServerConfig config) {
        this.config = config;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(config.getTimeoutMs()))
                .build();
    }

    /**
     * Wysyła zapytanie GET z ciałem JSON.
     * Przechwytuje pełne informacje o wysłanej oraz otrzymanej odpowiedzi.
     */
    public CommunicationLog sendGetWithBody(String url, String jsonBody) {
        CommunicationLog log = new CommunicationLog();

        Map<String, String> requestHeaders = new LinkedHashMap<>();
        requestHeaders.put("Content-Type", "application/json");
        requestHeaders.put("Accept", "application/json");

        log.setRequest("GET", url, requestHeaders, jsonBody);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .method("GET", HttpRequest.BodyPublishers.ofString(jsonBody))
                    .timeout(Duration.ofMillis(config.getTimeoutMs()))
                    .build();

            long startTime = System.currentTimeMillis();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            long endTime = System.currentTimeMillis();

            Map<String, String> responseHeaders = new LinkedHashMap<>();
            response.headers().map().forEach((key, values) -> responseHeaders.put(key, String.join(", ", values)));

            log.setResponse(response.statusCode(), responseHeaders, response.body());
            log.setResponseTimeMs(endTime - startTime);

        } catch (Exception e) {
            log.setError(e.getClass().getSimpleName() + ": " + e.getMessage());
        }

        return log;
    }

    /**
     * Sprawdza dostępność serwera poprzez wysłanie żądania GET
     */
    public CommunicationLog sendGet(String url) {
        CommunicationLog log = new CommunicationLog();

        Map<String, String> requestHeaders = new LinkedHashMap<>();
        requestHeaders.put("Accept", "text/html, application/json");

        log.setRequest("GET", url, requestHeaders, null);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "text/html, application/json")
                    .GET()
                    .timeout(Duration.ofMillis(config.getTimeoutMs()))
                    .build();

            long startTime = System.currentTimeMillis();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            long endTime = System.currentTimeMillis();

            Map<String, String> responseHeaders = new LinkedHashMap<>();
            response.headers().map().forEach((key, values) -> responseHeaders.put(key, String.join(", ", values)));

            log.setResponse(response.statusCode(), responseHeaders, response.body());
            log.setResponseTimeMs(endTime - startTime);

        } catch (Exception e) {
            log.setError(e.getClass().getSimpleName() + ": " + e.getMessage());
        }

        return log;
    }

    public ServerConfig getConfig() {
        return config;
    }
}
