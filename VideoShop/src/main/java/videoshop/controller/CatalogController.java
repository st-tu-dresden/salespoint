package videoshop.controller;

import org.salespointframework.core.inventory.Inventory;
import org.salespointframework.core.inventory.InventoryItem;
import org.salespointframework.core.quantity.Quantity;
import org.salespointframework.core.quantity.Units;
import org.salespointframework.core.time.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import videoshop.model.Comment;
import videoshop.model.Disc;
import videoshop.model.VideoCatalog;

@Controller
class CatalogController {

	private final VideoCatalog videoCatalog;
	private final Inventory inventory;
	private final TimeService timeService;

	// (｡◕‿◕｡)
	// Da wir nur ein Catalog.html-Template nutzen, aber dennoch den richtigen Titel auf der Seite haben wollen,
	// nutzen wir den MessageSourceAccessor um an die messsages.properties Werte zu kommen
	private final MessageSourceAccessor messageSourceAccessor;

	@Autowired
	public CatalogController(VideoCatalog videoCatalog, Inventory inventory, TimeService timeService,
			MessageSource messageSource) {

		this.videoCatalog = videoCatalog;
		this.inventory = inventory;
		this.timeService = timeService;
		this.messageSourceAccessor = new MessageSourceAccessor(messageSource);
	}

	@RequestMapping("/dvdCatalog")
	public String dvdCatalog(ModelMap modelMap) {
		modelMap.addAttribute("catalog", videoCatalog.findDvds());
		String title = messageSourceAccessor.getMessage("catalog.dvd.title");
		modelMap.addAttribute("title", title);
		return "catalog";
	}

	@RequestMapping("/blurayCatalog")
	public String blurayCatalog(ModelMap modelMap) {
		modelMap.addAttribute("catalog", videoCatalog.findBluRays());
		String title = messageSourceAccessor.getMessage("catalog.bluray.title");
		modelMap.addAttribute("title", title);
		return "catalog";
	}

	// (｡◕‿◕｡)
	// Befindet sich die angesurfte Url in der Form /foo/5 statt /foo?bar=5 so muss man @PathVariable benutzen
	// Lektüre: http://spring.io/blog/2009/03/08/rest-in-spring-3-mvc/
	@RequestMapping("/detail/{pid}")
	public String detail(@PathVariable("pid") Disc disc, ModelMap modelMap) {
		modelMap.addAttribute("disc", disc);

		InventoryItem item = inventory.getByProductIdentifier(InventoryItem.class, disc.getIdentifier());
		Quantity quantity = item == null ? Units.ZERO : item.getQuantity();
		modelMap.addAttribute("quantity", quantity);

		return "detail";
	}

	// (｡◕‿◕｡)
	// Der Katalog bzw die Datenbank "weiß" nicht, dass die Disc mit einem Kommentar versehen wurde,
	// deswegen wird die update-Methode aufgerufen
	@RequestMapping(value = "/comment", method = RequestMethod.POST)
	public String comment(@RequestParam("pid") Disc disc, @RequestParam("comment") String comment,
			@RequestParam("rating") int rating) {
		disc.addComment(new Comment(comment, rating, timeService.getTime().getDateTime()));
		videoCatalog.update(disc);
		return "redirect:detail/" + disc.getIdentifier();
	}
}
