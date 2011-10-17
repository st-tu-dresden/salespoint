package example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WelcomeController {
	@RequestMapping("/")
    public String index() {
		return "welcome";
    }
	
	@RequestMapping("/index")
	public String index2() {
		return "welcome";
	}
	
}