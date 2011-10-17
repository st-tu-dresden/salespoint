package dvdshop.controller;

import org.salespointframework.web.spring.annotations.Interceptors;
import org.salespointframework.web.spring.interceptors.LoginInterceptor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

//@Interceptors({ LoginInterceptor.class, UserInterceptor.class })
@Interceptors(LoginInterceptor.class)
@Controller
public class ShopController {

	@RequestMapping("/")
	public String index() {
		return "index";
	}

	@RequestMapping("/index")
	public String index2() {
		return "index";
	}
}
