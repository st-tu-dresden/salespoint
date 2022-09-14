/*
 * Copyright 2017-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.salespointframework.accountancy;

import java.time.Duration;
import java.time.temporal.TemporalAmount;
import java.util.Map;
import java.util.Optional;

import javax.money.MonetaryAmount;

import org.salespointframework.accountancy.AccountancyEntry.AccountancyEntryIdentifier;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.time.Interval;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

/**
 * The {@code Accountancy} interface is implemented by classes offering a basic accounting service. Generally, an
 * {@code Accountancy} aggregates objects of the type {@link AccountancyEntry} and subclasses thereof. Additionally, an
 * {@code Accountancy} offers methods for querying of entries and financial statistics.
 *
 * @author Hannes Weisbach
 * @author Oliver Gierke
 */
@Service
public interface Accountancy {

	/**
	 * Adds a new {@link AccountancyEntry} to this {@code Accountancy}. The {@link AccountancyEntry}'s date will be set to
	 * the value returned by {@link BusinessTime#getTime()} in case it is not set already.
	 *
	 * @param accountancyEntry entry to be added to the accountancy, must not be {@literal null}.
	 * @return the added {@link AccountancyEntry}.
	 */
	<T extends AccountancyEntry> T add(T accountancyEntry);

	/**
	 * Returns all {@link AccountancyEntry}s of the specified type {@code clazz} and all sub-types, previously added to
	 * the accountancy. If no entries of the specified type exist, an empty {@code Iterable} is returned.
	 *
	 * @param clazz Class object corresponding to the type of the entries to be returned, has to implement
	 *          {@link AccountancyEntry}
	 * @return a {@link Streamable} containing all entries of type clazz
	 */
	Streamable<AccountancyEntry> findAll();

	/**
	 * Returns the {@link AccountancyEntry} of type {@code clazz} and all sub-types, identified by
	 * {@link AccountancyEntryIdentifier}. {@literal null} is returned, if no entry with the given identifier exists.
	 *
	 * @param clazz type of the entry to be returned; has to implement {@link AccountancyEntry}
	 * @param accountancyEntryIdentifier the {@link AccountancyEntryIdentifier} of the entry to be returned
	 * @return the {@link AccountancyEntry} or sub type thereof of type {@code clazz} which has the identifier
	 *         {@link AccountancyEntryIdentifier}
	 */
	Optional<AccountancyEntry> get(AccountancyEntryIdentifier accountancyEntryIdentifier);

	/**
	 * Returns all {@link AccountancyEntry}s that were created in the given {@link Interval}. So every entry with an time
	 * stamp <= {@code to} and >= {@code from} is returned. If no entries within the specified time span exist, or no
	 * entries of the specified class type exist, an empty {@link Streamable} is returned.
	 *
	 * @param interval the {@link Interval} we want to find {@link AccountancyEntry} instances for.
	 * @return a {@link Streamable} containing all entries in the given {@link Interval}.
	 */
	Streamable<AccountancyEntry> find(Interval interval);

	/**
	 * Returns all {@link AccountancyEntry}s which have their {@code date} within the given {@link Interval}. The
	 * {@link Interval} is divided into parts of length of the given {@link Duration}. According to their respective date,
	 * entries are sorted in exactly one of the time intervals. The last time interval may be shorter than the given
	 * {@link Duration}. Returned is a map, having a {@link Interval} objects as its key, and an {@link Streamable} as
	 * value. The {@link Streamable} contains all entries of the specific type with its date in the interval specified by
	 * the key.
	 *
	 * @param <T> common super type of all entries returned
	 * @param clazz class type of the requested entries; has to implement {@link AccountancyEntry}
	 * @param from all returned entries will have a time stamp after {@code from}
	 * @param to all returned entries will have a time stamp before {@code to}
	 * @param duration length of the time intervals, the period between {@code from} and {@code to} is divided
	 * @return a map, with intervals of {@code period} length between {@code from} and {@code to} as keys, and as value an
	 *         <code>Iterable</code> containing all entries within the key- <code>Interval</code>
	 */
	Map<Interval, Streamable<AccountancyEntry>> find(Interval interval, TemporalAmount duration);

	/**
	 * Returns the sum of the field {@code amount} of all {@link AccountancyEntry}s which have their {@code date} within
	 * (including) {@code from} and {@code to}. <br />
	 * The time between {@code from} and {@code to} is divided into parts of {@code period} length. According to their
	 * time stamp, entries are sorted in exactly one of the time intervals. The last time interval may be shorter than
	 * {@code period}.<br />
	 * Returned is a map, having a {@link Interval} objects as its key, and a {@link MonetaryAmount} as value. The
	 * {@link MonetaryAmount} object's value is equal to the sum of all entries' {@code amount}-field, with a date within
	 * the key- {@link Interval}. If within an interval no entries of the specified type exist, a {@link MonetaryAmount}
	 * object with a value of zero is added as value for that interval.
	 *
	 * @param from all returned entries will have a time stamp after {@code from}.
	 * @param to all returned entries will have a time stamp before {@code to}.
	 * @param duration length of the time intervals, the period between {@code from} and {@code to} is divided.
	 * @return a {@link Map}, with intervals of {@code period} length between {@code from} and {@code to} as keys, and as
	 *         value a {@link MonetaryAmount} object, equal to the sum of the amount fields of all entries within the key-
	 *         {@link Interval}.
	 */
	Map<Interval, MonetaryAmount> salesVolume(Interval interval, TemporalAmount duration);
}
