package org.salespointframework.useraccount.web;

import org.salespointframework.useraccount.AuthenticationManager;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * {@link HandlerMethodArgumentResolver} to inject the UserAccount of the currently logged in user int Spring MVC
 * controller method parameters annotated with {@link LoggedIn}.
 * 
 * @author Paul Henke
 * @author Oliver Gierke
 */
@Component
class LoggedInUserAccountArgumentResolver implements HandlerMethodArgumentResolver {

	private final AuthenticationManager authenticationManager;

	/**
	 * Creates a new {@link LoggedInUserAccountArgumentResolver} using the given {@link AuthenticationManager}.
	 * 
	 * @param authenticationManager must not be {@literal null}.
	 */
	@Autowired
	public LoggedInUserAccountArgumentResolver(AuthenticationManager authenticationManager) {
		
		Assert.notNull(authenticationManager, "AuthenticationManager must not be null!");
		this.authenticationManager = authenticationManager;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.web.method.support.HandlerMethodArgumentResolver#resolveArgument(org.springframework.core.MethodParameter, org.springframework.web.method.support.ModelAndViewContainer, org.springframework.web.context.request.NativeWebRequest, org.springframework.web.bind.support.WebDataBinderFactory)
	 */
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		return authenticationManager.getCurrentUser();
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.web.method.support.HandlerMethodArgumentResolver#supportsParameter(org.springframework.core.MethodParameter)
	 */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(LoggedIn.class)
				&& UserAccount.class.isAssignableFrom(parameter.getParameterType());
	}
}
