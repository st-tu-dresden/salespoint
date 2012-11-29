package videoshop.controller;

import org.salespointframework.core.inventory.PersistentInventory;
import org.salespointframework.core.inventory.PersistentInventoryItem;
import org.salespointframework.core.order.OrderStatus;
import org.salespointframework.core.order.PersistentOrder;
import org.salespointframework.core.order.PersistentOrderManager;
import org.salespointframework.web.annotation.Capabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@Capabilities("boss")
public class BossController {
	
	@Autowired
	private PersistentOrderManager orderManager;
	@Autowired
	private PersistentInventory inventory;
	
	@RequestMapping("/orders")
	public String orders(ModelMap modelMap) 
	{
		Iterable<PersistentOrder> completedOrders = orderManager.find(PersistentOrder.class, OrderStatus.COMPLETED);
		modelMap.addAttribute("ordersCompleted", completedOrders);
		return "orders";
	}

	@RequestMapping("/stock")
	public String stock(ModelMap modelMap) 
	{
		Iterable<PersistentInventoryItem> stock = inventory.find(PersistentInventoryItem.class);
		modelMap.addAttribute("stock", stock);
		return "stock";
	}
}
