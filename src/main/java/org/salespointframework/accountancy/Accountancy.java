package org.salespointframework.accountancy;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import org.joda.money.Money;
import org.salespointframework.time.Interval;

/**
 * The {@code Accountancy} interface is implemented by classes offering a basic accounting service. Generally, an
 * {@code Accountancy} aggregates objects of the type {@link AccountancyEntry} and subclasses thereof. Additionally, an
 * {@code Accountancy} offers methods for querying of entries and financial statistics.
 * 
 * @author Hannes Weisbach
 * @author Oliver Gierke
 */
public interface Accountancy<T extends AccountancyEntry> {

	/**
	 * Adds a new {@link AccountancyEntry} to this {@code Accountancy}.
	 * 
	 * @param accountancyEntry entry to be added to the accountancy
	 */
	T add(T accountancyEntry);

	/**
	 * Returns all {@link AccountancyEntry}s of the specified type {@code clazz} and all sub-types, previously added to
	 * the accountancy. If no entries of the specified type exist, an empty {@code Iterable} is returned.
	 * 
	 * @param <T> common super type of all entries returned.
	 * @param clazz Class object corresponding to the type of the entries to be returned, has to implement
	 *          {@link AccountancyEntry}
	 * @return an {@link Iterable} containing all entries of type clazz
	 */
	Iterable<T> findAll();

	/**
	 * Returns the {@link AccountancyEntry} of type {@code clazz} and all sub-types, identified by
	 * {@link AccountancyEntryIdentifier}. {@literal null} is returned, if no entry with the given identifier exists.
	 * 
	 * @param <E> common super type of all entries returned.
	 * @param clazz type of the entry to be returned; has to implement {@link AccountancyEntry}
	 * @param accountancyEntryIdentifier the {@link AccountancyEntryIdentifier} of the entry to be returned
	 * @return the {@link AccountancyEntry} or sub type thereof of type {@code clazz} which has the identifier
	 *         {@link AccountancyEntryIdentifier}
	 */
	Optional<T> get(AccountancyEntryIdentifier accountancyEntryIdentifier);

	/**
	 * Returns all {@link AccountancyEntry}s in between the dates {@code from} and {@code to} of the specified class type
	 * {@code clazz} and all sub-types, including from and to. So every entry with an time stamp <= {@code to} and >=
	 * {@code from} is returned. If no entries within the specified time span exist, or no entries of the specified class
	 * type exist, an empty Iterable is returned.
	 * 
	 * @param <T> common super type of all entries returned
	 * @param from {@link LocalDateTime} denoting the start of the requested time period
	 * @param to {@link LocalDateTime} denoting the end of the requested time period
	 * @param clazz class type of the requested entries, has to implement {@link AccountancyEntry}
	 * @return an {@link Iterable} containing all entries between from and to of type E
	 */
	Iterable<T> find(LocalDateTime from, LocalDateTime to);

	// TODO comment fortsetzen? -> " If no entries for an interval exist"
	/**
	 * Returns all {@link AccountancyEntry}s of type {@code clazz} and all sub-types, which have their {@code date} within
	 * (including) {@code from} and {@code to}. <br />
	 * The time between {@code from} and {@code to} is divided into parts of {@code period} length. According to their
	 * respective date, entries are sorted in exactly one of the time intervals. The last time interval may be shorter
	 * than {@code period}.<br />
	 * Returned is a map, having a {@link Interval} objects as its key, and an {@link Iterable} as value. The
	 * {@link Iterable} contains all entries of the specific type with its date in the interval specified by the key.<br />
	 * If no entries for an interval exist, the {@link Iterable}.
	 * 
	 * @param <T> common super type of all entries returned
	 * @param clazz class type of the requested entries; has to implement {@link AccountancyEntry}
	 * @param from all returned entries will have a time stamp after {@code from}
	 * @param to all returned entries will have a time stamp before {@code to}
	 * @param duration length of the time intervals, the period between {@code from} and {@code to} is divided
	 * @return a map, with intervals of {@code period} length between {@code from} and {@code to} as keys, and as value an
	 *         <code>Iterable</code> containing all entries within the key- <code>Interval</code>
	 */
	Map<Interval, Iterable<T>> find(LocalDateTime from, LocalDateTime to, Duration duration);

	/**
	 * Returns the sum of the field {@code amount} of all {@link AccountancyEntry}s of type {@code clazz} and its
	 * sub-types, which have their {@code date} within (including) {@code from} and {@code to}. <br />
	 * The time between {@code from} and {@code to} is divided into parts of {@code period} length. According to their
	 * time stamp, entries are sorted in exactly one of the time intervals. The last time interval may be shorter than
	 * {@code period}.<br />
	 * Returned is a map, having a {@link Interval} objects as its key, and an {@link Money} as value. The {@link Money}
	 * object's value is equal to the sum of all entries' {@code amount}-field, with a date within the key-
	 * {@link Interval}. If within an interval no entries of the specified type exist, a {@link Money} object with a value
	 * of zero is added as value for that interval.
	 * 
	 * @param <T> common super type of all entries returned.
	 * @param from all returned entries will have a time stamp after {@code from}.
	 * @param to all returned entries will have a time stamp before {@code to}.
	 * @param duration length of the time intervals, the period between {@code from} and {@code to} is divided.
	 * @return a {@link Map}, with intervals of {@code period} length between {@code from} and {@code to} as keys, and as
	 *         value a {@link Money} object, equal to the sum of the amount fields of all entries within the key-
	 *         {@link Interval}.
	 */
	Map<Interval, Money> salesVolume(LocalDateTime from, LocalDateTime to, Duration duration);
}
