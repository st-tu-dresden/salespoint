package org.salespointframework.accountancy;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * This class represents an accountancy. An accountancy aggregates of
 * {@link AccountancyEntry}s.
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
	 * Creates a new {@link PersistentAccountancy} with the given {@link BusinessTime} and {@link AccountancyEntryRepository}.
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
		
		Objects.requireNonNull(accountancyEntry, "accountancyEntry must not be null");
		
		if(accountancyEntry.getDate() == null) {
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
		
		Objects.requireNonNull(accountancyEntryIdentifier, "accountancyEntryIdentifier must not be null");
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

		LocalDateTime nextStep;
		Map<Interval, Iterable<T>> entries = new HashMap<Interval, Iterable<T>>();

		for (; from.isBefore(to.minus(duration)); from = from.plus(duration)) {
			nextStep = from.plus(duration);
			entries.put(new Interval(from, nextStep), find(from, nextStep));
		}
		/*
		 * Remove last interval from loop, to save the test for the last
		 * interval in every iteration. But it's java, it not like you're gonna
		 * notice the speedup, hahaha. BTW: the cake is a lie, hahaha.
		 */
		entries.put(new Interval(from, to), find(from, to));
		return entries;
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

		Money total;
		Map<Interval, Money> sales = new HashMap<Interval, Money>();
		Map<Interval, Iterable<T>> entries = find(from, to, period);

		
		// TODO: Use streams?
		for (Entry<Interval, Iterable<T>> e : entries.entrySet()) {
			
			total = Money.zero(CurrencyUnit.EUR);
			
			for (T t : e.getValue()) {
				total = total.plus(t.getValue());
			}
			
			sales.put(e.getKey(), total);
		}

		return sales;
	}
}
