package org.salespointframework.web.spring.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.salespointframework.core.user.User;
import org.salespointframework.web.WebAuthenticationManager;
import org.salespointframework.web.annotation.LoggedInUser;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 
 * @author Paul Henke
 *
 */
public class LoggedInUserInterceptor extends HandlerInterceptorAdapter {

	@Override
	public void postHandle(HttpServletRequest request,HttpServletResponse response, Object handler,	ModelAndView modelAndView) {

		if (handler instanceof HandlerMethod) {
			
			final HandlerMethod handlerMethod = (HandlerMethod) handler;
			
			final LoggedInUser loggedInUser = handlerMethod.getMethodAnnotation(LoggedInUser.class);

			if(loggedInUser != null) {
				///final Class<? extends User> clazz = loggedInUser.value();
				final String name = loggedInUser.value();
				
				final HttpSession session = request.getSession();
	
				final User user = WebAuthenticationManager.INSTANCE.getUser(session);
				
				modelAndView.addObject(name, user);
			}
		}
	}
}
