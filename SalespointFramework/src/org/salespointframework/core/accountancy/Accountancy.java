package org.salespointframework.core.accountancy;

import org.joda.time.DateTime;

public interface Accountancy {

	/**
	 * Adds a new <code>AccountancyEntry</code> to this <code>Accountancy</code>
	 * The new entry is persisted transparently into the underlying database.
	 * Once an <code>AccountancyEntry</code> has been written to the database,
	 * it cannot be added a second time.
	 * 
	 * @param accountancyEntry
	 *            <code>AccountancyEntry</code> which should be added to the
	 *            <code>Accountancy</code>
	 */
	void addEntry(AbstractAccountancyEntry accountancyEntry);

	/**
	 * Adds multiple <code>AccountancyEntry</code>s to this
	 * <code>Accountancy</code> and persists them to underlying database. Once
	 * an <code>AccountancyEntry</code> has been added to the persistence layer,
	 * it cannot be modified again.
	 * 
	 * @param accountancyEntries
	 */
	 void addEntries(Iterable<AbstractAccountancyEntry> accountancyEntries);

	/**
	 * Returns all <code>AccountancyEntry</code>s in between the dates
	 * <code>from</code> and <code>to</code>, including from and to. So every
	 * entry with an time stamp <= to and >= from is returned. If no entries
	 * within the specified time span exist, an empty Iterable is returned.
	 * 
	 * @param from
	 *            time stamp denoting the start of the requested time period
	 * @param to
	 *            time stamp denoting the end of the requested time period
	 * @return an unmodifiable Iterable containing all entries between from and
	 *         to
	 */
	Iterable<AbstractAccountancyEntry> getEntries(DateTime from, DateTime to);

	
	
	// TODO häßlich mit Class<T>, gez. Paul
	/**
	 * Returns all <code>AccountancyEntry</code>s of the specified type
	 * <code>class</code>. If no entries of the specified type exist, an empty
	 * Iterable is returned.
	 * 
	 * @param clazz
	 *            The type of the entries.
	 * @return an unmodifiable Iterable containing all entries of type clazz
	 */
	<T extends AbstractAccountancyEntry> Iterable<T> getEntries(Class<T> clazz);

	/**
	 * Returns all <code>AccountancyEntry</code>s in between the dates
	 * <code>from</code> and <code>to</code> of the specified class type
	 * <code>clazz</code>, including from and to. So every entry with an time
	 * stamp <= to and >= from is returned. If no entries within the specified
	 * time span exist, or no entries of the specified class type exist, an
	 * empty Iterable is returned.
	 * 
	 * @param <T>
	 *            type of the requested entries
	 * 
	 * @param from
	 *            time stamp denoting the start of the requested time period
	 * @param to
	 *            time stamp denoting the end of the requested time period
	 * @param clazz
	 *            class type of the requested entries
	 * @return an unmodifiable Iterable containing all entries between from and
	 *         to of type T
	 */
	<T extends AbstractAccountancyEntry> Iterable<T> getEntries(Class<T> clazz, DateTime from, DateTime to);

}