package dvdshop.controller;

import java.util.HashMap;
import java.util.Map;

import org.salespointframework.core.inventory.PersistentInventory;
import org.salespointframework.core.order.OrderStatus;
import org.salespointframework.core.order.PersistentOrder;
import org.salespointframework.core.order.PersistentOrderManager;
import org.salespointframework.core.user.PersistentUserManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import dvdshop.model.Customer;
import dvdshop.model.VideoCatalog;

@Controller
public class BossController {
	
	private final PersistentOrderManager orderManager = new PersistentOrderManager();
	private final PersistentUserManager userManager = new PersistentUserManager();
	private final PersistentInventory inventory = new PersistentInventory();
	private final VideoCatalog videoCatalog = new VideoCatalog();
	
	@RequestMapping("/orderPerCustomer")
	public ModelAndView customers(ModelAndView mav) {
		Iterable<Customer> customers = userManager.find(Customer.class);
		Map<Customer, Iterable<PersistentOrder>> orders = new HashMap<Customer, Iterable<PersistentOrder>>(); 
		for(Customer customer : customers) {
			orders.put(customer, orderManager.find(customer.getIdentifier()));
		}
		
		mav.addObject("customers", customers);
		mav.addObject("orders", orders.entrySet());
		mav.setViewName("ordersPerCustomer");
		return mav;
	}
}
