package org.salespointframework.web.spring.propertyeditors;

import java.beans.PropertyEditorSupport;

import org.salespointframework.core.calendar.CalendarEntryIdentifier;

/**
 * 
 * @author Paul Henke
 * 
 */
@Deprecated
public class CalendarEntryIdentifierEditor extends PropertyEditorSupport
{
	@Override
	public void setAsText(String text)
	{
		@SuppressWarnings("deprecation")
		CalendarEntryIdentifier calendarEntryIdentifier = new CalendarEntryIdentifier(text);
		setValue(calendarEntryIdentifier);
	}
}