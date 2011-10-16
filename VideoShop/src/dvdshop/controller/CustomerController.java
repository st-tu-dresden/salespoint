package dvdshop.controller;

import javax.servlet.http.HttpServletRequest;

import org.salespointframework.core.user.PersistentUserManager;
import org.salespointframework.core.user.UserIdentifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import dvdshop.model.Customer;

@Controller
public class CustomerController {
	
	PersistentUserManager userManager = new PersistentUserManager();

	/*
	@RequestMapping("/settingssubmit")
	public ModelAndView settingsSubmit(HttpServletRequest request,
			ModelAndView mav, 
			@RequestParam("") String newPassword,
			@RequestParam("") String oldPassword,
			@RequestParam("") String adress) {

		Customer customer = userManager.getUserByToken(Customer.class, request.getSession());
		boolean result = customer.changePassword(newPassword, oldPassword);
		
		mav.addObject("result", result);
		mav.setViewName("settings");
		return mav;
	}
	*/

	@RequestMapping("/new")
	public ModelAndView register(HttpServletRequest request, ModelAndView mav,
			@RequestParam("name") UserIdentifier userIdentifier,
			@RequestParam("password") String password,
			@RequestParam("street") String street,
			@RequestParam("city") String city) {
		
		Customer customer = new Customer(userIdentifier, password,	street + "\n" + city);
		userManager.add(customer);
		userManager.logOn(customer, request.getSession());

		mav.setViewName("redirect:/");
		return mav;
	}
	
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request) {
		userManager.logOff(request.getSession());
		return "redirect:/";
	}
	
	@RequestMapping("/register")
	public String registerCustomer() {
		return "register";
	}

}
