package org.salespointframework.web.spring.propertyeditors;

import java.beans.PropertyEditorSupport;

import org.salespointframework.core.quantity.Units;

/**
 * 
 * @author Paul Henke
 * 
 */
public class UnitsEditor extends PropertyEditorSupport
{
	@Override
	public void setAsText(String text)
	{
		// JAva fails hard ... again
		try {
			long l = Long.parseLong(text);
			Units units = Units.of(l);
			setValue(units);
			
		} catch (NumberFormatException e) {
			setValue(null);
		}
	}
}
