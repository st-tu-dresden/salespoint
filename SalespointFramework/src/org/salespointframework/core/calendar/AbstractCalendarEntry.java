package org.salespointframework.core.calendar;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.joda.time.DateTime;
import org.salespointframework.util.Objects;

/**
 * 
 * This is an abstract representation of an calendar entry which provides basic functionality
 * 
 * @author stanley
 *
 */
@MappedSuperclass
public abstract class AbstractCalendarEntry implements CalendarEntry {
	
    @Id
    @GeneratedValue
    private long calendarEntryIdentifier;

    @ElementCollection
    protected Map<String, CapabilityList> capabilities = new HashMap<String, CapabilityList>();    
    
    /**
     * Description of this calendar entry. May be empty.
     */ 
    protected String description;
    
    /**
     * The start date and time for this entry.
     */
    protected long startTime;
    
    /**
     * The end date and time for this entry.
     */
    protected long endTime;
    
    /**
     * Title of this entry.
     */
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
     * @param owner The id of the user, who created this entry.
     * @param title The title of this entry.
     * @param start Start time and date.
     * @param end End time and date.
     * @throws IllegalArgumentException The {@link IllegalArgumentException} will be thrown, if the begin is not before the end of this calendar entry.
     */
    public AbstractCalendarEntry(String owner, String title, DateTime start, DateTime end) {
        Objects.requireNonNull(owner, "owner");
        Objects.requireNonNull(title, "title");
        Objects.requireNonNull(start, "start");
        Objects.requireNonNull(end, "end");
        
        if (start.isAfter(end))
            throw new IllegalArgumentException("An calendar entry cannot end before it starts." );
        
        this.title = title;
        this.startTime = start.getMillis();
        this.endTime = end.getMillis();
        
        description = "";
        repeatCount = 0;
        repeatStep = 0;
        
        for (CalendarEntryCapability cap : CalendarEntryCapability.values())
            addCapability(owner, cap);
    }
    
    @Override
    public boolean equals(Object object) {
        if (object instanceof CalendarEntry)
            return this.equals((CalendarEntry)object);
        return false;
    }
    
    /**
     * 
     * @param entry
     * @return
     */
    public boolean equals(CalendarEntry entry) {
        return this.hashCode() == entry.hashCode();
    }
    
    @Override
    public int hashCode() {
        //TODO think about this...
        return (int) calendarEntryIdentifier;
    }
    
    @Override
    public String getDescription() {
        return description;
    }
    
    @Override
    public String getTitle() {
        return title;
    }
    
    @Override
    public DateTime getStart() {
        return new DateTime(startTime);
    }
    
    @Override
    public DateTime getEnd() {
        return new DateTime(endTime);
    }
    
    @Override
    public long getID() {
        return calendarEntryIdentifier; 
    }

    @Override
    public void setStart(DateTime start) {
        Objects.requireNonNull(start, "start");
        if (start.getMillis() >= endTime)
            throw new IllegalArgumentException("An calendar entry cannot start after it ends." );
        
        this.startTime = start.getMillis();
    }

    @Override
    public void setEnd(DateTime end) {
        Objects.requireNonNull(end, "end");
        if (end.getMillis() <= startTime)
            throw new IllegalArgumentException("An calendar entry cannot end before it starts." );
        
        this.endTime = end.getMillis();
    }

    @Override
    public void setTitle(String title) {
        Objects.requireNonNull(title, "title");
        this.title = title;
    }

    @Override
    public void setDescription(String description) {
        Objects.requireNonNull(description, "description");
        this.description = description;
    }

    //TODO think about how to realize on-to-many relationship between two other entities (user <--> cap)
    @Override
    public String getOwner() {
        for (String userId : capabilities.keySet()) {
            if (capabilities.get(userId).contains(CalendarEntryCapability.OWNER)) {
                return userId;
            }
        }
        return null;
    }
    
    @Override
    public void addCapability(String user, CalendarEntryCapability capability) {
        Objects.requireNonNull(user, "user");
        Objects.requireNonNull(capability, "capability");
        
        CapabilityList capList = null;
        
        if ((capList = capabilities.get(user))==null) {
            capList = new CapabilityList();
            capabilities.put(user, capList);
        } else {
            if (capList.contains(capability))
                return;
        }
        
        capList.add(capability);
    }

    @Override
    public void removeCapability(String user, CalendarEntryCapability capability) {
        Objects.requireNonNull(user, "user");
        Objects.requireNonNull(capability, "capability");
        
        if (capability == CalendarEntryCapability.OWNER)
            throw new IllegalArgumentException("Capability 'OWNER' cannot be removed.");
            
        CapabilityList capList = null;
        
        if ((capList = capabilities.get(user))!=null) {
            capList.remove(capability);
            if (capList.isEmpty())
                capabilities.remove(user);
        }
    }

    public Iterable<CalendarEntryCapability> getCapabilitiesByUser(String user) {
        Objects.requireNonNull(user, "user");
        
        return capabilities.get(user);
    }
    
    public Iterable<String> getUsersByCapability(CalendarEntryCapability capability) {
        Objects.requireNonNull(capability, "capability");
        
        List<String> users = new LinkedList<String>();
        
        for (String user : capabilities.keySet()) {
            if (capabilities.get(user).contains(capability))
                users.add(user);
        }
        
        return users;
    }
    
}