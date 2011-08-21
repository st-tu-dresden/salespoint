package org.salespointframework.core.calendar;

import org.joda.time.DateTime;
import org.salespointframework.core.users.UserIdentifier;

/**
 * A calendar entry is an appointment that basically sould have a definded start and end date and a title.
 * 
 * @author Stanley Förster
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
     * Should return the user's identifiaction who is the owner of this entry.
     * 
     * @return owner ID of the user who is the owner of this entry.
     */
    UserIdentifier getOwner();

    /**
     * Should return the identification of this entry.
     * 
     * @return Identification of this entry.
     */
    CalendarEntryIdentifier getCalendarEntryIdentifier();

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
     * Should change the title.
     * 
     * @param title
     *            the new title.
     */
    void setTitle(String title);

    /**
     * Should change the description.
     * 
     * @param description
     *            the new description
     */
    void setDescription(String description);

    /**
     * Should add a new capability to the given user for this entry.
     * 
     * @param user
     *            The user's identifiaction who should get a new capability.
     * @param capability
     *            the capability that the user should get.
     */
    void addCapability(UserIdentifier user, CalendarEntryCapability capability);

    /**
     * Removes the capability of the user
     * 
     * @param user
     *            The user's identification who should loose the capability.
     * @param capability
     *            the capability that the user should loose.
     */
    void removeCapability(UserIdentifier user, CalendarEntryCapability capability);
    
    /**
     * Should return all capabilities the given user has for this calendar entry.
     * 
     * @param user The user's identification, to get all capabilities for.
     * 
     * @return An iterable of all capabilities the given user has for this entry.
     */
    public Iterable<CalendarEntryCapability> getCapabilitiesByUser(UserIdentifier user);
    
    /**
     * Should return all users who have the given capability for this entry.
     * 
     * @param capability The capability for that all users should be returned.
     * @return An iterable of all user identifications who have the given capability for this entry.
     */
    public Iterable<UserIdentifier> getUsersByCapability(CalendarEntryCapability capability);
}
