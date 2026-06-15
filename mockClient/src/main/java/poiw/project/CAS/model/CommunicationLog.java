package poiw.project.CAS.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Przechwytuje pełne informacje o wysyłanych i otrzymywanych odpowiedziach.
 * Zaprojektowana do debugowania - widzimy dokładnie co zostało wysłane i
 * odebrane.
 */
public class CommunicationLog {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    // Szczegóły żądania
    private String requestMethod;
    private String requestUrl;
    private Map<String, String> requestHeaders;
    private String requestBody;

    // Szczegóły odpowiedzi
    private int responseStatusCode;
    private Map<String, String> responseHeaders;
    private String responseBody;

    // Metadane
    private LocalDateTime timestamp;
    private long responseTimeMs;
    private boolean success;
    private String errorMessage;

    public CommunicationLog() {
        this.timestamp = LocalDateTime.now();
        this.success = false;
    }

    // --- Request setters ---

    public void setRequest(String method, String url, Map<String, String> headers, String body) {
        this.requestMethod = method;
        this.requestUrl = url;
        this.requestHeaders = headers;
        this.requestBody = body;
    }

    // --- Response setters ---

    public void setResponse(int statusCode, Map<String, String> headers, String body) {
        this.responseStatusCode = statusCode;
        this.responseHeaders = headers;
        this.responseBody = body;
        this.success = true;
    }

    public void setError(String errorMessage) {
        this.errorMessage = errorMessage;
        this.success = false;
    }

    public void setResponseTimeMs(long responseTimeMs) {
        this.responseTimeMs = responseTimeMs;
    }

    // Getters

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public Map<String, String> getRequestHeaders() {
        return requestHeaders;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public int getResponseStatusCode() {
        return responseStatusCode;
    }

    public Map<String, String> getResponseHeaders() {
        return responseHeaders;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public long getResponseTimeMs() {
        return responseTimeMs;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Zwraca sformatowane, czytelne podsumowanie wymiany.
     * Jest to główny komunikat debugowania.
     */
    public String toDebugString() {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================================\n");
        sb.append("  LOG KOMUNIKACJI - ").append(timestamp.format(FORMATTER)).append("\n");
        sb.append("========================================================\n\n");

        // Sekcja wysyłania żądania
        sb.append("---- REQUEST -----------------------------------------------\n");
        sb.append("  Method:  ").append(requestMethod).append("\n");
        sb.append("  URL:     ").append(requestUrl).append("\n");
        if (requestHeaders != null && !requestHeaders.isEmpty()) {
            sb.append("  Headers:\n");
            requestHeaders.forEach((k, v) -> sb.append("    ").append(k).append(": ").append(v).append("\n"));
        }
        if (requestBody != null) {
            sb.append("  Body:\n    ").append(requestBody).append("\n");
        }

        sb.append("\n");

        // Sekcja odpowiedzi
        if (success) {
            sb.append("---- RESPONSE ----------------------------------------------\n");
            sb.append("  Status:  ").append(responseStatusCode).append("\n");
            sb.append("  Time:    ").append(responseTimeMs).append(" ms\n");
            if (responseHeaders != null && !responseHeaders.isEmpty()) {
                sb.append("  Headers:\n");
                responseHeaders.forEach((k, v) -> sb.append("    ").append(k).append(": ").append(v).append("\n"));
            }
            if (responseBody != null) {
                sb.append("  Body:\n    ").append(responseBody).append("\n");
            }
        } else {
            sb.append("---- ERROR -------------------------------------------------\n");
            sb.append("  ").append(errorMessage != null ? errorMessage : "Unknown error").append("\n");
        }

        sb.append("\n========================================================\n");
        return sb.toString();
    }
}
