package videoshop.controller;


import org.salespointframework.core.inventory.Inventory;
import org.salespointframework.core.inventory.InventoryItem;
import org.salespointframework.core.quantity.Quantity;
import org.salespointframework.core.quantity.Units;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import videoshop.model.Comment;
import videoshop.model.Disc;
import videoshop.model.VideoCatalog;

/**
 * 
 * @author Paul Henke
 * @author Oliver Gierke
 */
@Controller
public class CatalogController {
	
	private final VideoCatalog videoCatalog;
	private final Inventory inventory;
	private final MessageSourceAccessor messageSourceAccessor;
	
	/**
	 * @param videoCatalog
	 * @param inventory
	 * @param messageSource
	 */
	@Autowired
	public CatalogController(VideoCatalog videoCatalog, Inventory inventory, MessageSource messageSource) {
		
		this.videoCatalog = videoCatalog;
		this.inventory = inventory;
		this.messageSourceAccessor = new MessageSourceAccessor(messageSource);
	}

	@RequestMapping("/dvdCatalog")
	public String dvdCatalog(ModelMap modelMap) 
	{
		modelMap.addAttribute("catalog", videoCatalog.findDvds());
		String title = messageSourceAccessor.getMessage("catalog.dvd.title");
		modelMap.addAttribute("title", title);
		return "catalog";
	}

	@RequestMapping("/blurayCatalog")
	public String blurayCatalog(ModelMap modelMap) 
	{
		modelMap.addAttribute("catalog", videoCatalog.findBluRays());
		String title = messageSourceAccessor.getMessage("catalog.bluray.title");
		modelMap.addAttribute("title", title);
		return "catalog";
	}
	
	@RequestMapping("/detail/{pid}")
	public String detail(@PathVariable("pid") Disc disc, ModelMap modelMap) 
	{
		modelMap.addAttribute("disc", disc);
		
		InventoryItem item = inventory.getByProductIdentifier(InventoryItem.class, disc.getIdentifier());
		Quantity quantity = item == null ? Units.ZERO : item.getQuantity();
		modelMap.addAttribute("quantity", quantity);
		
		return "detail";
	}

	@RequestMapping(value="/comment", method=RequestMethod.POST)
	public String comment(@RequestParam("pid") Disc disc, @RequestParam("comment") String comment, @RequestParam("rating") int rating ) 
	{
		disc.addComment(new Comment(comment, rating));
		return "redirect:detail/" + disc.getIdentifier();
	}
}
