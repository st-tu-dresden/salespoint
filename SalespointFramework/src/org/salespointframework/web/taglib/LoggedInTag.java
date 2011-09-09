package org.salespointframework.web.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.salespointframework.core.shop.Shop;
import org.salespointframework.core.users.User;
import org.salespointframework.core.users.UserManager;

public class LoggedInTag extends BodyTagSupport {

	private static final long serialVersionUID = 1L;
	
	private boolean status = true;

	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public int doStartTag() throws JspException {

		UserManager<User> usermanager = Shop.INSTANCE.getUserManager();
		User user = usermanager.getUserByToken(User.class, pageContext.getSession());

		if (status) {
			return user == null ? SKIP_BODY : EVAL_BODY_INCLUDE;
		} else {
			return user == null ? EVAL_BODY_INCLUDE : SKIP_BODY;
		}
	}

}
