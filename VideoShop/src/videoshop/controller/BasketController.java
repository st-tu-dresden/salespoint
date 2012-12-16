package videoshop.controller;

import javax.servlet.http.HttpSession;

import org.salespointframework.core.accountancy.payment.Cash;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.order.ChargeLine;
import org.salespointframework.core.order.PersistentOrder;
import org.salespointframework.core.order.PersistentOrderLine;
import org.salespointframework.core.order.PersistentOrderManager;
import org.salespointframework.core.quantity.Quantity;
import org.salespointframework.core.quantity.Units;
import org.salespointframework.core.user.PersistentUserManager;
import org.salespointframework.util.Iterables;
import org.salespointframework.web.annotation.Capabilities;
import org.salespointframework.web.annotation.Get;
import org.salespointframework.web.annotation.LoggedInUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import videoshop.model.Customer;
import videoshop.model.Disc;
import videoshop.model.Dvd;
import videoshop.model.VideoCatalog;


@Controller
@Capabilities("customer")
public class BasketController {
	
	@Autowired
	private PersistentUserManager userManager;
	@Autowired
	private PersistentOrderManager orderManager;
	@Autowired
	private VideoCatalog videoCatalog;
	
	@RequestMapping("/addDisc")
	public String addDisc(@Get("pid") Disc disc, @RequestParam("number") int number,@LoggedInUser Customer customer, HttpSession session, ModelMap modelMap) 
	{
		if(number <= 0 || number > 5) number = 1;
		
		PersistentOrder order = (PersistentOrder) session.getAttribute("order");
		
		
		if (order == null) {
			order = new PersistentOrder(customer.getIdentifier(), Cash.CASH);
			// TODO remove
			order.addChargeLine(new ChargeLine(Money.ONE, "1"));
			order.addChargeLine(new ChargeLine(Money.OVER9000, "2"));
			order.addChargeLine(new ChargeLine(Money.ZERO, "3"));
			// TODO
			orderManager.add(order);
			session.setAttribute("order", order);
		}
		
		Quantity quantity = Units.of(number);

		PersistentOrderLine orderLine = new PersistentOrderLine(disc, quantity);

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
		PersistentOrder order = (PersistentOrder) session.getAttribute("order");

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
		PersistentOrder order = (PersistentOrder) session.getAttribute("order");

		// TODO remove
		ChargeLine cl = order.getChargeLines().iterator().next();
		order.removeChargeLine(cl.getIdentifier());
		orderManager.update(order);
		// TODO
		
		if (order != null) {
			session.setAttribute("order", null);
			order.payOrder();
			order.completeOrder();
			orderManager.update(order);
		}
		return "index";
	}
}
