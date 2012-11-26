package org.salespointframework.web.spring.propertyeditors;

import java.beans.PropertyEditorSupport;

import org.salespointframework.core.accountancy.AccountancyEntryIdentifier;

/**
 * 
 * @author Paul Henke
 * 
 */
@Deprecated
public class AccountancyEntryIdentifierEditor extends PropertyEditorSupport
{
	@Override
	public void setAsText(String text)
	{
		@SuppressWarnings("deprecation")
		AccountancyEntryIdentifier accountancyEntryIdentifier = new AccountancyEntryIdentifier(text);
		setValue(accountancyEntryIdentifier);
	}
}