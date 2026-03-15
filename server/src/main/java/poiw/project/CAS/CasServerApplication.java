package poiw.project.CAS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@Controller
public class CasServerApplication {

    @GetMapping("/")
    public String index() {
        return "redirect:/cards";
    }

    public static void main(String[] args) {
        SpringApplication.run(CasServerApplication.class, args);
    }
}
