package org.salespointframework.core.accountancy;

import org.joda.time.DateTime;
import org.salespointframework.core.money.Money;

/**
 * Implementing the <code>AccountancyEntry</code> interface allows objects to be
 * used with the <code>Accountancy</code>. An <code>AccountancyEntry</code>
 * consists at least of a date, some sort of description, and a monetary value
 * and a unique identifier
 * 
 * @author Hannes Weisbach
 * 
 */
public interface AccountancyEntry {
	/**
	 * @return the date, when this entry was posted.
	 */
	public DateTime getDate();

	/**
	 * 
	 * @return description, detailing the entry.
	 */
	public String getDescription();

	/**
	 * 
	 * @return the monetary value for this entry.
	 */
	public Money getValue();

	/**
	 * @return <code>AccountancyEntryIdentifier</code> to uniquely identify this
	 *         entry.
	 * 
	 */
	public AccountancyEntryIdentifier getIdentifier();
}