package org.salespointframework.web.spring.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.salespointframework.core.shop.Shop;
import org.salespointframework.core.user.PersistentUser;
import org.salespointframework.core.user.PersistentUserManager;
import org.salespointframework.core.user.TransientUser;
import org.salespointframework.core.user.TransientUserManager;
import org.salespointframework.core.user.User;
import org.salespointframework.core.user.UserManager;
import org.salespointframework.web.annotation.LoggedInUser;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoggedInUserInterceptor extends HandlerInterceptorAdapter {

	@Override
	public void postHandle(HttpServletRequest request,HttpServletResponse response, Object handler,	ModelAndView modelAndView) {

		if (handler instanceof HandlerMethod) {
			
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			
			LoggedInUser loggedInUser = handlerMethod.getMethodAnnotation(LoggedInUser.class);
			
			final Class<? extends User> clazz = loggedInUser.value();
			final String name = loggedInUser.name();
			
			HttpSession session = request.getSession();

			UserManager<?> usermanager = Shop.INSTANCE.getUserManager();

			if (usermanager == null) {
				throw new NullPointerException("Shop.INSTANCE.getUserManager() returned null");
			}

			User user = usermanager.getUserByToken(session);
			
			modelAndView.addObject(name, clazz.cast(user));
		}
	}
}
