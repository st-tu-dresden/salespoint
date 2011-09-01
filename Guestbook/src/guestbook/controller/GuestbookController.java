package guestbook.controller;

import guestbook.model.Guestbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/guestbook")
public class GuestbookController {
	
	@Autowired
	Guestbook guestbook;
	
	String lastName = "";
	
	@RequestMapping("/")
	public ModelAndView guestBook(ModelAndView mav)
	{
		mav.setViewName("/gb/guestbook");
		return addStuff(mav);
	}
	
	@RequestMapping(value="/addEntry",method=RequestMethod.POST)
	public ModelAndView addEntry(          
		  @RequestParam("name") String name,
          @RequestParam("text") String text,
          ModelAndView mav) 
	{
		guestbook.addEntry(name, text);
		lastName = name;
		
		mav.setViewName("redirect:/guestbook/");
		return addStuff(mav);
	}
	
	@RequestMapping(value="/removeEntry",method=RequestMethod.GET)
	public ModelAndView removeEntry(	
			  @RequestParam("id") int id,
	          ModelAndView mav) 
	{
		guestbook.removeEntry(id);
		mav.setViewName("redirect:/guestbook/");
		return addStuff(mav);
	}
	
	private ModelAndView addStuff(ModelAndView mav) {
		mav.addObject("guestbookEntries", guestbook.getEntries());
		mav.addObject("lastName", lastName);
		return  mav;
	}
}
