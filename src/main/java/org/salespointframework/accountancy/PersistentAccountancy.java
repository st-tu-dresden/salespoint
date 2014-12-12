package org.salespointframework.accountancy;

import static java.util.function.Function.*;
import static java.util.stream.Collectors.*;
import static java.util.stream.StreamSupport.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.time.Interval;
import org.salespointframework.time.Intervals;
import org.springframework.beans.factory.annotation.Autowired;
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
class PersistentAccountancy<T extends AccountancyEntry> implements Accountancy<T> {

	private final BusinessTime businessTime;
	private final AccountancyEntryRepository<T> repository;

	/**
	 * Creates a new {@link PersistentAccountancy} with the given {@link BusinessTime} and
	 * {@link AccountancyEntryRepository}.
	 * 
	 * @param businessTime must not be {@literal null}.
	 * @param repository must not be {@literal null}.
	 */
	@Autowired
	public PersistentAccountancy(BusinessTime businessTime, AccountancyEntryRepository<T> repository) {

		Assert.notNull(businessTime, "BusinessTime must not be null!");
		Assert.notNull(repository, "AccountancyEntryRepository must not be null!");

		this.businessTime = businessTime;
		this.repository = repository;
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.accountancy.Accountancy#add(org.salespointframework.accountancy.AccountancyEntry)
	 */
	@Override
	public final T add(T accountancyEntry) {

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
	public final Optional<T> get(AccountancyEntryIdentifier accountancyEntryIdentifier) {

		Assert.notNull(accountancyEntryIdentifier, "Account entry identifier must not be null!");
		return repository.findOne(accountancyEntryIdentifier);
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.accountancy.Accountancy#findAll()
	 */
	@Override
	public final Iterable<T> findAll() {
		return repository.findAll();
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.accountancy.Accountancy#find(java.time.LocalDateTime, java.time.LocalDateTime)
	 */
	@Override
	public final Iterable<T> find(LocalDateTime from, LocalDateTime to) {

		Assert.notNull(from, "from must not be null");
		Assert.notNull(to, "to must not be null");

		return repository.findByDateBetween(from, to);
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.accountancy.Accountancy#find(java.time.LocalDateTime, java.time.LocalDateTime, java.time.Duration)
	 */
	@Override
	public final Map<Interval, Iterable<T>> find(LocalDateTime from, LocalDateTime to, Duration duration) {

		Assert.notNull(from, "from must not be null");
		Assert.notNull(to, "to must not be null");
		Assert.notNull(duration, "period must not be null");

		return new Intervals(from, to, duration).//
				stream().parallel().//
				collect(toMap(identity(), i -> find(i.getStart(), i.getEnd())));
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.accountancy.Accountancy#salesVolume(java.time.LocalDateTime, java.time.LocalDateTime, java.time.Duration)
	 */
	@Override
	public final Map<Interval, Money> salesVolume(LocalDateTime from, LocalDateTime to, Duration period) {

		Assert.notNull(from, "from must not be null");
		Assert.notNull(to, "to must not be null");
		Assert.notNull(period, "period must not be null");

		Map<Interval, Money> sales = new HashMap<Interval, Money>();

		for (Entry<Interval, Iterable<T>> e : find(from, to, period).entrySet()) {

			sales.put(e.getKey(), stream(e.getValue().spliterator(), false).//
					map(AccountancyEntry::getValue).//
					reduce(Money.zero(CurrencyUnit.EUR), Money::plus));
		}

		return sales;
	}
}
