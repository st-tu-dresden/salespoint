package org.salespointframework.core.calendar;

import javax.persistence.Embeddable;
import org.salespointframework.util.SalespointIdentifier;

/**
 * This is an identifier for calendar entries.
 * It is also the primary key of {@link PersistentCalendarEntry}s.
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
	 * Creates a new identifier for use with {@link CalendarEntry}s that has the given value.
	 * 
	 * @param calendarEntryIdentifier The value of this identifier.
	 * 
	 * @see SalespointIdentifier#SalespointIdentifier(String)
	 */
	public CalendarEntryIdentifier(String calendarEntryIdentifier) {
		super(calendarEntryIdentifier);
	}
}
