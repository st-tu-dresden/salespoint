package org.salespointframework.web.spring.propertyeditors;

import java.beans.PropertyEditorSupport;

import org.salespointframework.core.user.UserIdentifier;

/**
 * 
 * @author Paul Henke
 * 
 */
@Deprecated
public class UserIdentifierEditor extends PropertyEditorSupport
{
	@Override
	public void setAsText(String text)
	{
		UserIdentifier userIdentifier = new UserIdentifier(text);
		setValue(userIdentifier);
	}
}