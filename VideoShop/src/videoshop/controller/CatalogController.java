package videoshop.controller;

import java.util.Locale;

import org.salespointframework.core.inventory.InventoryItem;
import org.salespointframework.core.inventory.PersistentInventory;
import org.salespointframework.core.inventory.PersistentInventoryItem;
import org.salespointframework.core.product.ProductIdentifier;
import org.salespointframework.core.quantity.Quantity;
import org.salespointframework.core.quantity.Units;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import videoshop.model.Comment;
import videoshop.model.Disc;
import videoshop.model.VideoCatalog;

@Controller
public class CatalogController {
	
	@Autowired
	private VideoCatalog videoCatalog;
	@Autowired
	private PersistentInventory inventory;
	@Autowired
	private MessageSource messageSource;
	
	@RequestMapping("/dvdCatalog")
	public String dvdCatalog(ModelMap modelMap, Locale locale) 
	{
		modelMap.addAttribute("catalog", videoCatalog.findDvds());
		String title = messageSource.getMessage("catalog.dvd.title", null , locale);
		modelMap.addAttribute("title", title);
		return "catalog";
	}

	@RequestMapping("/blurayCatalog")
	public String blurayCatalog(ModelMap modelMap, Locale locale) 
	{
		modelMap.addAttribute("catalog", videoCatalog.findBluRays());
		String title = messageSource.getMessage("catalog.bluray.title", null , locale);
		modelMap.addAttribute("title", title);
		return "catalog";
	}
	
	@RequestMapping("/detail/{pid}")
	public String detail(ModelMap modelMap, @PathVariable("pid") ProductIdentifier pid) 
	{
		Disc disc = videoCatalog.getDisc(pid);
		modelMap.addAttribute("disc", disc);
		
		InventoryItem item = inventory.getByProductIdentifier(PersistentInventoryItem.class, pid);
		Quantity quantity = item == null ? Units.ZERO : item.getQuantity();
		modelMap.addAttribute("quantity", quantity);
		
		return "detail";
	}

	@RequestMapping("/comment")
	public String comment(@RequestParam("pid") ProductIdentifier pid, @RequestParam("comment") String comment, @RequestParam("rating") int rating ) 
	{
		Disc disc = videoCatalog.getDisc(pid);
		disc.addComment(new Comment(comment, rating));
		videoCatalog.update(disc);
		return "redirect:detail";
	}
}
