package org.salespointframework.core.accountancy;

import org.joda.time.DateTime;
import org.salespointframework.core.money.Money;

public interface AccountancyEntry
{
	AccountancyEntryIdentifier getAccountancyEntryIdentifier();

	// TODO user defined one?
	/**
	 * The timestamp of this entry. This can be the creation time or a user
	 * defined one, that was given to the constructor when it has been created.
	 * 
	 * @return the timestamp that is stored in this entry.
	 */
	DateTime getCreationDate();

	/**
	 * The value of this entry.
	 * 
	 * @return the value that is stored in this entry.
	 */
	Money getValue();

}