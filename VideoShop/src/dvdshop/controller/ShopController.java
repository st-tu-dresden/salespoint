package dvdshop.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.salespointframework.core.accountancy.payment.Cash;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.order.OrderCompletionResult;
import org.salespointframework.core.order.PersistentOrder;
import org.salespointframework.core.order.PersistentOrderLine;
import org.salespointframework.core.product.ProductIdentifier;
import org.salespointframework.core.user.PersistentUserManager;
import org.salespointframework.util.Iterables;
import org.salespointframework.web.spring.annotations.Interceptors;
import org.salespointframework.web.spring.interceptors.LoginInterceptor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import dvdshop.model.Comment;
import dvdshop.model.Customer;
import dvdshop.model.Disc;
import dvdshop.model.Dvd;
import dvdshop.model.VideoCatalog;

@Interceptors({ LoginInterceptor.class, UserInterceptor.class })
@Controller
public class ShopController {

	{
		Database.INSTANCE.initializeEntityManagerFactory("DVDShop");
	}

	VideoCatalog videoCatalog = new VideoCatalog();

	PersistentUserManager userManager = new PersistentUserManager();

	/*
	 * @RequestMapping("/") public String index() { return "index"; }
	 */

	@RequestMapping("/")
	public ModelAndView index(ModelAndView mav) {

		mav.addObject("items", Iterables.asList(new VideoCatalog().findDvds()));
		mav.setViewName("index");

		return mav;
	}

	@RequestMapping("/index")
	public ModelAndView index__(ModelAndView mav) {

		mav.addObject("items", Iterables.asList(new VideoCatalog().findDvds()));
		mav.setViewName("index");

		return mav;
	}
	
	@RequestMapping("/dvdCatalog")
	public ModelAndView dvdCatalog(ModelAndView mav) {
		mav.addObject("items", Iterables.asList(new VideoCatalog().findDvds()));
		mav.setViewName("dvdCatalog");
		return mav;
	}

	@RequestMapping("/bluerayCatalog")
	public ModelAndView bluerayCatalog(ModelAndView mav) {
		mav.addObject("items", videoCatalog.findBlueRays());
		mav.setViewName("bluerayCatalog");
		return mav;
	}

	
	@RequestMapping("/detail")
	public ModelAndView catalog(ModelAndView mav,
			@RequestParam("pid") ProductIdentifier pid) {
		Dvd dvd = videoCatalog.getDvd(pid);
		mav.setViewName("detail");
		mav.addObject("dvd", dvd);
		mav.addObject("comments", Iterables.asList(dvd.getComments()));
		return mav;
	}

	@RequestMapping("/comment")
	public ModelAndView catalog(ModelAndView mav,
			@RequestParam("pid") ProductIdentifier pid,
			@RequestParam("comment") String comment,
			@RequestParam("rating") int rating ) {

		Disc disc = videoCatalog.get(Disc.class, pid);

		disc.addComment(new Comment(comment, rating));

		videoCatalog.update(disc);
		
		mav.addObject("dvd", disc);
		mav.setViewName("detail");
		
		return mav;
	}
	
		 

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

	@RequestMapping("basket")
	public ModelAndView basket(HttpServletRequest request, ModelAndView mav) {

		PersistentOrder order = (PersistentOrder) request.getSession().getAttribute("order");

		if (order != null) {
			mav.addObject("items", Iterables.asList(order.getOrderLines()));
		} else {
			mav.addObject("items", new ArrayList<PersistentOrderLine>());
		}

		mav.setViewName("basket");
		return mav;
	}

	@RequestMapping("buy")
	public ModelAndView buy(HttpServletRequest request, ModelAndView mav) {

		PersistentOrder order = (PersistentOrder) request.getSession().getAttribute("order");

		if (order != null) {
			request.getSession().setAttribute("order", null);
			order.payOrder();
			System.out.println(order.getOrderStatus());
			OrderCompletionResult ocr = order.completeOrder();
			
			System.out.println(ocr.getStatus());
			//System.out.println(ocr.getException().toString());
		}

		mav.setViewName("index");
		return mav;
	}

	@RequestMapping("/register")
	public String registerCustomer() {
		return "register";
	}

}
