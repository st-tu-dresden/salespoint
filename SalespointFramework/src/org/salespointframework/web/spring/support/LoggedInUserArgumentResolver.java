package org.salespointframework.web.spring.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.salespointframework.core.user.User;
import org.salespointframework.web.WebAuthenticationManager;
import org.salespointframework.web.annotation.LoggedInUser;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 
 * @author Paul Henke
 *
 */
public class LoggedInUserArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		final HttpSession session = webRequest.getNativeRequest(HttpServletRequest.class).getSession();
		final Class<?> clazz = parameter.getParameterType();
		final User user = WebAuthenticationManager.INSTANCE.getUser(session);
		return clazz.cast(user);
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(LoggedInUser.class) && User.class.isAssignableFrom(parameter.getParameterType());
	}

}
