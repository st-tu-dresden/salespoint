package dvdshop.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.salespointframework.core.user.PersistentUserManager;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import dvdshop.model.Customer;

public class AlwaysInterceptor extends HandlerInterceptorAdapter {
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mav) throws Exception
	{
		PersistentUserManager userManager = new PersistentUserManager();
		Customer c = userManager.getUserByToken(Customer.class, request.getSession());
	
		System.out.println("intercepting: " + c);
		
		if(c != null) {
			mav.addObject("loggedInUser", c);
		}
	}
}
