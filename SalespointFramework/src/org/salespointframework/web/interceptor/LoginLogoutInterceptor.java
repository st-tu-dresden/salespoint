package org.salespointframework.web.interceptor;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.salespointframework.core.shop.Shop;
import org.salespointframework.core.user.Capability;
import org.salespointframework.core.user.PersistentUser;
import org.salespointframework.core.user.PersistentUserManager;
import org.salespointframework.core.user.TransientUser;
import org.salespointframework.core.user.TransientUserManager;
import org.salespointframework.core.user.User;
import org.salespointframework.core.user.UserIdentifier;
import org.salespointframework.core.user.UserManager;

import org.salespointframework.web.annotation.Login;
import org.salespointframework.web.annotation.Logout;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoginLogoutInterceptor extends HandlerInterceptorAdapter {
	private Logger log = Logger.getLogger(this.getClass().getName());

	@SuppressWarnings("unchecked")
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

		if (handler instanceof HandlerMethod) 
		{
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			
			Login login = handlerMethod.getMethodAnnotation(Login.class);
			if(login != null) {
				return handleLogin(login, request, response, handlerMethod);
			}
				
			Logout logout = handlerMethod.getMethodAnnotation(Logout.class);
			if(logout != null) {
				return handleLogout(logout, request, response, handlerMethod);
			}
		}
		return true;
	}
	
	private boolean handleLogout(Logout login, HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) 
	{
		Shop.INSTANCE.getUserManager().logout(request.getSession());
		return true;
	}

	private boolean handleLogin(Login login, HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) {
		
		final UserIdentifier userIdentifier = new UserIdentifier(login.userIdentifier());
		final String password = login.password();
		final String success = login.success();
		final String failure = login.failure();
		
		User user = null;

		// TODO nullcheck
		UserManager<?> usermanager = Shop.INSTANCE.getUserManager();
		
	
		
		if (usermanager instanceof TransientUserManager) {
			user = ((TransientUserManager) usermanager).get(TransientUser.class, userIdentifier);
		}

		if (usermanager instanceof PersistentUserManager) {
			user = ((PersistentUserManager) usermanager).get(PersistentUser.class, userIdentifier);
		}

		if (!(usermanager instanceof TransientUserManager || usermanager instanceof PersistentUserManager)) {
			user = ((UserManager<User>) usermanager).get(User.class, userIdentifier );
		}
		
		if(user == null) {
			//TODO failure
		} else {
			if(user.verifyPassword(password)) {
				HttpSession session = request.getSession();
				if (usermanager instanceof TransientUserManager) {
					((TransientUserManager) usermanager).login((TransientUser)user, session);
					//response.sendRedirect(arg0)
					return true;
				}

				if (usermanager instanceof PersistentUserManager) {
					((PersistentUserManager) usermanager).login((PersistentUser)user, session);
				}

				if (!(usermanager instanceof TransientUserManager || usermanager instanceof PersistentUserManager)) {
					((UserManager<User>)usermanager).login(user, session);
				}
			}
		}
		
		return false;
	}
}