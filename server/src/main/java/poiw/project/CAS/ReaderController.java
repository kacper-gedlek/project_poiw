package poiw.project.CAS;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/readers")
public class ReaderController {
    private final ReaderRepository readerRepository;

    public ReaderController(ReaderRepository readerRepository) {
        this.readerRepository = readerRepository;
    }

    @GetMapping
    public String listReaders(Model model) {
        model.addAttribute("readers", readerRepository.findAll());
        if (!model.containsAttribute("newReader")) {
            model.addAttribute("newReader", new Reader());
        }
        return "readers";
    }

    @PostMapping("/add")
    public String addReader(@Valid @ModelAttribute("newReader") Reader reader, BindingResult result, Model model) {
        if (reader.getReaderNumber() != null && readerRepository.findByReaderNumber(reader.getReaderNumber()).isPresent()) {
            result.rejectValue("readerNumber", "error.duplicate", "Reader Number already exists!");
        }

        if (result.hasErrors()) {
            model.addAttribute("readers", readerRepository.findAll());
            return "readers";
        }

        readerRepository.save(reader);
        return "redirect:/readers";
    }

    @PostMapping("/delete/{id}")
    public String deleteReader(@PathVariable Long id) {
        readerRepository.deleteById(id);
        return "redirect:/readers";
    }
}
