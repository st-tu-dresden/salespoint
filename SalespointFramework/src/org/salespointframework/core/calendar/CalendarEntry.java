package org.salespointframework.core.calendar;

import org.joda.time.DateTime;

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
    String getOwner();
    
    /**
     * Returns the id of this entry
     * @return ID
     */
    int getID();

}
