package org.salespointframework.web.spring.interceptor;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
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

import org.salespointframework.web.WebLoginLogoutManager;
import org.salespointframework.web.annotation.Login;
import org.salespointframework.web.annotation.Logout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Deprecated
public class LoginLogoutInterceptor extends HandlerInterceptorAdapter {
	private Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private Locale locale;
	
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
		/*
		UserManager<?> usermanager = Shop.INSTANCE.getUserManager();
		if(usermanager == null) {
			throw new NullPointerException("Shop.INSTANCE.getUserManager() returned null");
		}
		usermanager.logout(request.getSession());
		*/
		
		WebLoginLogoutManager.INSTANCE.logout(request.getSession());
		
		return true;
	}

	@SuppressWarnings("unchecked")
	private boolean handleLogin(Login login, HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) throws IOException {
		
		
		final UserIdentifier userIdentifier = new UserIdentifier(login.userIdentifier());
		final String password = login.password();
		final String redirect = login.redirect();
		final String errorMessage = login.errorMessage();
		
		User user = null;

		final UserManager<?> usermanager = Shop.INSTANCE.getUserManager();
		
		if(usermanager == null) {
			throw new NullPointerException("Shop.INSTANCE.getUserManager() returned null");
		}
	
		
		if (usermanager instanceof TransientUserManager) {
			user = ((TransientUserManager) usermanager).get(TransientUser.class, userIdentifier);
		}

		if (usermanager instanceof PersistentUserManager) {
			user = ((PersistentUserManager) usermanager).get(PersistentUser.class, userIdentifier);
		}

		if (!(usermanager instanceof TransientUserManager || usermanager instanceof PersistentUserManager)) {
			user = ((UserManager<User>) usermanager).get(User.class, userIdentifier );
		}
		
		boolean hasError = false;
		
		if(user == null) {
			hasError = true;
		} else {
			if(user.verifyPassword(password)) {
				HttpSession session = request.getSession();
				if (usermanager instanceof TransientUserManager) {
					((TransientUserManager) usermanager).login((TransientUser)user, session);
				}

				if (usermanager instanceof PersistentUserManager) {
					((PersistentUserManager) usermanager).login((PersistentUser)user, session);
				}

				if (!(usermanager instanceof TransientUserManager || usermanager instanceof PersistentUserManager)) {
					((UserManager<User>)usermanager).login(user, session);
				}
				
				response.sendRedirect(redirect);
				return true;				
				
			} else {
				hasError = true;
			}
		}
		
		if(hasError) {
			String msg = messageSource.getMessage(errorMessage, null, errorMessage , locale);
		//	request.get
			
		}
		
		return true;
	}
}