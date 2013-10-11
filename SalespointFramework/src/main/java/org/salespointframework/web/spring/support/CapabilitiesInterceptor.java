package org.salespointframework.web.spring.support;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.salespointframework.core.user.Capability;
import org.salespointframework.core.user.User;
import org.salespointframework.web.WebAuthenticationManager;
import org.salespointframework.web.annotation.Capabilities;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 
 * @author Paul Henke
 * 
 */
public class CapabilitiesInterceptor extends HandlerInterceptorAdapter {

	//private Logger log = Logger.getLogger(this.getClass().getName());

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException 
	{
		if (handler instanceof HandlerMethod) 
		{
			final HandlerMethod handlerMethod = (HandlerMethod) handler;
			final Capabilities capabilitiesOnMethod = handlerMethod.getMethodAnnotation(Capabilities.class);
			final Capabilities capabilitiesOnClass = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(),Capabilities.class);

			if (capabilitiesOnClass == null && capabilitiesOnMethod == null) {
				return true;
			}

			final User user = WebAuthenticationManager.INSTANCE.getUser(request.getSession());

			if(user != null) {
				if (capabilitiesOnClass != null) {
					for (String capString : capabilitiesOnClass.value()) {
						if (user.hasCapability(new Capability(capString))) {
							return true;
						}
					}
				}
	
				if (capabilitiesOnMethod != null) {
					for (String capString : capabilitiesOnMethod.value()) {
						if (user.hasCapability(new Capability(capString))) {
							return true;
						}
					}
				}
			}

			if (capabilitiesOnMethod != null) {
				if (!capabilitiesOnMethod.redirect().equals("")) {
					response.sendRedirect(capabilitiesOnMethod.redirect());
					return true;
				}
			}

			if (capabilitiesOnClass != null) {
				if (!capabilitiesOnClass.redirect().equals("")) {
					response.sendRedirect(capabilitiesOnClass.redirect());
					return true;
				}
			}

			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
		return true;
	}
}
