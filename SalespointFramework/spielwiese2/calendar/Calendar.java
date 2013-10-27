package org.salespointframework.core.calendar;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.salespointframework.core.useraccount.UserAccountIdentifier;


// TODO fix javadoc

/**
 * A calendar manages a set of calendar entries.
 * 
 * @param <T>
 *            Type of calendar entries (extends
 *            {@link org.salespointframework.core.calendar.CalendarEntry})
 * 
 * @author Stanley Förster
 * @author Oliver Gierke
 */
public interface Calendar {

	/**
	 * Adds the given entry to the calendar.
	 * 
	 * @param entry
	 *            the entry that is to be added to the calendar.
	 * 
	 * @throws NullPointerException
	 *             if <code>entry</code> is <code>null</code>
	 */
	void add(CalendarEntry entry);

	/**
	 * Checks, whether there exists an {@link CalendarEntry} that has the given
	 * {@link CalendarEntryIdentifier} in this calendar or not.
	 * 
	 * @param calendarEntryIdentifier
	 *            The identifier of the entry that should be checked to be
	 *            contained in this calendar.
	 * @return <code>true</code> if there exists an entry with the given
	 *         identifier in this calendar, <code>false</code> otherwise.
	 * @throws NullPointerException
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
	 * @throws NullPointerException
	 *             if <code>calendarEntryIdentifier</code> is <code>null</code>.
	 */
	<E extends CalendarEntry> E get(Class<E> clazz,
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
	<E extends CalendarEntry> Iterable<E> find(Class<E> clazz);

	/**
	 * Deletes the entry with the given id from the calendar.
	 * 
	 * @param calendarEntryIdentifier
	 *            Id of the entry that is to be removed from the calendar.
	 * @return <code>true</code> if removal was successful, <code>false</code>
	 *         otherwise.
	 * @throws NullPointerException
	 *             if <code>calendarEntryIdentifier</code> is <code>null</code>.
	 */
	boolean remove(CalendarEntryIdentifier calendarEntryIdentifier);

    /**
     * Returns all entries that have the given title.
     * 
     * @param <E>
     *            common super type of all entries returned
     * 
     * @param clazz
     *            Class object corresponding to the type of the entries to be
     *            returned, has to be <code>PersistentCalendarEntry</code> or a
     *            sub-class of it.
     * 
     * @param title
     *            The title that entries should have.
     * @return An Iterable with all found entries
     */
	<E extends CalendarEntry> Iterable<E> find(Class<E> clazz, String title);

    /**
     * Returns all entries for that the given user is the owner.
     * 
     * @param <T>
     *            common super type of all entries returned
     * 
     * @param clazz
     *            Class object corresponding to the type of the entries to be
     *            returned, has to be <code>PersistentCalendarEntry</code> or a
     *            sub-class of it.
     * 
     * @param userIdentifier
     *            {@link UserAccountIdentifier} of the user whose entries should be
     *            found.
     * @return An {link @Iterable} with all found entries.
     */
	<E extends CalendarEntry> Iterable<E> find(Class<E> clazz, UserAccountIdentifier userIdentifier);

    /**
     * Returns all entries that start and end between the given start and end date.
     * 
     * @param <T>
     *            common super type of all entries returned
     * 
     * @param clazz
     *            Class object corresponding to the type of the entries to be
     *            returned, has to be <code>PersistentCalendarEntry</code> or a
     *            sub-class of it.
     * 
     * @param start
     *            The start of the interval in which returned entries should
     *            have their start and end date.
     * 
     * @param end
     *            The end of the interval in which returned entries should have
     *            their start and end date. The end is exclusive.
     * 
     * 
     * @return An Iterable with all found entries.
     * 
     * @throws NullPointerException
     *             if <code>start</code> or <code>end</code> or both are <code>null</code>
     * 
     * @see Interval#contains(org.joda.time.ReadableInterval)
     */
	<E extends CalendarEntry> Iterable<E> between(Class<E> clazz, DateTime start, DateTime end);

    /**
     * Returns all entries that end between the given start and end date.
     * 
     * @param <T>
     *            common super type of all entries returned
     * 
     * @param clazz
     *            Class object corresponding to the type of the entries to be
     *            returned, has to be <code>PersistentCalendarEntry</code> or a
     *            sub-class of it.
     * 
     * @param start
     *            The start of the interval in which returned entries should
     *            have their end date.
     * 
     * @param end
     *            The end of the interval in which returned entries should have
     *            their end date. The end is exclusive.
     * 
     * 
     * @return An iterable with all found entries.
     * 
     * @throws NullPointerException
     *             if <code>start</code> or <code>end</code> or both are <code>null</code>
     * 
     * @see Interval#contains(org.joda.time.ReadableInstant)
     */
	<E extends CalendarEntry> Iterable<E> endsBetween(Class<E> clazz, DateTime start, DateTime end);

    /**
     * Returns all entries that start between the given start and end date.
     * 
     * @param <T>
     *            common super type of all entries returned
     * 
     * @param clazz
     *            Class object corresponding to the type of the entries to be
     *            returned, has to be <code>PersistentCalendarEntry</code> or a
     *            sub-class of it.
     * 
     * @param start
     *            The start of the interval in which returned entries should
     *            have their start date.
     * 
     * @param end
     *            The end of the interval in which returned entries should have
     *            their start date. The end is exclusive.
     * 
     * 
     * @return An iterable with all found entries.
     * 
     * @throws NullPointerException
     *             if <code>start</code> or <code>end</code> or both are <code>null</code>
     * 
     * @see Interval#contains(org.joda.time.ReadableInstant)
     */
	<E extends CalendarEntry> Iterable<E> startsBetween(Class<E> clazz, DateTime start, DateTime end);


}
