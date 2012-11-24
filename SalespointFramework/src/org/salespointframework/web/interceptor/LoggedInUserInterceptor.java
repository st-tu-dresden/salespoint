package org.salespointframework.web.interceptor;

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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoggedInUserInterceptor extends HandlerInterceptorAdapter {

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
		HttpSession session = request.getSession();
		
		UserManager<?> usermanager = Shop.INSTANCE.getUserManager();
		
		if(usermanager == null) {
			throw new NullPointerException("Shop.INSTANCE.getUserManager() returned null");
		}
		
		User user = null;
		
		if(usermanager instanceof TransientUserManager) {
			user = ((TransientUserManager)usermanager).getUserByToken(TransientUser.class, session);
		}
		
		if(usermanager instanceof PersistentUserManager) {
			user = ((PersistentUserManager)usermanager).getUserByToken(PersistentUser.class, session);
		}
		
		if(!(usermanager instanceof TransientUserManager || usermanager instanceof PersistentUserManager)) {
			user = ((UserManager<User>)usermanager).getUserByToken(User.class, session);
		}
		
		if(user != null) {
			modelAndView.addObject("sp_User", user);
		}
		
	}
}
