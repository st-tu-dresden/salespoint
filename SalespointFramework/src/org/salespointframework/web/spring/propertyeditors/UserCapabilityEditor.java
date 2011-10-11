package org.salespointframework.web.spring.propertyeditors;

import java.beans.PropertyEditorSupport;

import org.salespointframework.core.user.UserCapability;

/**
 * 
 * @author Paul Henke
 *
 */
public class UserCapabilityEditor extends PropertyEditorSupport
{
	@Override
	public void setAsText(String text)
	{
		UserCapability capabiliy = new UserCapability(text);
		setValue(capabiliy);
	}
}