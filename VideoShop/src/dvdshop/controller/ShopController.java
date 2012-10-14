package dvdshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ShopController {

	@RequestMapping({"/", "/index"})
	public String index() {
		return "index";
	}
	
	
}
