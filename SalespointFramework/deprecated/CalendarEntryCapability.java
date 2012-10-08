package org.salespointframework.core.calendar;

import java.util.EnumSet;

/**
 * <code>CalendarEntryCapability</code>s are used to designate if a user is
 * allowed to perform a certain action on a given <code>CalendarEntry</code>.
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
	 * The user who has this capability is able to share the entry with other
	 * users.
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
	public static final EnumSet<CalendarEntryCapability> CAPBILITY_ALL = EnumSet
			.allOf(CalendarEntryCapability.class);
}