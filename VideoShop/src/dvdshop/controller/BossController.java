package dvdshop.controller;

import org.salespointframework.core.order.OrderStatus;
import org.salespointframework.core.order.PersistentOrder;
import org.salespointframework.core.order.PersistentOrderManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BossController {
	
	private final PersistentOrderManager orderManager = new PersistentOrderManager();
	
	@RequestMapping("/orders")
	public ModelAndView orders(ModelAndView mav) {

		Iterable<PersistentOrder> ordersOpen = orderManager.find(OrderStatus.OPEN);
		Iterable<PersistentOrder> ordersCompleted = orderManager.find(OrderStatus.COMPLETED);
		
		mav.addObject("ordersOpen", ordersOpen);
		mav.addObject("ordersCompleted", ordersCompleted);
		mav.setViewName("orders");
		return mav;
	}
}
