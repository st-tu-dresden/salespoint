package org.salespointframework.core.accountancy;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.money.Money;
import org.salespointframework.util.Iterables;
import org.salespointframework.util.Objects;

/**
 * This class represents an accountancy. An accountancy consists of
 * <code>AccountancyEntries</code>.
 * 
 * @author hannesweisbach
 * @author Thomas Dedek
 * 
 */
public final class PersistentAccountancy implements Serializable, Accountancy {
	private EntityManagerFactory emf;
	private static final long serialVersionUID = 1L;

	/**
	 * Create a new <code>Accountancy</code>. For persistence, an
	 * <code>EntityManager</code> is created internally as required. The
	 * <code>Database.INSTANCE</code> has to be initialized first.
	 * 
	 * @param entityManager
	 *            <code>EntityManager</code> which is used for this Accountancy.
	 */
	public PersistentAccountancy() {
		this.emf = Database.INSTANCE.getEntityManagerFactory();
	}

	/* (non-Javadoc)
	 * @see org.salespointframework.core.accountancy.IAccountancy#addEntry(org.salespointframework.core.accountancy.AbstractAccountancyEntry)
	 */
	@Override
	public void addEntry(AbstractAccountancyEntry accountancyEntry) {
		EntityManager em = emf.createEntityManager();
		Objects.requireNonNull(accountancyEntry, "accountancyEntry");
		em.getTransaction().begin();
		em.persist(accountancyEntry);
		em.getTransaction().commit();
	}

	/* (non-Javadoc)
	 * @see org.salespointframework.core.accountancy.IAccountancy#addEntries(java.lang.Iterable)
	 */
	@Override
	public void addEntries(Iterable<AbstractAccountancyEntry> accountancyEntries) {
		EntityManager em = emf.createEntityManager();
		Objects.requireNonNull(accountancyEntries, "accountancyEntries");
		em.getTransaction().begin();
		for (AbstractAccountancyEntry e : accountancyEntries)
			em.persist(e);
		em.getTransaction().commit();
	}

	/* (non-Javadoc)
	 * @see org.salespointframework.core.accountancy.IAccountancy#getEntries(org.joda.time.DateTime, org.joda.time.DateTime)
	 */
	@Override
	public Iterable<AbstractAccountancyEntry> getEntries(DateTime from, DateTime to) {
		Objects.requireNonNull(from, "from");
		Objects.requireNonNull(to, "to");

		EntityManager em = emf.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<AbstractAccountancyEntry> q = cb
				.createQuery(AbstractAccountancyEntry.class);
		Root<AbstractAccountancyEntry> r = q.from(AbstractAccountancyEntry.class);
		Predicate p = cb.between(r.get(AbstractAccountancyEntry_.timeStamp), from.toDate(), to.toDate());
		q.where(p);
		TypedQuery<AbstractAccountancyEntry> tq = em.createQuery(q);

		return Iterables.from(tq.getResultList());
	}

	/* (non-Javadoc)
	 * @see org.salespointframework.core.accountancy.IAccountancy#getEntries(java.lang.Class)
	 */
	@Override
	public <T extends AbstractAccountancyEntry> Iterable<T> getEntries(Class<T> clazz) {
		Objects.requireNonNull(clazz, "clazz");

		EntityManager em = emf.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(clazz);
		Root<T> r = q.from(clazz);
		q.where(r.type().in(clazz));
		TypedQuery<T> tq = em.createQuery(q);

		return Iterables.from(tq.getResultList());
	}

	/* (non-Javadoc)
	 * @see org.salespointframework.core.accountancy.IAccountancy#getEntries(java.lang.Class, org.joda.time.DateTime, org.joda.time.DateTime)
	 */
	@Override
	public <T extends AbstractAccountancyEntry> Iterable<T> getEntries(Class<T> clazz,
			DateTime from, DateTime to) {
		Objects.requireNonNull(from, "from");
		Objects.requireNonNull(to, "to");
		Objects.requireNonNull(clazz, "clazz");

		EntityManager em = emf.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(clazz);
		Root<T> entry = q.from(clazz);

		Predicate p = entry.type().in(clazz);
		Predicate p1 = cb.between(entry.get(AbstractAccountancyEntry_.timeStamp),
				from.toDate(), to.toDate());

		q.where(cb.and(p, p1));
		TypedQuery<T> tq = em.createQuery(q);
		
		return Iterables.from(tq.getResultList());
	}

	@Override
	public <T extends AbstractAccountancyEntry> Map<Interval, Iterable<T>> getEntries(
			Class<T> clazz, DateTime from, DateTime to, Period period) {
		DateTime nextStep;
		HashMap<Interval, Iterable<T>> entries = new HashMap<Interval, Iterable<T>>();
		
		for(; from.isBefore(to.minus(period)); from = from.plus(period)) {
			nextStep = from.plus(period);
			entries.put(new Interval(from, nextStep), getEntries(clazz, from, nextStep));
		}
		/* Remove last interval from loop, to save the test for the last interval in every
		 * iteration. But it's java, it not like you're gonna notice the speedup, hahaha.
		 * BTW: the cake is a lie, hahaha.
		 */
		entries.put(new Interval(from, to), getEntries(clazz, from, to));
		return entries;
	}

	@Override
	public <T extends AbstractAccountancyEntry> Map<Interval, Money> getSalesVolume(
			Class<T> clazz, DateTime from, DateTime to, Period period) {
		Money total;
		Map<Interval, Money> sales = new HashMap<Interval, Money>();
		Map<Interval, Iterable<T>> entries = getEntries(clazz, from, to, period);
		
		for(Entry<Interval, Iterable<T>> e : entries.entrySet()) {
			total = Money.ZERO;
			for(T t : e.getValue()) {
				total = total.add_(t.getValue());
			}
			sales.put(e.getKey(), total);
		}
	
		return sales;
	}

}
