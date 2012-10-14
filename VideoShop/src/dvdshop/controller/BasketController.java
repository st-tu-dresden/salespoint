package dvdshop.controller;

import javax.servlet.http.HttpSession;

import org.salespointframework.core.accountancy.payment.Cash;
import org.salespointframework.core.order.PersistentOrder;
import org.salespointframework.core.order.PersistentOrderLine;
import org.salespointframework.core.order.PersistentOrderManager;
import org.salespointframework.core.product.ProductIdentifier;
import org.salespointframework.core.quantity.Quantity;
import org.salespointframework.core.quantity.Units;
import org.salespointframework.core.user.PersistentUserManager;
import org.salespointframework.util.Iterables;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import dvdshop.model.Customer;
import dvdshop.model.Disc;
import dvdshop.model.Dvd;
import dvdshop.model.VideoCatalog;

@Controller
public class BasketController {
	
	private final PersistentUserManager userManager = new PersistentUserManager();
	private final PersistentOrderManager orderManager = new PersistentOrderManager();
	private final VideoCatalog videoCatalog = new VideoCatalog();
	
	@RequestMapping("/addDisc")
	public ModelAndView addDisc(HttpSession session, ModelAndView mav, @RequestParam("pid") ProductIdentifier pid, @RequestParam("number") int number) {

		Customer customer = userManager.getUserByToken(Customer.class, session);
		
		if(number <= 0 || number > 5) number = 1;
		
		PersistentOrder order = (PersistentOrder) session.getAttribute("order");

		if (order == null) {
			order = new PersistentOrder(customer.getIdentifier(), Cash.CASH);
			orderManager.add(order);
			session.setAttribute("order", order);
		}

		Disc disc = videoCatalog.get(Disc.class, pid);
		
		Quantity q = Units.of(number);

		PersistentOrderLine orderLine = new PersistentOrderLine(disc.getIdentifier(), q);

		order.addOrderLine(orderLine);
		
		orderManager.update(order);
		
		if(disc instanceof Dvd) {
			mav.setViewName("redirect:dvdCatalog");
		} else {
			mav.setViewName("redirect:blurayCatalog");
		}
		return mav;
	}

	@RequestMapping("/shoppingBasket")
	public ModelAndView basket(HttpSession session, ModelAndView mav) {
		boolean isEmpty = true;
		PersistentOrder order = (PersistentOrder) session.getAttribute("order");

		if (order != null) {
			mav.addObject("order", order);
			isEmpty = Iterables.isEmpty(order.getOrderLines());
		}

		mav.addObject("isEmpty", isEmpty);
		
		mav.setViewName("basket");
		return mav;
	}

	@RequestMapping("/buy")
	public String buy(HttpSession session) {

		PersistentOrder order = (PersistentOrder) session.getAttribute("order");

		if (order != null) {
			session.setAttribute("order", null);
			order.payOrder();
			order.completeOrder();
			orderManager.update(order);
		}
		return "index";
	}
}
