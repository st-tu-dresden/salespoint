package org.salespointframework.core.calendar;

import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
class CapabilityList extends ArrayList<CalendarEntryCapability> {

    private static final long serialVersionUID = -4240058989013189975L;
    
    @SuppressWarnings("unused")
    @Id
    @GeneratedValue
    private long id;
    
}