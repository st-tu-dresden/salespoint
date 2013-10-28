package org.salespointframework.web.spring.support;

import org.salespointframework.core.useraccount.UserAccount;
import org.salespointframework.core.useraccount.UserAccountIdentifier;
import org.salespointframework.core.useraccount.UserAccountManager;
import org.salespointframework.web.annotation.LoggedIn;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 
 * @author Paul Henke
 *
 */
public class LoggedInUserAccountArgumentResolver implements HandlerMethodArgumentResolver {

	private UserAccountManager userAccountManager;
	
	public LoggedInUserAccountArgumentResolver(UserAccountManager userAccountManager) {
		this.userAccountManager = userAccountManager;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		UserAccountIdentifier userAccountIdentifier = new UserAccountIdentifier(name);
		
		return userAccountManager.get(userAccountIdentifier);
	
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(LoggedIn.class) && UserAccount.class.isAssignableFrom(parameter.getParameterType());
	}

}
