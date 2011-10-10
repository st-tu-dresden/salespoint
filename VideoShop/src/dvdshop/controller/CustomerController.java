package dvdshop.controller;

import javax.servlet.http.HttpServletRequest;

import org.salespointframework.core.order.PersistentOrder;
import org.salespointframework.core.order.PersistentOrderManager;
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

	@RequestMapping("/settingssubmit")
	public ModelAndView settingsSubmit(HttpServletRequest request,
			ModelAndView mav, 
			@RequestParam("") String newPassword,
			@RequestParam("") String oldPassword,
			@RequestParam("") String adress) {

		Customer c = userManager.getUserByToken(Customer.class, request.getSession());
		boolean result = c.changePassword(newPassword, oldPassword);
		
		mav.addObject("result", result);
		mav.setViewName("settings");
		return mav;
	}

	// /basket im BasketController machts
	/*
	@RequestMapping("/orders")
	public ModelAndView myOrders(HttpServletRequest request, ModelAndView mav) {
		PersistentOrderManager pom = new PersistentOrderManager();

		Customer c = cm.getUserByToken(Customer.class, request.getSession());

		Iterable<PersistentOrder> orders = pom.find(c.getIdentifier());
		
		mav.addObject("orders", orders);
		mav.setViewName("orders");
		
		return mav;
	}
	*/
	
	@RequestMapping("/new")
	public ModelAndView register(HttpServletRequest request, ModelAndView mav,
			@RequestParam("name") UserIdentifier userIdentifier,
			@RequestParam("password") String password,
			@RequestParam("street") String street,
			@RequestParam("city") String city) {
		mav.setViewName("redirect:/");

		
		Customer customer = new Customer(userIdentifier, password,
				street + "\n" + city);
		userManager.add(customer);
		userManager.logOn(customer, request.getSession());

		return mav;
	}
	
	@RequestMapping("/logout")
	public ModelAndView logout(HttpServletRequest request, ModelAndView mav) {
		mav.setViewName("redirect:/");
		userManager.logOff(request.getSession());
		return mav;
	}
	
	@RequestMapping("/register")
	public String registerCustomer() {
		return "register";
	}

}
