package org.salespointframework.web.spring.interceptor;

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
import org.salespointframework.core.user.UserManager;
import org.salespointframework.web.WebLoginLogoutManager;
import org.salespointframework.web.annotation.Capabilities;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class CapabilitiesInterceptor extends HandlerInterceptorAdapter {

	private Logger log = Logger.getLogger(this.getClass().getName());

	@SuppressWarnings("unchecked")
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

		if (handler instanceof HandlerMethod) {

			final HandlerMethod handlerMethod = (HandlerMethod) handler;
			final Capabilities capabilitiesOnMethod = handlerMethod.getMethodAnnotation(Capabilities.class);
			final Capabilities capabilitiesOnClass = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(),Capabilities.class);
			//final String redirect = !capabilitiesOnMethod.redirect().equals("") ? capabilitiesOnMethod.redirect() : capabilitiesOnClass.redirect();

			final Set<String> capSet = new HashSet<String>();
			if (capabilitiesOnMethod != null) {
				capSet.addAll(Arrays.asList(capabilitiesOnMethod.value()));
			}
			if (capabilitiesOnClass != null) {
				capSet.addAll(Arrays.asList(capabilitiesOnClass.value()));
			}

			if (capSet.size() == 0) {
				return true;
			}
			
			User user = WebLoginLogoutManager.INSTANCE.getUser(request.getSession());

			/*
			final UserManager<?> usermanager = Shop.INSTANCE.getUserManager();

			if (usermanager == null) {
				throw new NullPointerException("Shop.INSTANCE.getUserManager() returned null");
			}

			final HttpSession session = request.getSession();
			
			final User user = usermanager.getUserByToken(session);
			*/

			boolean hasCapability = false;

			if (user != null) {
				hasCapability = true;
				for (String cap : capSet) {
					if (!user.hasCapability(new Capability(cap))) {
						hasCapability = false;
						break;
					}
				}
			}

			if (!hasCapability) {
				if(capabilitiesOnMethod != null) {
					if(!capabilitiesOnMethod.redirect().equals("")) {
						response.sendRedirect(capabilitiesOnMethod.redirect());
						return true;
					}
				}
				if(capabilitiesOnClass != null) {
					if(!capabilitiesOnClass.redirect().equals("")) {
						response.sendRedirect(capabilitiesOnClass.redirect());
						return true;
					}
				}
				
				if(capabilitiesOnMethod != null) {
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
					return true;
				}
				
				if(capabilitiesOnClass != null) {
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
					return true;
				}
			}
		}
		return true;
	}
}
