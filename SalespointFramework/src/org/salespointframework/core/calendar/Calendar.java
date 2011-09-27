package org.salespointframework.core.calendar;

import org.salespointframework.util.ArgumentNullException;

/**
 * A calendar manages a set of calendar entries.
 * 
 * @param <T>
 *            Type of calendar entries (extends
 *            {@link org.salespointframework.core.calendar.CalendarEntry})
 * 
 * @author Stanley Förster
 */
public interface Calendar<T extends CalendarEntry> {

    /**
     * Adds the given entry to the calendar.
     * 
     * @param entry
     *            the entry that is to be added to the calendar.
     * 
     * @throws ArgumentNullException
     *             if <code>entry</code> is <code>null</code>
     */
    void add(T entry);

    /**
     * Checks, whether there exists an {@link CalendarEntry} that has the given
     * {@link CalendarEntryIdentifier} in this calendar or not.
     * 
     * @param calendarEntryIdentifier
     *            The identifier of the entry that should be checked to be
     *            contained in this calendar.
     * @return <code>true</code> if there exists an entry with the given
     *         identifier in this calendar, <code>false</code> otherwise.
     * @throws ArgumentNullException
     *             if <code>calendarEntryIdentifier</code> is <code>null</code>.
     */
    boolean contains(CalendarEntryIdentifier calendarEntryIdentifier);

    /**
     * Returns the calendar entry which has the given id.
     * 
     * @param calendarEntryIdentifier
     *            Id of the requested entry.
     * @return the entry with the given id or <code>null</code> if no entry was
     *         found.
     * @throws ArgumentNullException
     *             if <code>calendarEntryIdentifier</code> is <code>null</code>.
     */
    T find(CalendarEntryIdentifier calendarEntryIdentifier);

    /**
     * Returns all entries that are stored in this calendar.
     * 
     * @return Iterable<T> of all found entries.
     */
    Iterable<T> getAllEntries();

    /**
     * Deletes the entry with the given id from the calendar.
     * 
     * @param calendarEntryIdentifier
     *            Id of the entry that is to be removed from the calendar.
     * @throws ArgumentNullException
     *             if <code>calendarEntryIdentifier</code> is <code>null</code>.
     */
    boolean remove(CalendarEntryIdentifier calendarEntryIdentifier);
}
