package org.salespointframework.web.spring.support;


import org.salespointframework.core.user.User;
import org.salespointframework.core.user.UserIdentifier;
import org.salespointframework.core.user.UserManager;
import org.salespointframework.web.annotation.LoggedInUser;
import org.springframework.beans.factory.annotation.Autowired;
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
public class LoggedInUserArgumentResolver implements HandlerMethodArgumentResolver {

	@Autowired
	private UserManager userManager;
	
	// TODO comments entfernen
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		UserIdentifier userIdentifier = new UserIdentifier(name);
		
		return userManager.get(User.class, userIdentifier);
	
		
		//final HttpSession session = webRequest.getNativeRequest(HttpServletRequest.class).getSession();
		//final Class<?> clazz = parameter.getParameterType();
		//final User user = WebAuthenticationManager.INSTANCE.getUser(session);
		//return clazz.cast(user);
		//return user;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(LoggedInUser.class) && User.class.isAssignableFrom(parameter.getParameterType());
	}

}
