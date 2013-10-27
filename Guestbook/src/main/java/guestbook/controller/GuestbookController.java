package guestbook.controller;

import guestbook.model.Guestbook;
import guestbook.model.GuestbookEntry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/guestbook")
public class GuestbookController {

	@Autowired
	Guestbook guestbook;

	@RequestMapping("/")
	public String guestBook() {
		return "guestbook";
	}

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

	@ModelAttribute("guestbookEntries")
	private Iterable<GuestbookEntry> getEntries() {
		return guestbook.getEntries();
	}
}
