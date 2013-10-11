package org.salespointframework.web.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.salespointframework.core.user.Capability;
import org.salespointframework.core.user.User;
import org.salespointframework.web.WebAuthenticationManager;

/**
 * This tag checks if currently authenticated (logged in) {@link User} has any given {@link Capability}.  
 * 
 * @author Lars Kreisz
 * @author Uwe Schmidt
 * @author Paul Henke
 * 
 * @see WebAuthenticationManager
 * 
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
		String[] capList = capabilityName.split(",");

		User user = WebAuthenticationManager.INSTANCE.getUser(pageContext.getSession());
			
		if (user != null)
		{
			boolean hasCapability = false;
			
			for(String capString : capList) {
				if(user.hasCapability(new Capability(capString))) {
					hasCapability = true;
					break;
				}
			}
			
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
 