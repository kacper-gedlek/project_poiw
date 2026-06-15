package poiw.project.CAS;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Random;

@Controller
@RequestMapping("/mock-client")
public class MockClientController {

    private final CardRepository cardRepository;
    private final ReaderRepository readerRepository;
    private final Random random = new Random();

    private int requestCount = 0;
    private String initializedReaderNumber;
    private String initializedReaderName;

    public MockClientController(CardRepository cardRepository, ReaderRepository readerRepository) {
        this.cardRepository = cardRepository;
        this.readerRepository = readerRepository;
    }

    @GetMapping
    public String showMockClient(Model model) {
        addDefaultModel(model, new ValidationRequest());
        return "mock-client";
    }

    @PostMapping("/initialize-reader")
    public String initializeReader(@ModelAttribute ValidationRequest request, Model model) {
        initializedReaderNumber = request.getReaderNumber();
        initializedReaderName = "Simulated RFID Reader";

        model.addAttribute("message", "Reader initialized: " + initializedReaderNumber);
        addDefaultModel(model, request);
        return "mock-client";
    }

    @PostMapping("/random-reader")
    public String randomReader(@ModelAttribute ValidationRequest request, Model model) {
        String randomReaderNumber = generateRandomNumber();

        initializedReaderNumber = randomReaderNumber;
        initializedReaderName = "Random RFID Reader";

        request.setReaderNumber(randomReaderNumber);

        model.addAttribute("message", "Random reader initialized: " + randomReaderNumber);
        addDefaultModel(model, request);
        return "mock-client";
    }

    @PostMapping("/random-card")
    public String randomCard(@ModelAttribute ValidationRequest request, Model model) {
        request.setCardNumber(generateRandomNumber());

        model.addAttribute("message", "Random card generated: " + request.getCardNumber());
        addDefaultModel(model, request);
        return "mock-client";
    }

    @PostMapping("/validate")
    public String validate(@ModelAttribute ValidationRequest request, Model model) {
        long start = System.currentTimeMillis();

        if ((request.getReaderNumber() == null || request.getReaderNumber().isBlank()) && initializedReaderNumber != null) {
            request.setReaderNumber(initializedReaderNumber);
        }

        Optional<Reader> readerOpt = readerRepository.findByReaderNumber(request.getReaderNumber());
        Optional<Card> cardOpt = cardRepository.findByCardNumber(request.getCardNumber());

        String readerName = readerOpt.map(Reader::getReaderName).orElse(null);
        String cardOwner = cardOpt.map(Card::getCardOwner).orElse(null);
        String validation;

        if (readerOpt.isPresent() && cardOpt.isPresent()) {
            Reader reader = readerOpt.get();
            Card card = cardOpt.get();

            if (card.getAccessLevel() >= reader.getRequiredAccessLevel()) {
                validation = "ACCESS_GRANTED";
            } else {
                validation = "ACCESS_DENIED";
            }
        } else {
            validation = "ERROR";
        }

        ValidationResponse response = new ValidationResponse(cardOwner, readerName, validation);
        requestCount++;

        long responseTime = System.currentTimeMillis() - start;

        model.addAttribute("response", response);
        model.addAttribute("responseTime", responseTime);
        model.addAttribute("requestJson", buildRequestJson(request));
        model.addAttribute("responseJson", buildResponseJson(response));
        model.addAttribute("message", "Validation request sent.");

        addDefaultModel(model, request);
        return "mock-client";
    }

    @PostMapping("/reset")
    public String reset(Model model) {
        initializedReaderNumber = null;
        initializedReaderName = null;
        requestCount = 0;

        model.addAttribute("message", "Mock client reset.");
        addDefaultModel(model, new ValidationRequest());
        return "mock-client";
    }

    private void addDefaultModel(Model model, ValidationRequest request) {
        model.addAttribute("validationRequest", request);
        model.addAttribute("requestCount", requestCount);
        model.addAttribute("initializedReaderNumber", initializedReaderNumber);
        model.addAttribute("initializedReaderName", initializedReaderName);
        model.addAttribute("cardsCount", cardRepository.count());
        model.addAttribute("readersCount", readerRepository.count());
    }

    private String generateRandomNumber() {
        StringBuilder sb = new StringBuilder(16);
        for (int i = 0; i < 16; i++) {
            sb.append(Integer.toHexString(random.nextInt(16)).toUpperCase());
        }
        return sb.toString();
    }

    private String buildRequestJson(ValidationRequest request) {
        return "{\n" +
                "  \"readerNumber\": \"" + nullToEmpty(request.getReaderNumber()) + "\",\n" +
                "  \"cardNumber\": \"" + nullToEmpty(request.getCardNumber()) + "\"\n" +
                "}";
    }

    private String buildResponseJson(ValidationResponse response) {
        return "{\n" +
                "  \"cardOwner\": " + jsonValue(response.getCardOwner()) + ",\n" +
                "  \"readerName\": " + jsonValue(response.getReaderName()) + ",\n" +
                "  \"validation\": \"" + response.getValidation() + "\"\n" +
                "}";
    }

    private String jsonValue(String value) {
        return value == null ? "null" : "\"" + value + "\"";
    }

    private String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}