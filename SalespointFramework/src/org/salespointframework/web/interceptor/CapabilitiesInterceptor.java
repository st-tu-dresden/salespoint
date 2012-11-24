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
import org.salespointframework.core.user.UserManager;
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

			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Capabilities capabilitiesOnMethod = handlerMethod.getMethodAnnotation(Capabilities.class);
			Capabilities capabilitiesOnClass = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(),Capabilities.class);

			Set<String> capSet = new HashSet<String>();
			if (capabilitiesOnMethod != null) {
				capSet.addAll(Arrays.asList(capabilitiesOnMethod.value()));
			}
			if (capabilitiesOnClass != null) {
				capSet.addAll(Arrays.asList(capabilitiesOnClass.value()));
			}

			if (capSet.size() == 0) {
				return true;
			}

			UserManager<?> usermanager = Shop.INSTANCE.getUserManager();

			if (usermanager == null) {
				throw new NullPointerException(
						"Shop.INSTANCE.getUserManager() returned null");
			}

			User user = null;
			HttpSession session = request.getSession();

			if (usermanager instanceof TransientUserManager) {
				user = ((TransientUserManager) usermanager).getUserByToken(TransientUser.class, session);
			}

			if (usermanager instanceof PersistentUserManager) {
				user = ((PersistentUserManager) usermanager).getUserByToken(PersistentUser.class, session);
			}

			if (!(usermanager instanceof TransientUserManager || usermanager instanceof PersistentUserManager)) {
				user = ((UserManager<User>) usermanager).getUserByToken(User.class, session);
			}

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
					if(!capabilitiesOnMethod.viewName().equals("")) {
						response.sendRedirect(capabilitiesOnMethod.viewName());
						return true;
					}
				}
				if(capabilitiesOnClass != null) {
					if(!capabilitiesOnClass.viewName().equals("")) {
						response.sendRedirect(capabilitiesOnClass.viewName());
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
