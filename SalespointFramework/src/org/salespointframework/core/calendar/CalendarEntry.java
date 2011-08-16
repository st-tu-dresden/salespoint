package org.salespointframework.core.calendar;

import org.joda.time.DateTime;
import org.salespointframework.core.users.UserIdentifier;

/**
 * A calendar entry is an appointment that basically sould have a definded start and end date and a title.
 * 
 * @author Stanley Foerster
 * 
 */
public interface CalendarEntry {

    /**
     * Should return the start point in time when this appointment starts.
     * 
     * @return start date and time which is unmodifiable.
     * 
     * @see DateTime
     */
    DateTime getStart();

    /**
     * Should return the end point in time of when this appointment ends.
     * 
     * @return end date and time which is unmodifiable
     * 
     * @see DateTime
     */
    DateTime getEnd();

    /**
     * Should return the title of this entry.
     * 
     * @return title of this entry.
     */
    String getTitle();

    /**
     * Should return a description of this entry
     * 
     * @return description of this entry.
     */
    String getDescription();

    /**
     * Should the user's id who is the owner of this entry.
     * 
     * @return owner ID of the user who is the owner of this entry.
     */
    UserIdentifier getOwner();

    /**
     * Should return the id of this entry.
     * 
     * @return ID of this entry.
     */
    CalendarEntryIdentifier getID();

    /**
     * Should set a new start date.
     * 
     * @param start
     *            the new start date.
     */
    void setStart(DateTime start);

    /**
     * Should set a new end date.
     * 
     * @param end
     *            the new end date.
     */
    void setEnd(DateTime end);

    /**
     * Sets a new title.
     * 
     * @param title
     *            the new title
     */
    void setTitle(String title);

    /**
     * Sets a new description
     * 
     * @param description
     *            the new description
     */
    void setDescription(String description);

    /**
     * Adds a new capability for the user.
     * 
     * @param user
     *            the user who should get a new capability
     * @param capability
     *            the capability that the user should get
     */
    void addCapability(UserIdentifier user, CalendarEntryCapability capability);

    /**
     * Removes the capability of the user
     * 
     * @param user
     *            the user who should loose the capability
     * @param capability
     *            the capability that the user should loose
     */
    void removeCapability(UserIdentifier user, CalendarEntryCapability capability);

}
