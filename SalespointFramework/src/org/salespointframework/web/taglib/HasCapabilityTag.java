package org.salespointframework.web.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.salespointframework.core.shop.Shop;
import org.salespointframework.core.user.User;
import org.salespointframework.core.user.UserCapability;
import org.salespointframework.core.user.UserManager;

/**
 * 
 * @author Lars Kreisz
 * @author Uwe Schmidt
 * @author Paul Henke
 */
@SuppressWarnings("serial")
public class HasCapabilityTag extends BodyTagSupport
{
	private String capabilityName;
	private boolean test = true;

	public void setCapabilityName(String capabilityName)
	{
		this.capabilityName = capabilityName;
	}
	
	/**
	 * The test condition that determines whether or not the body content should be processed.
	 * @param test a boolean condition
	 */
	public void setTest(boolean test)
	{
		this.test = test;
	}


	@Override
	public int doStartTag() throws JspException
	{
		@SuppressWarnings("unchecked")
		UserManager<User> usermanager = (UserManager<User>) Shop.INSTANCE.getUserManager();
		
		if(usermanager == null) {
			throw new NullPointerException("Shop.INSTANCE.getUserManager() returned null");
		}
		
		User user = usermanager.getUserByToken(User.class, pageContext.getSession());
		UserCapability capability = new UserCapability(capabilityName);
		
			
		if (user != null)
		{
			boolean hasCapability = user.hasCapability(capability);
			
			if(hasCapability && test) {
				return EVAL_BODY_INCLUDE;
			} 
			
			if(!hasCapability && !test) {
				return EVAL_BODY_INCLUDE;
			}
		} else {
			if(!test) {
				return EVAL_BODY_INCLUDE;
			}
		}
		
		return SKIP_BODY;
	}
}
 