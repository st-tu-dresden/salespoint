package org.salespointframework.core.calendar;

import org.joda.time.DateTime;
import org.salespointframework.core.users.User;

/**
 * 
 * @author stanley
 *
 */
public interface CalendarEntry {
    
    /**
     * Returns the start date and time of this entry
     * @return start date and time
     */
    DateTime getStart();
    
    /**
     * Returns the end date and time of this entry
     * @return end date and time
     */
    DateTime getEnd();
    
    /**
     * Returns the title of this entry
     * @return title
     */
    String getTitle();
    
    /**
     * Returns the description of this entry
     * @return description
     */
    String getDescription();
    
    /**
     * Returns the owner this entry
     * @return owner
     */
    User getOwner();
    
    /**
     * Returns the id of this entry
     * @return ID
     */
    int getID();
    
    void setStart(DateTime start);
    void setEnd(DateTime end);
    void setTitle(String title);
    void setDescription(String description);
    
    void addCapability(User user, CalendarEntryCapability capability);
    void removeCapability(User user, CalendarEntryCapability capability);

}
