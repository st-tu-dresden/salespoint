package org.salespointframework.core.calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class CalendarEntryCapability {
    
    @SuppressWarnings("unused")
    @Id
    @GeneratedValue
    private long id;
    
    protected String name;
    
    @Deprecated
    public CalendarEntryCapability() {}
    
    public CalendarEntryCapability(String name) {
        this.name=name;
    }
    
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof CalendarEntryCapability) && obj.hashCode() == this.hashCode();
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
