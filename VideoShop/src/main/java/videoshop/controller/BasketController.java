package videoshop.controller;

import javax.servlet.http.HttpSession;

import org.salespointframework.core.accountancy.payment.Cash;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.order.ChargeLine;
import org.salespointframework.core.order.Order;
import org.salespointframework.core.order.OrderLine;
import org.salespointframework.core.order.OrderManager;
import org.salespointframework.core.quantity.Quantity;
import org.salespointframework.core.quantity.Units;
import org.salespointframework.util.Iterables;
import org.salespointframework.web.annotation.Capabilities;
import org.salespointframework.web.annotation.LoggedInUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import videoshop.model.Customer;
import videoshop.model.Disc;
import videoshop.model.Dvd;


@Controller
@Capabilities("customer")
class BasketController {
	
	private final OrderManager orderManager;
	
	/**
	 * @param orderManager
	 */
	@Autowired
	public BasketController(OrderManager orderManager) {
		this.orderManager = orderManager;
	}

	@RequestMapping("/addDisc")
	public String addDisc(@PathVariable("pid") Disc disc, @RequestParam("number") int number,
			@LoggedInUser Customer customer, HttpSession session, ModelMap modelMap) 
	{
		if(number <= 0 || number > 5) number = 1;
		
		Order order = (Order) session.getAttribute("order");
		
		
		if (order == null) {
			order = new Order(customer.getIdentifier(), Cash.CASH);
			// TODO remove
			order.addChargeLine(new ChargeLine(Money.ONE, "1"));
			order.addChargeLine(new ChargeLine(Money.OVER9000, "2"));
			order.addChargeLine(new ChargeLine(Money.ZERO, "3"));
			// TODO
			orderManager.add(order);
			session.setAttribute("order", order);
		}
		
		Quantity quantity = Units.of(number);

		OrderLine orderLine = new OrderLine(disc, quantity);

		order.addOrderLine(orderLine);
		
		orderManager.update(order);
		
		if(disc instanceof Dvd) {
			return "redirect:dvdCatalog";
		} else {
			return "redirect:blurayCatalog";
		}
	}

	@RequestMapping("/shoppingBasket")
	public String basket(HttpSession session, ModelMap modelMap) 
	{
		boolean isEmpty = true;
		Order order = (Order) session.getAttribute("order");

		if (order != null) {
			modelMap.addAttribute("order", order);
			isEmpty = Iterables.isEmpty(order.getOrderLines());
		}

		modelMap.addAttribute("isEmpty", isEmpty);
		
		return "basket";
	}

	@RequestMapping("/buy")
	public String buy(HttpSession session) 
	{
		Order order = (Order) session.getAttribute("order");

		// TODO remove
		ChargeLine cl = order.getChargeLines().iterator().next();
		order.removeChargeLine(cl.getIdentifier());
		orderManager.update(order);
		// TODO
		
		if (order != null) {
			session.setAttribute("order", null);
			order.payOrder();
			orderManager.completeOrder(order);
		}
		return "index";
	}
}
