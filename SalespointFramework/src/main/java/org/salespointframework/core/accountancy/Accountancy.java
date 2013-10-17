package org.salespointframework.core.accountancy;

import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.salespointframework.core.money.Money;

/**
 * The <code>Accountancy</code> interface is implemented by classes offering a
 * basic accounting service. Generally, an <code>Accountancy</code> aggregates
 * objects of the type {@link AccountancyEntry} and subclasses thereof.
 * Additionally, an <code>Accountancy</code> offers methods for querying of
 * entries and financial statistics.
 * 
 * @author Hannes Weisbach
 */
public interface Accountancy {
	/**
	 * Adds a new {@link AccountancyEntry} to this <code>Accountancy</code>	 * .
	 * 
	 * @param accountancyEntry
	 *            entry to be added to the accountancy
	 */
	void add(AccountancyEntry accountancyEntry);

	/**
	 * Returns all {@link AccountancyEntry}s of the specified type
	 * <code>clazz</code> and all sub-types, previously added to the
	 * accountancy.
	 * 
	 * If no entries of the specified type exist, an empty <code>Iterable</code>
	 * is returned.
	 * 
	 * @param <E>
	 *            common super type of all entries returned
	 * 
	 * @param clazz
	 *            Class object corresponding to the type of the entries to be
	 *            returned, has to implement {@link AccountancyEntry}
	 * 
	 * @return an {@link Iterable} containing all entries of type clazz
	 */
	<E extends AccountancyEntry> Iterable<E> find(Class<E> clazz);

	/**
	 * Returns the {@link AccountancyEntry} of type <code>clazz</code> and
	 * all sub-types, identified by {@link AccountancyEntryIdentifier}.
	 * 
	 * <code>null</code> is returned, if no entry with the given identifier
	 * exists.
	 * 
	 * @param <E>
	 *            common super type of all entries returned
	 * @param clazz
	 *            type of the entry to be returned; has to implement
	 *            {@link AccountancyEntry}
	 * @param accountancyEntryIdentifier the {@link AccountancyEntryIdentifier} of the entry to be returned
	 * @return the {@link AccountancyEntry} or sub type thereof of type
	 *         <code>clazz</code> which has the identifier
	 *         {@link AccountancyEntryIdentifier}
	 */
	<E extends AccountancyEntry> E get(Class<E> clazz,
			AccountancyEntryIdentifier accountancyEntryIdentifier);

	/**
	 * Returns all {@link AccountancyEntry}s in between the dates
	 * <code>from</code> and <code>to</code> of the specified class type
	 * <code>clazz</code> and all sub-types, including from and to. So every
	 * entry with an time stamp <= <code>to</code> and >= <code>from</code> is
	 * returned. If no entries within the specified time span exist, or no
	 * entries of the specified class type exist, an empty Iterable is returned.
	 * 
	 * @param <E>
	 *            common super type of all entries returned
	 * 
	 * @param from
	 *            {@link DateTime} denoting the start of the requested time period
	 * @param to
	 *            {@link DateTime} denoting the end of the requested time period
	 * @param clazz
	 *            class type of the requested entries, has to implement
	 *            {@link AccountancyEntry}
	 * @return an {@link Iterable} containing all entries between from and to of type E
	 */
	<E extends AccountancyEntry> Iterable<E> find(Class<E> clazz, DateTime from, DateTime to);

	
	// TODO comment fortsetzen? -> " If no entries for an interval exist"
	/**
	 * Returns all {@link AccountancyEntry}s of type <code>clazz</code> and
	 * all sub-types, which have their <code>date</code> within (including)
	 * <code>from</code> and <code>to</code>. <br>
	 * The time between <code>from</code> and <code>to</code> is divided into
	 * parts of <code>period</code> length. According to their respective date,
	 * entries are sorted in exactly one of the time intervals. The last time
	 * interval may be shorter than <code>period</code>.<br>
	 * Returned is a map, having a {@link Interval} objects as its key, and
	 * an {@link Iterable} as value. The <code>Iterable</code>
	 * contains all entries of the specific type with its date in the interval
	 * specified by the key.<br>
	 * If no entries for an interval exist, the <code>Iterable</code>	
	 * 
	 * 
	 * @param <E>
	 *            common super type of all entries returned
	 * 
	 * @param clazz
	 *            class type of the requested entries; has to implement
	 *            {@link AccountancyEntry}
	 * @param from
	 *            all returned entries will have a time stamp after
	 *            <code>from</code>
	 * @param to
	 *            all returned entries will have a time stamp before
	 *            <code>to</code>
	 * @param period
	 *            length of the time intervals, the period between
	 *            <code>from</code> and <code>to</code> is divided
	 * @return a map, with intervals of <code>period</code> length between
	 *         <code>from</code> and <code>to</code> as keys, and as value an
	 *         <code>Iterable</code> containing all entries within the key-
	 *         <code>Interval</code>
	 */
	<E extends AccountancyEntry> Map<Interval, Iterable<E>> find(Class<E> clazz,
			DateTime from, DateTime to, Period period);

	/**
	 * Returns the sum of the field <code>amount</code> of all
	 * {@link AccountancyEntry}s of type <code>clazz</code> and its
	 * sub-types, which have their <code>date</code> within (including)
	 * <code>from</code> and <code>to</code>. <br>
	 * The time between <code>from</code> and <code>to</code> is divided into
	 * parts of <code>period</code> length. According to their time stamp,
	 * entries are sorted in exactly one of the time intervals. The last time
	 * interval may be shorter than <code>period</code>.<br>
	 * Returned is a map, having a <code>Interval</code> objects as its key, and
	 * an <code>Money</code> as value. The <code>Money</code> object's value is
	 * equal to the sum of all entries' <code>amount</code>-field, with a date
	 * within the key-<code>Interval</code>.
	 * 
	 * If within an interval no entries of the specified type exist, a
	 * <code>Money</code> object with a value of zero is added as value for that
	 * interval.
	 * 
	 * @param <E>
	 *            common super type of all entries returned
	 * 
	 * @param clazz
	 *            class type of the requested entries; has implement
	 *            {@link AccountancyEntry}
	 * @param from
	 *            all returned entries will have a time stamp after
	 *            <code>from</code>
	 * @param to
	 *            all returned entries will have a time stamp before
	 *            <code>to</code>
	 * @param period
	 *            length of the time intervals, the period between
	 *            <code>from</code> and <code>to</code> is divided
	 * @return a map, with intervals of <code>period</code> length between
	 *         <code>from</code> and <code>to</code> as keys, and as value a
	 *         <code>Money</code> object, equal to the sum of the amount fields
	 *         of all entries within the key-<code>Interval</code>
	 */
	<E extends AccountancyEntry> Map<Interval, Money> salesVolume(Class<E> clazz,
			DateTime from, DateTime to, Period period);
}