package org.salespointframework.core.calendar;

import org.joda.time.DateTime;

/**
 * This is an interface which provides basic methods to handle an entry of a
 * calendar.
 * 
 * @author stanley
 * 
 */
public interface CalendarEntry {

    /**
     * Returns the start date and time of this entry
     * 
     * @return start date and time
     */
    DateTime getStart();

    /**
     * Returns the end date and time of this entry
     * 
     * @return end date and time
     */
    DateTime getEnd();

    /**
     * Returns the title of this entry
     * 
     * @return title
     */
    String getTitle();

    /**
     * Returns the description of this entry
     * 
     * @return description
     */
    String getDescription();

    /**
     * Returns the user-id of the owner of this entry
     * 
     * @return owner
     */
    String getOwner();

    /**
     * Returns the id of this entry
     * 
     * @return ID
     */
    long getID();

    /**
     * Sets a new start time.
     * 
     * @param start
     *            the new start time
     */
    void setStart(DateTime start);

    /**
     * Sets a new end time.
     * 
     * @param end
     *            the new end time
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
    void addCapability(String user, CalendarEntryCapability capability);

    /**
     * Removes the capability of the user
     * 
     * @param user
     *            the user who should loose the capability
     * @param capability
     *            the capability that the user should loose
     */
    void removeCapability(String user, CalendarEntryCapability capability);

}
