package org.salespointframework.core.calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.joda.time.DateTime;
import org.salespointframework.core.users.User;

/**
 * 
 * This is an abstract representation of an calendar entry which provides basic functionality
 * 
 * @author stanley
 *
 */
@Entity
public /* abstract */  class AbstractCalendarEntry implements CalendarEntry {
	/* Eclipse Link Bug entdeckt >__<, man verzeihe mir den Comment */
	
    @Id
    @GeneratedValue
    private int calendarEntryIdentifier;
    
    protected String description;
    
    /**
     * The start date and time for this entry.
     */
    protected DateTime startTime;
    /**
     * The end date and time for this entry.
     */
    protected DateTime endTime;
    protected String title;
    /**
     * Represents how often this entry should be repeated.
     * For determining the time between two repetitions, see {@link repeatedStep}
     * 
     */
    protected int repeatCount;
    /**
     * Represents the time in millis between two repetitions of this entry.
     * For determining how often an entry should be repeated, see {@link repeatedCount}
     */
    protected long repeatStep;
    
    /**
     * This contructor should not be used.
     * It only exists for persistence reasons.
     */
    @Deprecated
    public AbstractCalendarEntry() {
        
    }
    
    /**
     * Basic contructor with 
     * @param owner The user, who created this entry.
     * @param title The title of this entry.
     * @param start Start time and date.
     * @param end End time and date.
     * @throws IllegalArgumentException The {@link IllegalArgumentException} will be thrown, if the end lays before the start of a calendar entry.
     */
    public AbstractCalendarEntry(User owner, String title, DateTime start, DateTime end) {
        if (start.isAfter(end))
            throw new IllegalArgumentException("An calendar entry cannot end before it starts." );
        
        this.title = title;
        this.startTime = start;
        this.endTime = end;
        
        description = "";
        repeatCount = 0;
        repeatStep = 0;
    }
    
    @Override
    public boolean equals(Object object) {
        if (object instanceof CalendarEntry)
            return this.equals((CalendarEntry)object);
        return false;
    }
    
    public boolean equals(CalendarEntry entry) {
        return this.hashCode() == entry.hashCode();
    }
    
    @Override
    public int hashCode() {
        //TODO think about this...
        return calendarEntryIdentifier;
    }
    
    @Override
    public String getDescription() {
        return description;
    }
    
    @Override
    public String getOwner() {
        return null; //FIXME
    }
    
    @Override
    public String getTitle() {
        return title;
    }
    
    @Override
    public DateTime getStart() {
        return startTime;
    }
    
    @Override
    public DateTime getEnd() {
        return endTime;
    }
    
    @Override
    public int getID() {
        return calendarEntryIdentifier; 
    }
}
