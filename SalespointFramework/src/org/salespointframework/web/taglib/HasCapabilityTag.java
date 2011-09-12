package org.salespointframework.web.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.salespointframework.core.shop.Shop;
import org.salespointframework.core.users.User;
import org.salespointframework.core.users.UserCapability;
import org.salespointframework.core.users.UserManager;

/**
 * 
 * @author Lars Kreisz
 * @author Uwe Schmidt
 * @author Paul Henke
 */
public class HasCapabilityTag extends BodyTagSupport {
	private static final long serialVersionUID = 1L;

	String usermanagerName;
	String capabilityName;

	public void setUserManagerName(String usermanagerName) {
		this.usermanagerName = usermanagerName;
	}

	public void setCapabilityName(String capabilityName) {
		this.capabilityName = capabilityName;
	}

	@Override
	public int doStartTag() throws JspException {

		UserManager<User> usermanager = Shop.INSTANCE.getUserManager();
		User user = usermanager.getUserByToken(User.class, pageContext.getSession());
		UserCapability capability = new UserCapability(capabilityName);

		if (user != null) {
			if (usermanager.hasCapability(user, capability)) {
				return EVAL_BODY_INCLUDE;
			}
		}
		return SKIP_BODY;
	}
}
