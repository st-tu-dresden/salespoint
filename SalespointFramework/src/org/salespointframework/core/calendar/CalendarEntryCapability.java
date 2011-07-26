package org.salespointframework.core.calendar;

import javax.persistence.Entity;

/**
 * This enum stores capabilities which users can have relative to calendar
 * entries.
 * 
 * @author stanley
 * 
 */
@Entity
public enum CalendarEntryCapability {
    OWNER,
    READ,
    SHARE,
    DELETE,
    WRITE
}