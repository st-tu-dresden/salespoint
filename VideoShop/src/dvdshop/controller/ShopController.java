package dvdshop.controller;

import javax.servlet.http.HttpServletRequest;

import org.salespointframework.core.database.Database;
import org.salespointframework.core.order.PersistentOrder;
import org.salespointframework.core.product.ProductIdentifier;
import org.salespointframework.core.user.PersistentUserManager;
import org.salespointframework.util.Iterables;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import dvdshop.model.Comment;
import dvdshop.model.Customer;
import dvdshop.model.Dvd;
import dvdshop.model.VideoCatalog;

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
		
		
		mav.addObject("items",
				Iterables.toList(new VideoCatalog().find(Dvd.class)));
		mav.setViewName("index");
		return mav;
	}

	@RequestMapping("/catalog")
	public ModelAndView catalog(ModelAndView mav) {
		mav.addObject("items", dvdCatalog.findDvds());
		mav.setViewName("catalog");
		return mav;
	}

	@RequestMapping("/detail")
	public ModelAndView catalog(ModelAndView mav,
			@RequestParam("pid") ProductIdentifier pid) {
		//ProductIdentifier pi = new ProductIdentifier(/* pid */); // TODO
		Dvd dvd = dvdCatalog.getDvd(pid);
		mav.setViewName("detail");
		mav.addObject("dvd", dvd);
		return mav;
	}

	@RequestMapping("/comment")
	public ModelAndView catalog(ModelAndView mav,
			@RequestParam("pid") ProductIdentifier pid,
			@RequestParam("comment") String comment,
			@RequestParam("rating") int rating) {

		//ProductIdentifier pi = new ProductIdentifier(/* pid */); // TODO
		Dvd dvd = dvdCatalog.getDvd(pid);

		dvd.addComment(new Comment(comment, rating));

		mav.setViewName("detail");
		mav.addObject("dvd", dvd);
		return mav;
	}

	@RequestMapping("/buy")
	public ModelAndView buy(HttpServletRequest request, ModelAndView mav,
			@RequestParam("pid") ProductIdentifier pid) {

		Customer customer = userManager.getUserByToken(Customer.class, request.getSession());

		PersistentOrder order = (PersistentOrder) request.getSession().getAttribute("order");
		
		if (order == null) {
			//PersistentOrder = new PersistentOrder(customer.getIdentifier(), "");
		}

		Dvd dvd = dvdCatalog.getDvd(pid);

		//dvdInventory.addProductInstance(dvdi);

		

		//mav.addObject("items", dvdCatalog.getProductTypes());
		mav.setViewName("catalog");
		return mav;
	}
}