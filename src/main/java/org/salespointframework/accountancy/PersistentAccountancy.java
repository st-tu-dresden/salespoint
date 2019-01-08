/*
 * Copyright 2017-2019 the original author or authors.
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

import static java.util.function.Function.*;
import static java.util.stream.Collectors.*;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.temporal.TemporalAmount;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javax.money.MonetaryAmount;

import org.javamoney.moneta.Money;
import org.salespointframework.core.Currencies;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.time.Interval;
import org.salespointframework.time.Intervals;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * This class represents an accountancy. An accountancy aggregates of {@link AccountancyEntry}s.
 * 
 * @author Hannes Weisbach
 * @author Thomas Dedek
 * @author Oliver Gierke
 */
@Service
@Transactional
@RequiredArgsConstructor
class PersistentAccountancy implements Accountancy {

	private final @NonNull BusinessTime businessTime;
	private final @NonNull AccountancyEntryRepository repository;

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.accountancy.Accountancy#add(org.salespointframework.accountancy.AccountancyEntry)
	 */
	@Override
	public final <T extends AccountancyEntry> T add(T accountancyEntry) {

		Assert.notNull(accountancyEntry, "Accountancy entry must not be null!");

		if (!accountancyEntry.hasDate()) {
			accountancyEntry.setDate(businessTime.getTime());
		}

		return repository.save(accountancyEntry);
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.accountancy.Accountancy#get(org.salespointframework.accountancy.AccountancyEntryIdentifier)
	 */
	@Override
	public final Optional<AccountancyEntry> get(AccountancyEntryIdentifier accountancyEntryIdentifier) {

		Assert.notNull(accountancyEntryIdentifier, "Account entry identifier must not be null!");
		return repository.findById(accountancyEntryIdentifier);
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.accountancy.Accountancy#findAll()
	 */
	@Override
	public final Streamable<AccountancyEntry> findAll() {
		return Streamable.of(repository.findAll());
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.accountancy.Accountancy#find(java.time.LocalDateTime, java.time.LocalDateTime)
	 */
	@Override
	public final Streamable<AccountancyEntry> find(Interval interval) {

		Assert.notNull(interval, "Interval must not be null!");

		return repository.findByDateIn(interval);
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.accountancy.Accountancy#find(org.salespointframework.time.Interval, java.time.temporal.TemporalAmount)
	 */
	@Override
	public final Map<Interval, Streamable<AccountancyEntry>> find(Interval interval, TemporalAmount duration) {

		Assert.notNull(interval, "Interval must not be null");
		Assert.notNull(duration, "TemporalAmount must not be null");

		return Intervals.divide(interval, duration).stream() //
				.collect(toMap(identity(), this::find));
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.accountancy.Accountancy#salesVolume(org.salespointframework.time.Interval, java.time.temporal.TemporalAmount)
	 */
	@Override
	public final Map<Interval, MonetaryAmount> salesVolume(Interval interval, TemporalAmount duration) {

		Assert.notNull(interval, "Interval must not be null");
		Assert.notNull(duration, "TemporalAmount must not be null");

		return find(interval, duration).entrySet().stream().//
				collect(toMap(Entry::getKey, entry -> entry.getValue().stream().//
						map(AccountancyEntry::getValue).//
						reduce(Money.of(0, Currencies.EURO), MonetaryAmount::add)));
	}
}
