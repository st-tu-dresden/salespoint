package dvdshop.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.salespointframework.core.accountancy.payment.Cash;
import org.salespointframework.core.order.OrderCompletionResult;
import org.salespointframework.core.order.PersistentOrder;
import org.salespointframework.core.order.PersistentOrderLine;
import org.salespointframework.core.order.PersistentOrderManager;
import org.salespointframework.core.product.ProductIdentifier;
import org.salespointframework.core.user.PersistentUserManager;
import org.salespointframework.util.Iterables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import dvdshop.model.Customer;
import dvdshop.model.Disc;
import dvdshop.model.VideoCatalog;

@Controller
public class BasketController {
	
	PersistentUserManager userManager = new PersistentUserManager();
	
	@Autowired
	VideoCatalog videoCatalog;
	
	@RequestMapping("/addDisc")
	public ModelAndView addDisc(HttpServletRequest request, ModelAndView mav, @RequestParam("pid") ProductIdentifier pid) {

		Customer customer = userManager.getUserByToken(Customer.class, request.getSession());

		PersistentOrder order = (PersistentOrder) request.getSession().getAttribute("order");

		if (order == null) {
			order = new PersistentOrder(customer.getIdentifier(), Cash.CASH);
			request.getSession().setAttribute("order", order);
		}

		Disc disc = videoCatalog.get(Disc.class, pid);

		PersistentOrderLine orderLine = new PersistentOrderLine(disc.getIdentifier());

		order.addOrderLine(orderLine);

		mav.addObject("items", Iterables.asList(videoCatalog.findDvds()));
		mav.setViewName("dvdCatalog");
		return mav;
	}

	@RequestMapping("/basket")
	public ModelAndView basket(HttpServletRequest request, ModelAndView mav) {
		boolean isEmpty = true;
		PersistentOrder order = (PersistentOrder) request.getSession().getAttribute("order");

		if (order != null) {
			mav.addObject("items", Iterables.asList(order.getOrderLines()));
			isEmpty = Iterables.isEmpty(order.getOrderLines());
		} else {
			mav.addObject("items", new ArrayList<PersistentOrderLine>());
		}

		mav.addObject("isEmpty", isEmpty);
		
		mav.setViewName("basket");
		return mav;
	}

	@RequestMapping("/buy")
	public ModelAndView buy(HttpServletRequest request, ModelAndView mav) {

		PersistentOrder order = (PersistentOrder) request.getSession().getAttribute("order");

		if (order != null) {
			request.getSession().setAttribute("order", null);
			order.payOrder();
			System.out.println(order.getOrderStatus());
			OrderCompletionResult ocr = order.completeOrder();
			
			System.out.println(ocr.getStatus());
			//System.out.println(ocr.getException().toString());
			new PersistentOrderManager().add(order);
		}

		mav.setViewName("index");
		return mav;
	}

}
