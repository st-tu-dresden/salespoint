package org.salespointframework.core.calendar;

import org.joda.time.DateTime;
import org.salespointframework.core.user.UserIdentifier;
import org.salespointframework.util.ArgumentNullException;

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
    void addCapability(UserIdentifier userIdentifier, CalendarEntryCapability capability);

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
    Iterable<CalendarEntryCapability> getCapabilities(UserIdentifier userIdentifier);

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
     */
    Iterable<UserIdentifier> getUsersByCapability(CalendarEntryCapability capability);

    /**
     * Removes a capability from a user
     * 
     * @param user
     *            The user's identification who should loose the capability.
     * @param capability
     *            the capability that the user should loose.
     * @throws ArgumentNullException
     *             if one ore more arguments are <code>null</code>
     * @throws IllegalArgumentException
     *             if a capability from the owner should be removed.
     */
    void removeCapability(UserIdentifier user, CalendarEntryCapability capability);

    /**
     * Changes the description.
     * 
     * @param description
     *            the new description
     * @throws ArgumentNullException
     *             if one ore more arguments are <code>null</code>
     */
    void setDescription(String description);

    /**
     * Sets a new end date.
     * 
     * @param end
     *            the new end date.
     * @throws ArgumentNullException
     *             if one ore more arguments are <code>null</code>
     */
    void setEnd(DateTime end);

    /**
     * Sets a new start date.
     * 
     * @param start
     *            the new start date.
     * @throws ArgumentNullException
     *             if one ore more arguments are <code>null</code>
     */
    void setStart(DateTime start);

    /**
     * Changes the title.
     * 
     * @param title
     *            the new title.
     * @throws ArgumentNullException
     *             if one ore more arguments are <code>null</code>
     */
    void setTitle(String title);
}
