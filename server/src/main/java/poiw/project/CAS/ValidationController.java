package poiw.project.CAS;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class ValidationController {

    private final CardRepository cardRepository;
    private final ReaderRepository readerRepository;

    public ValidationController(CardRepository cardRepository, ReaderRepository readerRepository) {
        this.cardRepository = cardRepository;
        this.readerRepository = readerRepository;
    }

    @PostMapping(value = "/validation", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ValidationResponse> validateAccess(@RequestBody ValidationRequest request) {
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

        return ResponseEntity.ok(new ValidationResponse(cardOwner, readerName, validation));
    }
}