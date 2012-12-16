package guestbook.controller;

import guestbook.model.Guestbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/guestbook")
public class GuestbookController {
	
	@Autowired
	Guestbook guestbook;
	
	String lastName = "";
	
	@RequestMapping("/")
	public String guestBook(ModelMap mav) {
		return "/gb/guestbook";
	}
	
	@RequestMapping(value="/addEntry", method=RequestMethod.POST)
	public String addEntry(@RequestParam("name") String name, @RequestParam("text") String text)
	{
		guestbook.addEntry(name, text);
		lastName = name;
		
		return "redirect:/guestbook/";
	}
	
	@RequestMapping(value="/removeEntry", method=RequestMethod.GET)
	public String removeEntry(@RequestParam("id") int id)
	{
		guestbook.removeEntry(id);
		return "redirect:/guestbook/";
	}
	
	@ModelAttribute
	private void addStuff(ModelMap modelMap) {
		modelMap.addAttribute("guestbookEntries", guestbook.getEntries());
		modelMap.addAttribute("lastName", lastName);
	}
}
