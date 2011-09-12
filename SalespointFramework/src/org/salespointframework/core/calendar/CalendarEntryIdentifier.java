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
@Embeddable
public final class CalendarEntryIdentifier extends SalespointIdentifier {
    @SuppressWarnings("javadoc")
    private static final long serialVersionUID = -4862317207862935005L;
}
