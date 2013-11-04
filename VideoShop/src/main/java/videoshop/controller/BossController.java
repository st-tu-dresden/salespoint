package videoshop.controller;

import org.salespointframework.core.inventory.Inventory;
import org.salespointframework.core.inventory.InventoryItem;
import org.salespointframework.core.order.Order;
import org.salespointframework.core.order.OrderManager;
import org.salespointframework.core.order.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import videoshop.model.Customer;
import videoshop.model.CustomerRepository;

// (｡◕‿◕｡)
// Straight forward?

@Controller
@PreAuthorize("hasRole('ROLE_BOSS')")
class BossController {
	
	private final OrderManager orderManager;
	private final Inventory inventory;
	private final CustomerRepository customerRepository;
	
	@Autowired
	public BossController(OrderManager orderManager, Inventory inventory, CustomerRepository customerRepository) {
		this.orderManager = orderManager;
		this.inventory = inventory;
		this.customerRepository = customerRepository;
	}
	
	@RequestMapping("/customers")
	public String customers(ModelMap modelMap) 
	{
		Iterable<Customer> customerList = customerRepository.findAll();
		modelMap.addAttribute("customerList", customerList);
		return "customers";
	}

	@RequestMapping("/orders")
	public String orders(ModelMap modelMap) 
	{
		Iterable<Order> completedOrders = orderManager.find(Order.class, OrderStatus.COMPLETED);
		modelMap.addAttribute("ordersCompleted", completedOrders);
		return "orders";
	}

	@RequestMapping("/stock")
	public String stock(ModelMap modelMap) 
	{
		Iterable<InventoryItem> stock = inventory.find(InventoryItem.class);
		modelMap.addAttribute("stock", stock);
		return "stock";
	}
}
