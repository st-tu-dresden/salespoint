package dvdshop.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.salespointframework.core.user.PersistentUserManager;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import dvdshop.model.Customer;

public class UserInterceptor extends HandlerInterceptorAdapter {

	Logger log = Logger.getLogger(this.getClass().getName());
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mav) throws Exception
	{
		PersistentUserManager userManager = new PersistentUserManager();
		Customer c = userManager.getUserByToken(Customer.class, request.getSession());
	
		if(c != null) {
			mav.addObject("loggedInUser", c);
			log.log(Level.INFO, "UserInterceptor: User " + c + " logged in");
		}
		else {
			log.info("UserInterceptor: no User");
		}
	}
}
