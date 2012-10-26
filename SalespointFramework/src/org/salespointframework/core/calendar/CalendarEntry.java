package org.salespointframework.core.calendar;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.salespointframework.core.user.UserIdentifier;


/**
 * A calendar entry is an appointment that has a start date, end date, and a
 * title.
 * 
 * @author Stanley Förster
 * 
 */
public interface CalendarEntry {

    /**
     * Adds a new capability to the given user. The capability is limited for
     * this entry.
     * 
     * @param userIdentifier
     *            The user's identification who should get a new capability.
     * @param capability
     *            the capability that the user should get.
     * @throws ArgumentNullException
     *             if <code>userIdentifier</code> or <code>capability</code> or
     *             both are <code>null</code>
     */
    //void addCapability(UserIdentifier userIdentifier, CalendarEntryCapability capability);

    /**
     * Returns the ID of this entry. The identifier can be used e.g. as primary
     * key in a database or as key in a <code>Map</code>-based implementation
     * 
     * @return The ID of this entry.
     */
    CalendarEntryIdentifier getIdentifier();

    /**
     * Returns all capabilities granted to a particular user. All capabilities
     * refer to <code>this</code> calendar entry.
     * 
     * @param userIdentifier
     *            The user's identification, to get all capabilities for.
     * 
     * @return An Iterable of all capabilities the given user has for this
     *         entry.
     * @throws ArgumentNullException
     *             if <code>userIdentifier</code> is <code>null</code>
     */
    //Iterable<CalendarEntryCapability> getCapabilities(UserIdentifier userIdentifier);

    /**
     * Returns a description of this entry
     * 
     * @return description of this entry.
     */
    String getDescription();

    /**
     * Returns the time <code>this</code> appointments is over.
     * 
     * @return end <code>DateTime</code>.
     * 
     * @see DateTime
     */
    DateTime getEnd();

    /**
     * Returns the identification of the user who owns this entry.
     * 
     * @return owner ID of the user who owns this entry.
     */
    UserIdentifier getOwner();

    /**
     * Returns the time <code>this</code> appointment begins.
     * 
     * @return start <code>DateTime</code>.
     * 
     * @see DateTime
     */
    DateTime getStart();

    /**
     * Returns the title of <code>this</code> entry.
     * 
     * @return title of this entry.
     */
    String getTitle();

    /**
     * Returns all users who have a certain capability for this entry.
     * 
     * @param capability
     *            The capability for that all users should be returned.
     * @return An iterable of all user identifications who have the given
     *         capability for this entry.
     * @throws ArgumentNullException
     *             if <code>capability</code> is <code>null</code>
     */
    //Iterable<UserIdentifier> getUsersByCapability(CalendarEntryCapability capability);

    /**
     * Removes a capability from a user
     * 
     * @param user
     *            The user's identification who should loose the capability.
     * @param capability
     *            the capability that the user should loose.
     * @throws ArgumentNullException
     *             if <code>user</code> or <code>capability</code> or both are
     *             <code>null</code>
     * @throws IllegalArgumentException
     *             if a capability from the owner should be removed.
     */
    //void removeCapability(UserIdentifier user, CalendarEntryCapability capability);

    /**
     * Changes the description.
     * 
     * @param description
     *            the new description
     * @throws NullPointerException
     *             if <code>description</code> is <code>null</code>
     */
    void setDescription(String description);

    /**
     * Sets a new end date. If it is before the current start date, the start
     * will be set back, too. The length of the appointment will be the same as
     * before.
     * 
     * @param end
     *            the new end date.
     * @throws NullPointerException
     *             if <code>end</code> is <code>null</code>
     */
    void setEnd(DateTime end);

    /**
     * Sets a new start date. If it is after the current end date, the end will
     * be set forward, too. The length of the appointment will be the same as
     * before.
     * 
     * @param start
     *            the new start date.
     * @throws NullPointerException
     *             if <code>is</code> is <code>null</code>
     */
    void setStart(DateTime start);

    /**
     * Changes the title.
     * 
     * @param title
     *            the new title.
     * @throws NullPointerException
     *             if <code>title</code> is <code>null</code>
     */
    void setTitle(String title);

    /**
     * Returns an {@link Iterable} which contains all time intervals of this
     * event and its repetitions. <code>maxEntries</code> is required to limit
     * the output for infinitely often repeated events. If
     * <code>maxEntries</code> is greater than <code>repeatCount</code>, only
     * <code>repeatCount</code> items will be returned. Possible values for
     * maxEntries are
     * <ul>
     * <li>&lt; 0 - output will be an empty iterable.</li>
     * <li>=&nbsp;0 - output will contain only the original event.</li>
     * <li>&gt; 0 - output will contain a maximum of <code>maxEntries</code>
     * repetitions, inclusive the original event.</li>
     * </ul>
     * 
     * @param maxEntries
     *            limits the output to a specified amount of repetitions.
     * @return An iterable that contains the original event and all repetitions,
     *         limited to <code>maxEntires</code>.
     */
	Iterable<Interval> getEntryList(int maxEntries);

	
    /**
     * Returns an {@link Iterable} which contains all time intervals of this
     * event and its repetitions that overlaps with the given {@link Interval}.
     * For a definition of <coder>overlaps()</code>, see
     * {@link Interval#overlaps(org.joda.time.ReadableInterval)}. If the given
     * interval is completely before the first occurence of this event, the
     * result will be empty. The result also can contain a maximum of
     * {@link Integer#MAX_VALUE} entries.
     * 
     * @param interval
     *            all returned intervals should overlap with this given
     *            interval.
     * @return An iterable that contains all found intervals.
     */
	Iterable<Interval> getEntryList(Interval interval);
}
