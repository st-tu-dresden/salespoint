package org.salespointframework.accountancy;

import static java.util.function.Function.*;
import static java.util.stream.Collectors.*;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javax.money.MonetaryAmount;

import org.javamoney.moneta.Money;
import org.salespointframework.core.Currencies;
import org.salespointframework.core.Streamable;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.time.Interval;
import org.salespointframework.time.Intervals;
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
		return repository.findOne(accountancyEntryIdentifier);
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

		return Streamable.of(repository.findByDateIn(interval));
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.accountancy.Accountancy#find(java.time.LocalDateTime, java.time.LocalDateTime, java.time.Duration)
	 */
	@Override
	public final Map<Interval, Streamable<AccountancyEntry>> find(Interval interval, Duration duration) {

		Assert.notNull(interval, "Interval must not be null");
		Assert.notNull(duration, "Duration must not be null");

		return Intervals.divide(interval, duration).stream().collect(toMap(identity(), this::find));
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.accountancy.Accountancy#salesVolume(java.time.LocalDateTime, java.time.LocalDateTime, java.time.Duration)
	 */
	@Override
	public final Map<Interval, MonetaryAmount> salesVolume(Interval interval, Duration period) {

		Assert.notNull(interval, "Interval must not be null");
		Assert.notNull(period, "period must not be null");

		return find(interval, period).entrySet().stream().//
				collect(toMap(Entry::getKey,
						entry -> entry.getValue().stream().//
								map(AccountancyEntry::getValue).//
								reduce(Money.of(0, Currencies.EURO), MonetaryAmount::add)));
	}
}
