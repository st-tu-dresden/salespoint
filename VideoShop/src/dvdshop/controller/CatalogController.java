package dvdshop.controller;

import org.salespointframework.core.inventory.PersistentInventory;
import org.salespointframework.core.product.ProductIdentifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import dvdshop.model.Comment;
import dvdshop.model.Disc;
import dvdshop.model.VideoCatalog;


@Controller
public class CatalogController {
	
	private final VideoCatalog videoCatalog = new VideoCatalog();
	private final PersistentInventory inventory = new PersistentInventory();

	@RequestMapping("/dvdCatalog")
	public ModelAndView dvdCatalog(ModelAndView mav) {
		mav.addObject("catalog", videoCatalog.findDvds());
		mav.addObject("title", "Dvd Catalog");

		mav.setViewName("catalog");
		return mav;
	}

	@RequestMapping("/blurayCatalog")
	public ModelAndView blurayCatalog(ModelAndView mav) {
		mav.addObject("catalog", videoCatalog.findBluRays());
		mav.addObject("title", "BluRay Catalog");

		mav.setViewName("catalog");
		return mav;
	}
	
	@RequestMapping("/detail/{pid}")
	public ModelAndView detail(ModelAndView mav, @PathVariable("pid") ProductIdentifier pid) {
		Disc disc = videoCatalog.getDisc(pid);
		mav.addObject("disc", disc);
		mav.addObject("count", inventory.count(disc.getIdentifier()));
		
		mav.setViewName("detail");
		return mav;
	}


	@RequestMapping("/comment")
	public ModelAndView comment(ModelAndView mav,
			@RequestParam("pid") ProductIdentifier pid,
			@RequestParam("comment") String comment,
			@RequestParam("rating") int rating ) {

		Disc disc = videoCatalog.get(Disc.class, pid);

		disc.addComment(new Comment(comment, rating));

		videoCatalog.update(disc);
		
		mav.addObject("disc", disc);
		mav.addObject("count", inventory.count(disc.getIdentifier()));
		
		mav.setViewName("detail");
		return mav;
	}
}
