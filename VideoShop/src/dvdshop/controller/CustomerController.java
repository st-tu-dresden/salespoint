package dvdshop.controller;

import javax.servlet.http.HttpServletRequest;

import org.salespointframework.core.user.PersistentUserManager;
import org.salespointframework.core.user.UserIdentifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dvdshop.model.Customer;

@Controller
public class CustomerController {
	
	private PersistentUserManager userManager = new PersistentUserManager();

	@RequestMapping("/registerNew")
	public String register(HttpServletRequest request,
			@RequestParam("name") UserIdentifier userIdentifier,
			@RequestParam("password") String password,
			@RequestParam("street") String street,
			@RequestParam("city") String city) {
		
		Customer customer = new Customer(userIdentifier, password,	street + "\n" + city);
		userManager.add(customer);
		userManager.login(customer, request.getSession());

		return "redirect:/";
	}
	
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request) {
		userManager.logout(request.getSession());
		return "redirect:/";
	}
	
	@RequestMapping("/register")
	public String registerCustomer() {
		return "register";
	}
}
