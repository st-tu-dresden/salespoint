package dvdshop.controller;

import javax.servlet.http.HttpSession;

import org.salespointframework.core.user.PersistentUserManager;
import org.salespointframework.core.user.UserIdentifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dvdshop.model.Customer;

@Controller
public class CustomerController {
	
	private final PersistentUserManager userManager = new PersistentUserManager();

	@RequestMapping("/registerNew")
	public String registerNew(HttpSession session,
			@RequestParam("name") UserIdentifier userIdentifier,
			@RequestParam("password") String password,
			@RequestParam("street") String street,
			@RequestParam("city") String city) {
		
		Customer customer = new Customer(userIdentifier, password, street + "\n" + city);
		userManager.add(customer);
		userManager.login(customer, session);

		return "redirect:/";
	}
	
	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		userManager.logout(session);
		return "redirect:/";
	}
	
	@RequestMapping("/register")
	public String register() {
		return "register";
	}
}
