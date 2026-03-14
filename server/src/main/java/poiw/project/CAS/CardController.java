package poiw.project.CAS;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/cards")
public class CardController {
    private final CardRepository cardRepository;

    public CardController(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @GetMapping
    public String listCards(Model model) {
        model.addAttribute("cards", cardRepository.findAll());
        if (!model.containsAttribute("newCard")) {
            model.addAttribute("newCard", new Card());
        }
        return "cards";
    }

    @PostMapping("/add")
    public String addCard(@Valid @ModelAttribute("newCard") Card card, BindingResult result, Model model) {
        // 1. Check if the card number already exists in the database
        if (card.getCardNumber() != null && cardRepository.findByCardNumber(card.getCardNumber()).isPresent()) {
            result.rejectValue("cardNumber", "error.duplicate", "Card Number already exists!");
        }

        // 2. If there are any errors (duplicate or validation), return to the form
        if (result.hasErrors()) {
            model.addAttribute("cards", cardRepository.findAll());
            return "cards";
        }

        // 3. Save only if everything is fine
        cardRepository.save(card);
        return "redirect:/cards";
    }

    @PostMapping("/toggle/{id}")
    public String toggleStatus(@PathVariable Long id) {
        cardRepository.findById(id).ifPresent(card -> {
            if ("ACTIVE".equals(card.getStatus())) {
                card.setStatus("INACTIVE");
            } else {
                card.setStatus("ACTIVE");
            }
            cardRepository.save(card);
        });
        return "redirect:/cards";
    }

    @PostMapping("/delete/{id}")
    public String deleteCard(@PathVariable Long id) {
        cardRepository.deleteById(id);
        return "redirect:/cards";
    }
}
