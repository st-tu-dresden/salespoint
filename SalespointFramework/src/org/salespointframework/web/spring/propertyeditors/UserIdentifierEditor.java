package org.salespointframework.web.spring.propertyeditors;

import java.beans.PropertyEditorSupport;

import org.salespointframework.core.users.UserIdentifier;

/**
 * 
 * @author Paul Henke
 * 
 */
public class UserIdentifierEditor extends PropertyEditorSupport {
	@Override
	public void setAsText(String text) {
		UserIdentifier userIdentifier = new UserIdentifier(text);
		setValue(userIdentifier);
	}
}