package org.salespointframework.core.calendar;

import javax.persistence.Embeddable;
import org.salespointframework.util.SalespointIdentifier;

/**
 * This is an identifier for calendar entries.
 * <code>CalendarEntryIdentifier</code> can be used as primary key attribute, as
 * it is done in {@link PersistentCalendarEntry}s, or as key for
 * <code>Map</code>-based implementations.
 * 
 * @author Stanley Förster
 * 
 * @see SalespointIdentifier
 * 
 */
@SuppressWarnings("serial")
@Embeddable
public final class CalendarEntryIdentifier extends SalespointIdentifier {

	/**
	 * Creates a new unique identifier for use with {@link CalendarEntry}s
	 * 
	 * @see SalespointIdentifier#SalespointIdentifier()
	 */
	public CalendarEntryIdentifier() {
		super();
	}

	/**
	 * Creates a new identifier for use with {@link CalendarEntry}s that has the
	 * given value.
	 * 
	 * @param calendarEntryIdentifier
	 *            The value of this identifier.
	 * 
	 * @see SalespointIdentifier#SalespointIdentifier(String)
	 */
	@Deprecated
	public CalendarEntryIdentifier(String calendarEntryIdentifier) {
		super(calendarEntryIdentifier);
	}
}
