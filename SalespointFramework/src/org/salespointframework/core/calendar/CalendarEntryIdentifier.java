package org.salespointframework.core.calendar;

import javax.persistence.Embeddable;
import org.salespointframework.util.SalespointIdentifier;

/**
 * This is an identifier for calendar entries.
 * 
 * @author Stanley Förster
 * 
 * @see SalespointIdentifier
 *
 */
@SuppressWarnings("serial")
@Embeddable
public final class CalendarEntryIdentifier extends SalespointIdentifier {
	public CalendarEntryIdentifier() {
		super();
	}
	public CalendarEntryIdentifier(String calendarEntryIdentifier) {
		super(calendarEntryIdentifier);
	}
}
