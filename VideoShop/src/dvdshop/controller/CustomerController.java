package dvdshop.controller;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.salespointframework.core.database.Database;

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

	{
		Database.INSTANCE.initializeEntityManagerFactory("DVDShop");
	}
	
	@RequestMapping("settings")
	public ModelAndView settings(HttpServletRequest request, ModelAndView mav) {
		PersistentUserManager userManager = new PersistentUserManager();	
		PersistentOrderManager orderManager = new PersistentOrderManager();

		Customer customer = userManager.getUserByToken(Customer.class, request.getSession());
		
		mav.addObject("customer", customer);
		mav.setViewName("settings");

		return mav;
	}

	@RequestMapping("settingssubmit")
	public ModelAndView settingsSubmit(HttpServletRequest request,
			ModelAndView mav, 
			@RequestParam("") String newPassword,
			@RequestParam("") String oldPassword,
			@RequestParam("") String adress) {

		PersistentUserManager cm = new PersistentUserManager();
		Customer c = cm.getUserByToken(Customer.class, request.getSession());
		boolean result = c.changePassword(newPassword, oldPassword);
		
		mav.addObject("result", result);
		mav.setViewName("settings");
		return mav;
	}

	@RequestMapping("orders")
	public ModelAndView myOrders(HttpServletRequest request, ModelAndView mav) {
		PersistentUserManager cm = new PersistentUserManager();	
		PersistentOrderManager pom = new PersistentOrderManager();

		Customer c = cm.getUserByToken(Customer.class, request.getSession());

		Iterable<PersistentOrder> orders = pom.find(c.getIdentifier());
		
		mav.addObject("orders", orders);
		mav.setViewName("orders");
		
		return mav;
	}
	
	@RequestMapping("/new")
	public ModelAndView register(HttpServletRequest request, ModelAndView mav,
			@RequestParam("name") UserIdentifier userIdentifier,
			@RequestParam("password") String password,
			@RequestParam("street") String street,
			@RequestParam("city") String city) {
		mav.setViewName("redirect:/");

		PersistentUserManager pm = new PersistentUserManager();
		Customer customer = new Customer(userIdentifier, password,
				street + "\n" + city);
		pm.add(customer);
		pm.logOn(customer, request.getSession());

		return mav;
	}

}
