package dvdshop.controller;

import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.salespointframework.core.accountancy.payment.Cash;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.order.PersistentOrder;
import org.salespointframework.core.order.PersistentOrderLine;
import org.salespointframework.core.product.ProductIdentifier;
import org.salespointframework.core.shop.Shop;
import org.salespointframework.core.user.PersistentUser;
import org.salespointframework.core.user.PersistentUserManager;
import org.salespointframework.core.user.UserIdentifier;
import org.salespointframework.util.Iterables;
import org.salespointframework.web.spring.annotations.Interceptors;
import org.salespointframework.web.spring.interceptors.LoginInterceptor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import dvdshop.model.Comment;
import dvdshop.model.Customer;
import dvdshop.model.Dvd;
import dvdshop.model.VideoCatalog;

@Interceptors({ LoginInterceptor.class, AlwaysInterceptor.class })
@Controller
public class ShopController {

	{
		Database.INSTANCE.initializeEntityManagerFactory("DVDShop");
	}

	VideoCatalog dvdCatalog = new VideoCatalog();

	PersistentUserManager userManager = new PersistentUserManager();

	/*
	 * @RequestMapping("/") public String index() { return "index"; }
	 */

	@RequestMapping("/")
	public ModelAndView index(ModelAndView mav) {

		mav.addObject("items", Iterables.toList(new VideoCatalog().findDvds()));
		mav.setViewName("index");

		return mav;
	}

	@RequestMapping("/dvdCatalog")
	public ModelAndView dvdCatalog(ModelAndView mav) {
		mav.addObject("items", Iterables.toList(new VideoCatalog().findDvds()));
		mav.setViewName("dvdCatalog");
		return mav;
	}

	@RequestMapping("/bluerayCatalog")
	public ModelAndView bluerayCatalog(ModelAndView mav) {
		mav.addObject("items", dvdCatalog.findBlueRays());
		mav.setViewName("bluerayCatalog");
		return mav;
	}

	@RequestMapping("/detail")
	public ModelAndView catalog(ModelAndView mav,
			@RequestParam("pid") ProductIdentifier pid) {
		Dvd dvd = dvdCatalog.getDvd(pid);
		mav.setViewName("detail");
		mav.addObject("dvd", dvd);
		mav.addObject("comments", Iterables.toList(dvd.getComments()));
		return mav;
	}

	@RequestMapping("/comment")
	public ModelAndView catalog(ModelAndView mav,
			@RequestParam("pid") ProductIdentifier pid,
			@RequestParam("comment") String comment,
			@RequestParam("rating") int rating) {

		Dvd dvd = dvdCatalog.getDvd(pid);

		dvd.addComment(new Comment(comment, rating));

		mav.setViewName("detail");
		mav.addObject("dvd", dvd);
		return mav;
	}

	@RequestMapping("/buy")
	public ModelAndView buy(HttpServletRequest request, ModelAndView mav,
			@RequestParam("pid") ProductIdentifier pid) {

		Customer customer = userManager.getUserByToken(Customer.class,
				request.getSession());

		PersistentOrder order = (PersistentOrder) request.getSession()
				.getAttribute("order");

		if (order == null) {
			order = new PersistentOrder(customer.getIdentifier(), Cash.CASH);
		}

		Dvd dvd = dvdCatalog.getDvd(pid);

		PersistentOrderLine orderLine = new PersistentOrderLine(
				dvd.getIdentifier());

		order.addOrderLine(orderLine);

		mav.addObject("items", dvdCatalog.findDvds());
		mav.setViewName("catalog");
		return mav;
	}

	@RequestMapping("basket")
	public ModelAndView basket(HttpServletRequest request,

	ModelAndView mav) {
		Customer customer =

		userManager.getUserByToken(Customer.class, request.getSession());

		PersistentOrder order = (PersistentOrder)

		request.getSession().getAttribute("order");

		if (order != null) {
			mav.addObject("items", Iterables.toList(order.getOrderLines()));
		} else {
			mav.addObject("items", new ArrayList<PersistentOrderLine>());
		}

		mav.setViewName("basket");
		return mav;
	}

	@RequestMapping("buy2")
	public ModelAndView buy2(HttpServletRequest request, ModelAndView mav) {

		Customer customer =	userManager.getUserByToken(Customer.class, request.getSession());

		PersistentOrder order = (PersistentOrder) request.getSession().getAttribute("order");

		if (order != null) {
			request.getSession().setAttribute("order", null);
			order.payOrder();
			order.completeOrder();
		}

		mav.setViewName("index");
		return mav;
	}

	@RequestMapping("/register")
	public String registerCustomer() {
		return "register";
	}
}
