package org.salespointframework.web.spring.propertyeditors;

import java.beans.PropertyEditorSupport;

import org.salespointframework.core.accountancy.AccountancyEntryIdentifier;

/**
 * 
 * @author Paul Henke
 * 
 */
public class AccountancyEntryIdentifierEditor extends PropertyEditorSupport
{
	@Override
	public void setAsText(String text)
	{
		AccountancyEntryIdentifier accountancyEntryIdentifier = new AccountancyEntryIdentifier(text);
		setValue(accountancyEntryIdentifier);
	}
}