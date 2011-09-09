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
	READ, SHARE, DELETE, WRITE;
	
	public static final EnumSet<CalendarEntryCapability> CAPBILITY_ALL = EnumSet.allOf(CalendarEntryCapability.class);
}