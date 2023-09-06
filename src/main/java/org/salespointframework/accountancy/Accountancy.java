/*
 * Copyright 2017-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.salespointframework.accountancy;

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
 * An application service to manage {@link AccountancyEntry}s and to create basic financial statistics.
 *
 * @author Hannes Weisbach
 * @author Oliver Drotbohm
 * @author Martin Morgenstern
 */
@Service
public interface Accountancy {

	/**
	 * Adds a new {@link AccountancyEntry} to this {@code Accountancy}. The {@link AccountancyEntry}'s date will be set
	 * to the value returned by {@link BusinessTime#getTime()} in case it is not set already.
	 *
	 * @param accountancyEntry entry to be added to the accountancy, must not be {@literal null}.
	 * @return the added {@link AccountancyEntry}.
	 */
	<T extends AccountancyEntry> T add(T accountancyEntry);

	/**
	 * Returns all {@link AccountancyEntry}s previously added to the accountancy.
	 *
	 * @return a {@link Streamable} containing all entries, or an empty {@link Streamable}.
	 */
	Streamable<AccountancyEntry> findAll();

	/**
	 * Returns the {@link AccountancyEntry} identified by the given {@link AccountancyEntryIdentifier}, if it exists.
	 *
	 * @param accountancyEntryIdentifier the {@link AccountancyEntryIdentifier} of the entry to be returned, must not be
	 *            {@literal null}.
	 * @return an {@code Optional} with a matching {@link AccountancyEntry}, or an empty {@code Optional} if no match
	 *         was found.
	 */
	Optional<AccountancyEntry> get(AccountancyEntryIdentifier accountancyEntryIdentifier);

	/**
	 * Returns all {@link AccountancyEntry}s that have a {@code date} within the given {@link Interval}.
	 *
	 * @param interval the interval within which we want to find {@link AccountancyEntry}s, must not be {@literal null}.
	 * @return a {@link Streamable} containing all entries in the given interval, or an empty {@link Streamable}.
	 */
	Streamable<AccountancyEntry> find(Interval interval);

	/**
	 * Returns all {@link AccountancyEntry}s within the given interval, grouped by sub-intervals of the given duration.
	 * These sub-intervals are used as the keys of the returned {@link Map}. Note that the last sub-interval may be
	 * shorter than the given {@code duration}.
	 * 
	 * @param interval the interval within which we want to find {@link AccountancyEntry}s, must not be {@literal null}.
	 * @param duration the duration of the sub-intervals, must not be {@literal null}.
	 * @return a {@link Map} containing a {@link Streamable} with zero or more {@link AccountancyEntry}s for each
	 *         sub-interval.
	 */
	Map<Interval, Streamable<AccountancyEntry>> find(Interval interval, TemporalAmount duration);

	/**
	 * Computes the sales volume, i.e., the sum of {@link AccountancyEntry#getValue()}, for all
	 * {@link AccountancyEntry}s within the given interval, grouped by sub-intervals of the given duration. If a
	 * sub-interval doesn't contain an {@link AccountancyEntry}, its sales volume is zero. Note that the last
	 * sub-interval may be shorter than the given {@code duration}.
	 * 
	 * @param interval the interval within which we want to find {@link AccountancyEntry}s, must not be {@literal null}.
	 * @param duration the duration of the sub-intervals that are used to group the summation, must not be
	 *            {@literal null}.
	 * @return a {@link Map} containing the summated {@link MonetaryAmount} for each sub-interval.
	 */
	Map<Interval, MonetaryAmount> salesVolume(Interval interval, TemporalAmount duration);
}
