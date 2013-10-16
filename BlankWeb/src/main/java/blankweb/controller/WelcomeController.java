package blankweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WelcomeController {
	@RequestMapping({"/", "index"})
    public String index() {
		return "welcome";
    }
}