package org.salespointframework.core.calendar;

import java.util.EnumSet;

/**
 * This enum stores capabilities which users can have relative to calendar
 * entries.
 * 
 * @author Stanley Förster
 * 
 */

public enum CalendarEntryCapability {
    /**
     * The user who has this capability is able to read the entry.
     */
	READ,
	
	/**
	 * The user who has this capability is able to share the entry with other users. 
	 */
	SHARE,
	
	/**
	 * The user who has this capability is able to delete the entry.
	 */
	DELETE,
	
	/**
	 * The user who has this capability is able to modify the entry. 
	 */
	WRITE;
	
	/**
	 * An enum set that containes all values of {@link CalendarEntryCapability}
	 */
	public static final EnumSet<CalendarEntryCapability> CAPBILITY_ALL = EnumSet.allOf(CalendarEntryCapability.class);
}