package dvdshop.controller;

import org.salespointframework.core.inventory.PersistentInventory;
import org.salespointframework.core.inventory.PersistentInventoryItem;
import org.salespointframework.core.order.OrderStatus;
import org.salespointframework.core.order.PersistentOrder;
import org.salespointframework.core.order.PersistentOrderManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import dvdshop.model.VideoCatalog;

@Controller
public class BossController {
	
	private final PersistentOrderManager orderManager = new PersistentOrderManager();
	private final PersistentInventory inventory = new PersistentInventory();
	private final VideoCatalog catalog = new VideoCatalog();
	
	@RequestMapping("/orders")
	public ModelAndView orders(ModelAndView mav) {
		Iterable<PersistentOrder> ordersCompleted = orderManager.find(PersistentOrder.class, OrderStatus.COMPLETED);
		mav.addObject("ordersCompleted", ordersCompleted);
		mav.setViewName("orders");
		return mav;
	}
	
	@RequestMapping("/stock")
	public ModelAndView stock(ModelAndView mav) {
		Iterable<PersistentInventoryItem> stock = inventory.find(PersistentInventoryItem.class);
		mav.addObject("stock", stock);
		mav.setViewName("stock");
		return mav;
	}
}
