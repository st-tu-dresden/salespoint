package org.salespointframework.core.accountancy;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
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
 * @author Hannes Weisbach
 * @author Thomas Dedek
 * 
 */
public final class PersistentAccountancy implements Accountancy<AbstractAccountancyEntry>
{
	/**
	 * <code>EntityManager</code> which is used for this Accountancy. The
	 * <code>Database.INSTANCE</code> has to be initialized first.
	 */
	private EntityManagerFactory emf = Database.INSTANCE.getEntityManagerFactory();

	/**
	 * Create a new <code>Accountancy</code>.
	 */
	public PersistentAccountancy()
	{

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void add(AbstractAccountancyEntry accountancyEntry)
	{
		Objects.requireNonNull(accountancyEntry, "accountancyEntry");
		EntityManager em = emf.createEntityManager();
		em.persist(accountancyEntry);
		beginCommit(em);
	}

	/**
	 * Adds multiple <code>AccountancyEntry</code>s to this
	 * <code>Accountancy</code> and persists them to underlying database. Once
	 * an <code>AccountancyEntry</code> has been added to the persistence layer,
	 * it cannot be modified again.
	 * 
	 * @param accountancyEntries
	 *            an {@link Iterable} of all <code>AccountancyEntry</code>s
	 *            which should be added to the <code>Accountancy</code>
	 */
	public final void addAll(Iterable<? extends AbstractAccountancyEntry> accountancyEntries)
	{
		Objects.requireNonNull(accountancyEntries, "accountancyEntries");
		EntityManager em = emf.createEntityManager();
		for (AccountancyEntry e : accountancyEntries)
		{
			em.persist(e);
		}
		beginCommit(em);

	}

	@Override
	public final AbstractAccountancyEntry get(Class<AbstractAccountancyEntry> clazz, AccountancyEntryIdentifier accountancyEntryIdentifier)
	{
		Objects.requireNonNull(clazz, "clazz");
		Objects.requireNonNull(accountancyEntryIdentifier, "accountancyEntryIdentifier");
		EntityManager em = emf.createEntityManager();
		return em.find(clazz, accountancyEntryIdentifier);
	}

	// TODO convencience
	// braucht man die wirklich ohne .clas, alle anderen im framework (Catalog,
	// Inventory, Order, Calendar?) sind mit class
	/**
	 * Returns all <code>AccountancyEntry</code>s in between the dates
	 * <code>from</code> and <code>to</code>, including from and to. So every
	 * entry with an time stamp <= to and >= from is returned. If no entries
	 * within the specified time span exist, an empty Iterable is returned.
	 * 
	 * @param from
	 *            time stamp denoting the start of the requested time period
	 * @param to
	 *            time stamp denoting the end of the requested time period
	 * @return an unmodifiable Iterable containing all entries between from and
	 *         to
	 */
	public final Iterable<AbstractAccountancyEntry> find(DateTime from, DateTime to)
	{
		Objects.requireNonNull(from, "from");
		Objects.requireNonNull(to, "to");

		EntityManager em = emf.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<AbstractAccountancyEntry> q = cb.createQuery(AbstractAccountancyEntry.class);
		Root<AbstractAccountancyEntry> r = q.from(AbstractAccountancyEntry.class);
		Predicate p = cb.between(r.get(AbstractAccountancyEntry_.dateCreated), from.toDate(), to.toDate());
		q.where(p);
		TypedQuery<AbstractAccountancyEntry> tq = em.createQuery(q);

		return Iterables.from(tq.getResultList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final <T extends AbstractAccountancyEntry> Iterable<T> find(Class<T> clazz)
	{
		Objects.requireNonNull(clazz, "clazz");

		EntityManager em = emf.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(clazz);
		Root<T> r = q.from(clazz);
		q.where(r.type().in(clazz));
		TypedQuery<T> tq = em.createQuery(q);

		return Iterables.from(tq.getResultList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final <T extends AbstractAccountancyEntry> Iterable<T> find(Class<T> clazz, DateTime from, DateTime to)
	{
		Objects.requireNonNull(from, "from");
		Objects.requireNonNull(to, "to");
		Objects.requireNonNull(clazz, "clazz");

		EntityManager em = emf.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(clazz);
		Root<T> entry = q.from(clazz);

		Predicate p = entry.type().in(clazz);
		Predicate p1 = cb.between(entry.get(AbstractAccountancyEntry_.dateCreated), from.toDate(), to.toDate());

		q.where(cb.and(p, p1));
		TypedQuery<T> tq = em.createQuery(q);

		return Iterables.from(tq.getResultList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final <T extends AbstractAccountancyEntry> Map<Interval, Iterable<T>> find(Class<T> clazz, DateTime from, DateTime to, Period period)
	{
		Objects.requireNonNull(clazz, "clazz");
		Objects.requireNonNull(from, "from");
		Objects.requireNonNull(to, "to");
		Objects.requireNonNull(period, "period");

		DateTime nextStep;
		HashMap<Interval, Iterable<T>> entries = new HashMap<Interval, Iterable<T>>();

		for (; from.isBefore(to.minus(period)); from = from.plus(period))
		{
			nextStep = from.plus(period);
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final <T extends AbstractAccountancyEntry> Map<Interval, Money> getSalesVolume(Class<T> clazz, DateTime from, DateTime to, Period period)
	{
		Objects.requireNonNull(clazz, "clazz");
		Objects.requireNonNull(from, "from");
		Objects.requireNonNull(to, "to");
		Objects.requireNonNull(period, "period");

		Money total;
		Map<Interval, Money> sales = new HashMap<Interval, Money>();
		Map<Interval, Iterable<T>> entries = find(clazz, from, to, period);

		for (Entry<Interval, Iterable<T>> e : entries.entrySet())
		{
			total = Money.ZERO;
			for (T t : e.getValue())
			{
				total = total.add_(t.getValue());
			}
			sales.put(e.getKey(), total);
		}

		return sales;
	}

	private final void beginCommit(EntityManager entityManager)
	{
		entityManager.getTransaction().begin();
		entityManager.getTransaction().commit();
	}
}
