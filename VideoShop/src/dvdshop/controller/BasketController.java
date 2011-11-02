package dvdshop.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.salespointframework.core.accountancy.payment.Cash;
import org.salespointframework.core.order.PersistentOrder;
import org.salespointframework.core.order.PersistentOrderLine;
import org.salespointframework.core.order.PersistentOrderManager;
import org.salespointframework.core.product.ProductIdentifier;
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
	public ModelAndView addDisc(HttpServletRequest request, ModelAndView mav, @RequestParam("pid") ProductIdentifier pid, @RequestParam("number") int number) {

		Customer customer = userManager.getUserByToken(Customer.class, request.getSession());
		
		if(number <= 0 || number > 5) number = 1;
		
		PersistentOrder order = (PersistentOrder) request.getSession().getAttribute("order");

		if (order == null) {
			order = new PersistentOrder(customer.getIdentifier(), Cash.CASH);
			orderManager.add(order);
			request.getSession().setAttribute("order", order);
		}

		Disc disc = videoCatalog.get(Disc.class, pid);

		PersistentOrderLine orderLine = new PersistentOrderLine(disc.getIdentifier(), number);

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
	public ModelAndView basket(HttpServletRequest request, ModelAndView mav) {
		boolean isEmpty = true;
		PersistentOrder order = (PersistentOrder) request.getSession().getAttribute("order");

		if (order != null) {
			mav.addObject("items", order.getOrderLines());
			isEmpty = Iterables.isEmpty(order.getOrderLines());
		} else {
			mav.addObject("items", new ArrayList<PersistentOrderLine>());
		}

		mav.addObject("isEmpty", isEmpty);
		
		mav.setViewName("basket");
		return mav;
	}

	@RequestMapping("/buy")
	public String buy(HttpServletRequest request) {

		PersistentOrder order = (PersistentOrder) request.getSession().getAttribute("order");

		if (order != null) {
			request.getSession().setAttribute("order", null);
			order.payOrder();
			order.completeOrder();
			orderManager.update(order);
		}
		return "index";
	}
}
