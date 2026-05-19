package poiw.project.CAS.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import poiw.project.CAS.config.ServerConfig;
import poiw.project.CAS.http.HttpService;
import poiw.project.CAS.model.CommunicationLog;
import poiw.project.CAS.model.ValidationResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Serwis do wysyłania żądań walidacji do serwera CAS.
 */
public class ValidationService {
    private final HttpService httpService;
    private final ServerConfig config;
    private final Gson gson;
    private final List<CommunicationLog> history;

    public ValidationService(ServerConfig config) {
        this.config = config;
        this.httpService = new HttpService(config);
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.history = new ArrayList<>();
    }

    /**
     * Wysyła żądanie walidacji do serwera CAS.
     */
    public CommunicationLog validate(String readerNumber, String cardNumber) {
        String jsonBody = gson.toJson(new ValidationRequest(readerNumber, cardNumber));
        CommunicationLog log = httpService.sendGetWithBody(config.getValidationUrl(), jsonBody);
        history.add(log);
        return log;
    }

    /**
     * Parsuje wynik walidacji z udanego CommunicationLog.
     */
    public ValidationResult parseResult(CommunicationLog log) {
        if (!log.isSuccess() || log.getResponseBody() == null) {
            return null;
        }
        try {
            return gson.fromJson(log.getResponseBody(), ValidationResult.class);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    /**
     * Zwraca ostatni log komunikacyjny lub null, jeśli nie wykonano żadnych żądań.
     */
    public CommunicationLog getLastLog() {
        return history.isEmpty() ? null : history.get(history.size() - 1);
    }

    /**
     * Czyści historię komunikacji.
     */
    public void clearHistory() {
        history.clear();
    }

    /**
     * Zwraca liczbę wykonanych żądań.
     */
    public int getRequestCount() {
        return history.size();
    }

    private static class ValidationRequest {
        private final String readerNumber;
        private final String cardNumber;

        ValidationRequest(String readerNumber, String cardNumber) {
            this.readerNumber = readerNumber;
            this.cardNumber = cardNumber;
        }
    }
}
