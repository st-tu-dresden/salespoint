package dvdshop.controller;

import javax.servlet.http.HttpServletRequest;

import org.salespointframework.core.database.Database;
import org.salespointframework.core.order.PersistentOrder;
import org.salespointframework.core.product.ProductIdentifier;
import org.salespointframework.util.Iterables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import dvdshop.model.Comment;
import dvdshop.model.Customer;
import dvdshop.model.CustomerManager;
import dvdshop.model.DVD;
import dvdshop.model.VideoCatalog;
import dvdshop.model.DVDInstance;
import dvdshop.model.DVDInventory;

@Controller
public class ShopController {

	{
		Database.INSTANCE.initializeEntityManagerFactory("DVDShop");
	}


	VideoCatalog dvdCatalog = new VideoCatalog();

	CustomerManager customerManager = new CustomerManager();

	DVDInventory dvdInventory = new DVDInventory();

	/*
	 * @RequestMapping("/") public String index() { return "index"; }
	 */

	@RequestMapping("/")
	public ModelAndView index(ModelAndView mav) {
		mav.addObject("items",
				Iterables.toList(new VideoCatalog().getProductTypes()));
		mav.setViewName("index");
		return mav;
	}

	@RequestMapping("/catalog")
	public ModelAndView catalog(ModelAndView mav) {
		mav.addObject("items", dvdCatalog.getProductTypes());
		mav.setViewName("catalog");
		return mav;
	}

	@RequestMapping("/detail")
	public ModelAndView catalog(ModelAndView mav,
			@RequestParam("pid") String pid) {
		ProductIdentifier pi = new ProductIdentifier(/* pid */); // TODO
		DVD dvd = dvdCatalog.findProductTypeByProductIdentifier(pi);
		mav.setViewName("detail");
		mav.addObject("dvd", dvd);
		return mav;
	}

	@RequestMapping("/comment")
	public ModelAndView catalog(ModelAndView mav,
			@RequestParam("pid") String pid,
			@RequestParam("comment") String comment,
			@RequestParam("rating") int rating) {

		ProductIdentifier pi = new ProductIdentifier(/* pid */); // TODO
		DVD dvd = dvdCatalog.findProductTypeByProductIdentifier(pi);

		dvd.addComment(new Comment(comment, rating));

		mav.setViewName("detail");
		mav.addObject("dvd", dvd);
		return mav;
	}

	@RequestMapping("/buy")
	public ModelAndView buy(HttpServletRequest request, ModelAndView mav,
			@RequestParam("pid") String pid) {

		Customer customer = customerManager.getUserByToken(request.getSession());

		PersistentOrder PersistentOrder = (PersistentOrder) request.getSession().getAttribute("PersistentOrder");
		
		if (PersistentOrder == null) {
			//PersistentOrder = new PersistentOrder(customer.getIdentifier(), "");
		}

		ProductIdentifier pi = new ProductIdentifier(pid); // TODO
		DVD dvd = dvdCatalog.find(DVD.class, pi);
		DVDInstance dvdi = new DVDInstance(dvd);

		dvdInventory.addProductInstance(dvdi);

		PersistentOrder.addOrderLine(dvdInventory, dvdi.getSerialNumber());

		mav.addObject("items", dvdCatalog.getProductTypes());
		mav.setViewName("catalog");
		return mav;
	}
}