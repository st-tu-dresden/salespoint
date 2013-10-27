package guestbook.controller;

import guestbook.model.Guestbook;
import guestbook.model.GuestbookEntry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

// http://blog.springsource.com/2010/01/25/ajax-simplifications-in-spring-3-0/

@Controller
@RequestMapping("/ajaxbook")
public class AjaxGuestbookController {

	@Autowired
	Guestbook guestbook;

	@RequestMapping("/")
	public String guestBook() {
		return "guestbook_ajax";
	}

	@RequestMapping(value = "/addEntry", method = RequestMethod.POST)
	public @ResponseBody
	GuestbookEntry addEntry(
			@RequestParam("name") String name,
			@RequestParam("text") String text) {
		return guestbook.addEntry(name, text);
	}

	@RequestMapping(value = "/removeEntry", method = RequestMethod.POST)
	public @ResponseBody
	boolean removeEntry(@RequestParam("id") int id) {
		return guestbook.removeEntry(id);
	}

	@ModelAttribute("guestbookEntries")
	private Iterable<GuestbookEntry> getEntries() {
		return guestbook.getEntries();
	}
}
