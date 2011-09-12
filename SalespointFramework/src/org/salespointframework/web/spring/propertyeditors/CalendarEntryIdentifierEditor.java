package org.salespointframework.web.spring.propertyeditors;

import java.beans.PropertyEditorSupport;

import org.salespointframework.core.calendar.CalendarEntryIdentifier;

/**
 * 
 * @author Paul Henke
 * 
 */
public class CalendarEntryIdentifierEditor extends PropertyEditorSupport {
	@Override
	public void setAsText(String text) {
		CalendarEntryIdentifier serialNumber = new CalendarEntryIdentifier(text);
		setValue(serialNumber);
	}
}