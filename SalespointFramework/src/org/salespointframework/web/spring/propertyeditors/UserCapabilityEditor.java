package org.salespointframework.web.spring.propertyeditors;

import java.beans.PropertyEditorSupport;

import org.salespointframework.core.user.Capability;

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
		Capability capabiliy = new Capability(text);
		setValue(capabiliy);
	}
}