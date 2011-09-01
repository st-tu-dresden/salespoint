package guestbook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

	@RequestMapping("/")
	public String index() {
		return "/main/index";
	}

	@RequestMapping("/really")
	public String really() {
		return "/main/really";
	}
}
