package org.salespointframework.web.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.salespointframework.core.shop.Shop;
import org.salespointframework.core.users.User;
import org.salespointframework.core.users.UserManager;

public class LoggedInTag extends BodyTagSupport {

	private static final long serialVersionUID = 1L;
	
	private boolean status = true;
	private String usermanagerName;

	public void setStatus(boolean status) {
		this.status = status;
	}

	public void setUserManagerName(String usermanagerName) {
		this.usermanagerName = usermanagerName;
	}

	@Override
	public int doStartTag() throws JspException {

		@SuppressWarnings("unchecked")
		UserManager<User> usermanager = (UserManager<User>) Shop.INSTANCE
				.getUserManager(usermanagerName);
		User user = usermanager.getUserByToken(pageContext.getSession());

		if (status) {
			return user == null ? SKIP_BODY : EVAL_BODY_INCLUDE;
		} else {
			return user == null ? EVAL_BODY_INCLUDE : SKIP_BODY;
		}
	}

}
