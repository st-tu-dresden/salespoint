package org.salespointframework.core.accountancy;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.salespointframework.core.Interval;
import org.salespointframework.core.time.BusinessTime;
import org.salespointframework.util.Iterables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
class PersistentAccountancy implements Accountancy {
	
	@PersistenceContext
	private EntityManager em;

	private final BusinessTime businessTime;
	
	@Autowired
	public PersistentAccountancy(BusinessTime businessTime) {
		this.businessTime = businessTime;
	}
	
	
	/**
	 * {@inheritDoc}
	 * The new entry is persisted transparently into the underlying database.
	 * Once an {@link PersistentAccountancyEntry} has been written to the database,
	 * it cannot be modified later on or added a second time. Doing so will
	 * result in a runtime exception to be thrown.
	 */

	@Override
	public final void add(AccountancyEntry accountancyEntry) {
		Objects.requireNonNull(accountancyEntry, "accountancyEntry must not be null");
		if(accountancyEntry.getDate() == null) {
			accountancyEntry.setDate(businessTime.getTime());
		}
		em.persist(accountancyEntry);
	}

	@Override
	public final <T extends AccountancyEntry> T get(
			Class<T> clazz,
			AccountancyEntryIdentifier accountancyEntryIdentifier) {
		
		Objects.requireNonNull(clazz, "clazz must not be null");
		Objects.requireNonNull(accountancyEntryIdentifier,
				"accountancyEntryIdentifier must not be null");
		return em.find(clazz, accountancyEntryIdentifier);
	}

	@Override
	public final <T extends AccountancyEntry> Iterable<T> find(
			Class<T> clazz) {
		Objects.requireNonNull(clazz, "clazz must not be null");

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(clazz);
		q.select(q.from(clazz));
		
		TypedQuery<T> tq = em.createQuery(q);

		return Iterables.of(tq.getResultList());
	}

	@Override
	public final <T extends AccountancyEntry> Iterable<T> find(
			Class<T> clazz, LocalDateTime from, LocalDateTime to) {
		Objects.requireNonNull(from, "from must not be null");
		Objects.requireNonNull(to, "to must not be null");
		Objects.requireNonNull(clazz, "clazz must not be null");

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(clazz);
		Root<T> entry = q.from(clazz);

		Predicate p1 = cb.between(entry.get(AccountancyEntry_.date),
				from, to);

		q.where(p1);
		TypedQuery<T> tq = em.createQuery(q);

		return Iterables.of(tq.getResultList());
	}

	@Override
	public final <T extends AccountancyEntry> Map<Interval, Iterable<T>> find(
			Class<T> clazz, LocalDateTime from, LocalDateTime to, Duration duration) {
		
		Objects.requireNonNull(clazz, "clazz must not be null");
		Objects.requireNonNull(from, "from must not be null");
		Objects.requireNonNull(to, "to must not be null");
		Objects.requireNonNull(duration, "period must not be null");

		LocalDateTime nextStep;
		Map<Interval, Iterable<T>> entries = new HashMap<Interval, Iterable<T>>();

		for (; from.isBefore(to.minus(duration)); from = from.plus(duration)) {
			nextStep = from.plus(duration);
			entries.put(new Interval(from, nextStep), find(clazz, from, nextStep));
		}
		/*
		 * Remove last interval from loop, to save the test for the last
		 * interval in every iteration. But it's java, it not like you're gonna
		 * notice the speedup, hahaha. BTW: the cake is a lie, hahaha.
		 */
		entries.put(new Interval(from, to), find(clazz, from, to));
		return entries;
	}

	@Override
	public final <T extends AccountancyEntry> Map<Interval, Money> salesVolume(
			Class<T> clazz, LocalDateTime from, LocalDateTime to, Duration period) {
		
		Objects.requireNonNull(clazz, "clazz must not be null");
		Objects.requireNonNull(from, "from must not be null");
		Objects.requireNonNull(to, "to must not be null");
		Objects.requireNonNull(period, "period must not be null");

		Money total;
		Map<Interval, Money> sales = new HashMap<Interval, Money>();
		Map<Interval, Iterable<T>> entries = find(clazz, from, to, period);

		for (Entry<Interval, Iterable<T>> e : entries.entrySet()) {
			total = Money.zero(CurrencyUnit.EUR);
			for (T t : e.getValue()) {
				total = total.plus(t.getValue());
			}
			sales.put(e.getKey(), total);
		}

		return sales;
	}

	/**
	 * Adds multiple {@link AccountancyEntry}>s to this
	 * {@link Accountancy} and persists them to underlying database. Once
	 * an {@link AccountancyEntry} has been added to the persistence layer,
	 * it cannot be modified or added again. Doing so would result in a runtime
	 * exception.
	 * 
	 * @param accountancyEntries
	 *            an {@link Iterable} of all {@link AccountancyEntry}s
	 *            which should be added to the {@link Accountancy}
	 */
	public final void addAll(
			Iterable<? extends AccountancyEntry> accountancyEntries) {
		Objects.requireNonNull(accountancyEntries, "accountancyEntries must not be null");
		for (AccountancyEntry e : accountancyEntries) {
			em.persist(e);
		}
	}
}
