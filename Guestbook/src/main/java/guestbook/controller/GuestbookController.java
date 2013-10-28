package guestbook.controller;

import guestbook.model.Guestbook;
import guestbook.model.GuestbookEntry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

// ‎(｡◕‿◕｡)
// Schon den Kommentar im WelcomeController gelesen?
// Wie hier zu sehen ist, kann man die @RequestMapping auch an einen Controller hängen.


@Controller
@RequestMapping("/guestbook")
public class GuestbookController {

	private final Guestbook guestbook;
	
	// ‎(｡◕‿◕｡)
	// Via @Autowired wird automatisch eine Instanz des Guestbooks in den Controller gegegeben.
	@Autowired
	public GuestbookController(Guestbook guestbook) {
		this.guestbook = guestbook;
	}

	@RequestMapping("/")
	public String guestBook() {
		return "guestbook";
	}

	// ‎(｡◕‿◕｡)
	// In der Html-Form wurden 2 Inputfelder mit Namen versehen, auf den Inhalt der Felder kann mit @RequestParam zugegriffen werden.
	// Dabei werden nicht nur Strings, sondern auch andere Typen, wie z.B. Integer (siehe removeEntry), unterstützt.
	
	// Auf Validierung, z.B. leerer Name oder Text wurde an der Stelle verzichtet. 
	// Spring bietet hierfür aber auch Support, sehen wir im Videoshop.
	@RequestMapping(value = "/addEntry", method = RequestMethod.POST)
	public String addEntry(
			@RequestParam("name") String name, 
			@RequestParam("text") String text) {
		
		guestbook.addEntry(name, text);
		return "redirect:/guestbook/";
	}

	@RequestMapping(value = "/removeEntry", method = RequestMethod.POST)
	public String removeEntry(@RequestParam("id") int id) {
		guestbook.removeEntry(id);
		return "redirect:/guestbook/";
	}

	// ‎(｡◕‿◕｡)
	// @ModelAttribute sorgt dafür, dass bei _jedem_ Controlleraufruf die Variable guestbookEntries im Html genutzt werden kann
	// Der Wert wird in eine ModelMap abgelegt und ist damit aus dem View abrufbar.
	@ModelAttribute("guestbookEntries")
	private Iterable<GuestbookEntry> getEntries() {
		return guestbook.getEntries();
	}
	
	// ‎(｡◕‿◕｡)
	// Benötigt man einen Wert nicht bei jedem Aufruf sondern nur bei einem ganz bestimmten 
	// oder ist der Wert von anderen Requestparameter abhängig, so ist es sinnvoll die ModelMap selber zu füllen 
	// Dazu reicht es diese in die Parameterliste aufzunehmen, Spring kümmert sich darum diese zu übergeben.
	// Btw, es sind noch viel mehr Parameter möglich, welche von Spring übergeben werden können, siehe:
	// http://docs.spring.io/spring/docs/3.2.x/javadoc-api/org/springframework/web/bind/annotation/RequestMapping.html
	public String dummy(ModelMap modelMap) {
		modelMap.addAttribute("variable", 1337);
		return "guestbook";
	}
}
