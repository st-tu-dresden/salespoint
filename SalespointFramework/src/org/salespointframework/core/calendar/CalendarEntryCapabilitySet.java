package org.salespointframework.core.calendar;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.salespointframework.core.users.UserIdentifier;
import org.salespointframework.util.Iterables;

@Entity
public class CalendarEntryCapabilitySet {
    @ElementCollection
    private Set<CalendarEntryCapability> capabilities = new HashSet<CalendarEntryCapability>();

    @SuppressWarnings("unused")
    @Id
    @GeneratedValue
    private long id;
    
    @AttributeOverride(name="id", column=@Column(name="USER_ID"))
    private UserIdentifier userIdentifier;
    @AttributeOverride(name="id", column=@Column(name="ENTRY_ID"))
    private CalendarEntryIdentifier calendarEntryIdentifier;
    
    @Deprecated
    public CalendarEntryCapabilitySet() {
    }

    public CalendarEntryCapabilitySet(UserIdentifier userIdentifier, CalendarEntryIdentifier calendarEntryIdentifier) {
        this.calendarEntryIdentifier = calendarEntryIdentifier;
        this.userIdentifier = userIdentifier; 
    }
    
    public UserIdentifier getUserIdentifier() {
        return userIdentifier;
    }
    
    public CalendarEntryIdentifier getCalendarEntryIdentifier() {
        return calendarEntryIdentifier;
    }
    
    public void add(CalendarEntryCapability capability) {
        capabilities.add(capability);
    }
    
    public void remove(CalendarEntryCapability capability) {
        capabilities.remove(capability);
    }
    
    public boolean isEmpty() {
        return capabilities.isEmpty();
    }
    
    public Iterator<CalendarEntryCapability> iterator() {
        return Iterables.from(capabilities).iterator();
    }

    public Iterable<CalendarEntryCapability> getCapabilities() {
        return Iterables.from(capabilities);
    }

    public boolean contains(CalendarEntryCapability capability) {
        return capabilities.contains(capability);
    }
}
