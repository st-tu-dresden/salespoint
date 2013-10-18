package org.salespointframework.web.spring.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.salespointframework.core.user.User;
import org.salespointframework.web.annotation.LoggedInUser;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 
 * @author Paul Henke
 *
 */
@Deprecated
public class LoggedInUserInterceptor extends HandlerInterceptorAdapter {

	@Override
	public void postHandle(HttpServletRequest request,HttpServletResponse response, Object handler,	ModelAndView modelAndView) 
	{
		if (handler instanceof HandlerMethod) 
		{
			final HandlerMethod handlerMethod = (HandlerMethod) handler;
			LoggedInUser loggedInUser = handlerMethod.getMethodAnnotation(LoggedInUser.class);
			if(loggedInUser == null) {
				loggedInUser = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(),LoggedInUser.class);
			}

			if(loggedInUser != null) 
			{
				final String name = loggedInUser.value();
				final HttpSession session = request.getSession();
				final User user = null; //WebAuthenticationManager.INSTANCE.getUser(session);
				
				modelAndView.addObject(name, user);
			}
		}
	}
}
