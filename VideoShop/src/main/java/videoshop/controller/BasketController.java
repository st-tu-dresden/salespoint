package videoshop.controller;

import javax.servlet.http.HttpSession;

import org.salespointframework.core.accountancy.payment.Cash;
import org.salespointframework.core.order.Basket;
import org.salespointframework.core.order.Order;
import org.salespointframework.core.order.OrderLine;
import org.salespointframework.core.order.OrderManager;
import org.salespointframework.core.quantity.Quantity;
import org.salespointframework.core.quantity.Units;
import org.salespointframework.core.useraccount.UserAccount;
import org.salespointframework.web.annotation.LoggedInUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import videoshop.model.Disc;
import videoshop.model.Dvd;

@Controller
class BasketController {

	private final OrderManager orderManager;

	/**
	 * @param orderManager
	 */
	@Autowired
	public BasketController(OrderManager orderManager) {
		this.orderManager = orderManager;
	}

	@RequestMapping(value = "/addDisc", method = RequestMethod.POST)
	public String addDisc(@RequestParam("pid") Disc disc,
			@RequestParam("number") int number, HttpSession session,
			ModelMap modelMap) {
		if (number <= 0 || number > 5) {
			number = 1;
		}

		Basket basket = (Basket) session.getAttribute("basket");

		if (basket == null) {
			basket = new Basket();
			session.setAttribute("basket", basket);
		}

		Quantity quantity = Units.of(number);

		OrderLine orderLine = new OrderLine(disc, quantity);

		basket.addOrderLine(orderLine);

		if (disc instanceof Dvd) {
			return "redirect:dvdCatalog";
		} else {
			return "redirect:blurayCatalog";
		}
	}

	@RequestMapping("/shoppingBasket")
	public String basket(HttpSession session, ModelMap modelMap) {
		Basket basket = (Basket) session.getAttribute("basket");

		if (basket == null) {
			basket = new Basket();
		}
		modelMap.addAttribute("basket", basket);

		return "basket";
	}

	@RequestMapping("/buy")
	public String buy(HttpSession session, @LoggedInUser UserAccount userAccount) {

		Basket basket = (Basket) session.getAttribute("basket");
		Order order = new Order(userAccount.getIdentifier(), Cash.CASH);
		basket.commit(order);

		orderManager.pay(order);
		orderManager.completeOrder(order);

		session.removeAttribute("basket");

		return "index";
	}
}
