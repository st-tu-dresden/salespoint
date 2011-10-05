package dvdshop.controller;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.salespointframework.core.database.Database;

import org.salespointframework.core.order.PersistentOrder;
import org.salespointframework.core.order.PersistentOrderManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import dvdshop.model.Customer;
import dvdshop.model.CustomerManager;

@Controller
public class CustomerController {
	
	
	{
		Database.INSTANCE.initializeEntityManagerFactory("DVDShop");
	}
	
	@RequestMapping("settings")
	public ModelAndView settings(HttpServletRequest request, ModelAndView mav) {
		CustomerManager cm = new CustomerManager();	
		PersistentOrderManager pom = new PersistentOrderManager();

		Customer c = cm.getUserByToken(request.getSession());
		
		mav.addObject("customer", c);
		mav.setViewName("settings");

		return mav;
	}

	@RequestMapping("settingssubmit")
	public ModelAndView settingsSubmit(HttpServletRequest request,
			ModelAndView mav, 
			@RequestParam("") String newPassword,
			@RequestParam("") String oldPassword,
			@RequestParam("") String adress) {

		EntityManager em = Database.INSTANCE.getEntityManagerFactory().createEntityManager();
		CustomerManager cm = new CustomerManager();
		Customer c = cm.getUserByToken(request.getSession());
		boolean result = c.changePassword(newPassword, oldPassword);
		
		em.getTransaction().begin();
		em.getTransaction().commit();

		mav.addObject("result", result);
		mav.setViewName("settings");
		return mav;
	}

	@RequestMapping("orders")
	public ModelAndView myOrders(HttpServletRequest request, ModelAndView mav) {
		CustomerManager cm = new CustomerManager();	
		PersistentOrderManager pom = new PersistentOrderManager();

		Customer c = cm.getUserByToken(request.getSession());

		Iterable<PersistentOrder> orders = pom.find(c.getIdentifier());
		
		mav.addObject("orders", orders);
		mav.setViewName("orders");
		
		return mav;
	}
}
