package guestbook.controller;

import guestbook.model.Guestbook;
import guestbook.model.GuestbookEntry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

//http://blog.springsource.com/2010/01/25/ajax-simplifications-in-spring-3-0/

@Controller
@RequestMapping("/ajaxbook")
public class AjaxGuestbookController {

	@Autowired
	Guestbook guestbook;

	@RequestMapping("/")
	public ModelAndView guestBook(ModelAndView mav)	{
		mav.addObject("guestbookEntries", guestbook.getEntries());
		mav.setViewName("/gb/guestbook_ajax");
		return mav;
	}

	@RequestMapping(value="/addEntry", method=RequestMethod.GET)
	public @ResponseBody GuestbookEntry addEntry(
			  @RequestParam("name") String name,
	          @RequestParam("text") String text) 
	{
		return guestbook.addEntry(name, text);
	}

	
	@RequestMapping(value="/removeEntry", method=RequestMethod.GET)
	public @ResponseBody boolean removeEntry(@RequestParam("id") int id) {
		return guestbook.removeEntry(id);
	}
}
