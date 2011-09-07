package org.salespointframework.web.spring.interceptors;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.salespointframework.core.shop.Shop;
import org.salespointframework.core.users.User;
import org.salespointframework.core.users.UserIdentifier;
import org.salespointframework.core.users.UserManager;
import org.salespointframework.web.WebConstants;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoginInterceptor extends HandlerInterceptorAdapter {
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler, ModelAndView mav) throws Exception {
		
		Map<String,String[]> params = request.getParameterMap();
	
		if(!params.containsKey(WebConstants.LOGIN_PARAM)) {
			return;
		}
		
		String identifier = params.get(WebConstants.LOGIN_PARAM_IDENTIFIER)[0];
		String password = params.get(WebConstants.LOGIN_PARAM_PASSWORD)[0];
		String usermanagerName = params.get(WebConstants.LOGIN_PARAM_USERMANAGER)[0];
		
		UserIdentifier userIdentifier = new UserIdentifier(identifier);		
		@SuppressWarnings("unchecked")
		UserManager<User> usermanager = (UserManager<User>) Shop.INSTANCE.getUserManager(usermanagerName);
		
		User user = usermanager.getUserByIdentifier(User.class, userIdentifier);
		
		if(user != null) {
			if(user.verifyPassword(password)) {
				usermanager.logOn(user, request.getSession());
			}
		}
	}
}
