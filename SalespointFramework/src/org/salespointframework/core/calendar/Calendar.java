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
	 * Returns the calendar entry which has the given id and is of type
	 * <code>clazz</code> or a sub-type thereof.
	 * 
	 * @param clazz
	 *            Class object corresponding to the type of the entries to be
	 *            returned, has to implement <code>CalendarEntry</code>
	 * @param <E>
	 *            common super type of all entries returned
	 * @param calendarEntryIdentifier
	 *            Id of the requested entry.
	 * @return the entry with the given id or <code>null</code> if no entry was
	 *         found.
	 * @throws ArgumentNullException
	 *             if <code>calendarEntryIdentifier</code> is <code>null</code>.
	 */
	<E extends T> E get(Class<E> clazz,
			CalendarEntryIdentifier calendarEntryIdentifier);

	/**
	 * Returns all entries that are stored in this calendar.
	 * 
	 * @param <E>
	 *            common super type of all entries returned
	 * @param clazz
	 *            Class object corresponding to the type of the entries to be
	 *            returned, has to implement <code>CalendarEntry</code>
	 * 
	 * @return Iterable<T> of all found entries.
	 */
	<E extends T> Iterable<E> find(Class<E> clazz);

	/**
	 * Deletes the entry with the given id from the calendar.
	 * 
	 * @param calendarEntryIdentifier
	 *            Id of the entry that is to be removed from the calendar.
	 * @return <code>true</code> if removal was successful, <code>false</code>
	 *         otherwise.
	 * @throws ArgumentNullException
	 *             if <code>calendarEntryIdentifier</code> is <code>null</code>.
	 */
	boolean remove(CalendarEntryIdentifier calendarEntryIdentifier);
}
