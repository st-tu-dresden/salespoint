package dvdshop.controller;

import org.salespointframework.core.inventory.PersistentInventory;
import org.salespointframework.core.product.ProductIdentifier;
import org.salespointframework.util.Iterables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import dvdshop.model.Comment;
import dvdshop.model.Disc;
import dvdshop.model.VideoCatalog;


@Controller
public class CatalogController {
	
	@Autowired
	VideoCatalog videoCatalog;

	@RequestMapping("/dvdCatalog")
	public ModelAndView dvdCatalog(ModelAndView mav) {
		mav.addObject("items", Iterables.asList(videoCatalog.findDvds()));
		mav.setViewName("dvdCatalog");
		return mav;
	}

	@RequestMapping("/blurayCatalog")
	public ModelAndView blurayCatalog(ModelAndView mav) {
		mav.addObject("items", Iterables.asList(videoCatalog.findBlueRays()));
		mav.setViewName("blurayCatalog");
		return mav;
	}

	
	@RequestMapping("/detail")
	public ModelAndView detail(ModelAndView mav, @RequestParam("pid") ProductIdentifier pid) {
		
		Disc disc = videoCatalog.getDisc(pid);
		
		mav.addObject("disc", disc);
		mav.addObject("comments", disc.getComments());
		mav.addObject("count", new PersistentInventory().count(disc.getIdentifier()));
		
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
		
		mav.setViewName("detail");
		
		return mav;
	}
}
