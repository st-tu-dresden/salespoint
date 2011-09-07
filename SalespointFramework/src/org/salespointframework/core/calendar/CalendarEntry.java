package org.salespointframework.core.calendar;

import org.joda.time.DateTime;
import org.salespointframework.core.users.UserIdentifier;

/**
 * A calendar entry is an appointment that has a start date, end date, and a
 * title.
 * 
 * @author Stanley Förster
 * 
 */
public interface CalendarEntry {

	/**
	 * Returns the time <code>this</code> appointment begins.
	 * 
	 * @return start <code>DateTime</code>, which is immutable.
	 * 
	 * @see DateTime
	 */
	DateTime getStart();

	/**
	 * Returns the time <code>this</code> appointments is over.
	 * 
	 * @return end <code>DateTime</code>, which is immutable.
	 * 
	 * @see DateTime
	 */
	DateTime getEnd();

	/**
	 * Returns the title of <code>this</code> entry.
	 * 
	 * @return title of this entry.
	 */
	String getTitle();

	/**
	 * Returns a description of this entry
	 * 
	 * @return description of this entry.
	 */
	String getDescription();

	/**
	 * Returns the identification of the user who owns this entry.
	 * 
	 * @return owner ID of the user who owns this entry.
	 */
	UserIdentifier getOwner();

	/**
	 * Returns the ID of this entry.
	 * 
	 * @return Identification of this entry.
	 */
	CalendarEntryIdentifier getCalendarEntryIdentifier();

	/**
	 * Sets a new start date.
	 * 
	 * @param start
	 *            the new start date.
	 */
	void setStart(DateTime start);

	/**
	 * Sets a new end date.
	 * 
	 * @param end
	 *            the new end date.
	 */
	void setEnd(DateTime end);

	/**
	 * Changes the title.
	 * 
	 * @param title
	 *            the new title.
	 */
	void setTitle(String title);

	/**
	 * Changes the description.
	 * 
	 * @param description
	 *            the new description
	 */
	void setDescription(String description);

	/**
	 * Adds a new capability to the given user. The capability is limited for
	 * this entry.
	 * 
	 * @param user
	 *            The user's identifiaction who should get a new capability.
	 * @param capability
	 *            the capability that the user should get.
	 */
	void addCapability(UserIdentifier user, CalendarEntryCapability capability);

	/**
	 * Removes a capability from a user
	 * 
	 * @param user
	 *            The user's identification who should loose the capability.
	 * @param capability
	 *            the capability that the user should loose.
	 */
	void removeCapability(UserIdentifier user,
			CalendarEntryCapability capability);

	/**
	 * Returns all capabilities granted to a particular user. All capabilities
	 * refer to <code>this</code> calendar entry.
	 * 
	 * @param user
	 *            The user's identification, to get all capabilities for.
	 * 
	 * @return An iterable of all capabilities the given user has for this
	 *         entry.
	 */
	public Iterable<CalendarEntryCapability> getCapabilitiesByUser(
			UserIdentifier user);

	/**
	 * Returns all users who have a certain capability for this entry.
	 * 
	 * @param capability
	 *            The capability for that all users should be returned.
	 * @return An iterable of all user identifications who have the given
	 *         capability for this entry.
	 */
	public Iterable<UserIdentifier> getUsersByCapability(
			CalendarEntryCapability capability);
}
