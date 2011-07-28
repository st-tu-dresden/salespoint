package org.salespointframework.core.accountancy;

import java.io.Serializable;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.joda.time.DateTime;
import org.salespointframework.core.database.Database;
import org.salespointframework.util.Objects;
import org.salespointframework.util.SalespointIterable;

/**
 * This class represents an accountancy. An accountancy consists of
 * <code>AccountancyEntries</code>.
 * 
 * @author hannesweisbach
 * @author Thomas Dedek
 * 
 */
public class Accountancy implements Serializable {
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
	public Accountancy() {
		this.emf = Database.INSTANCE.getEntityManagerFactory();
	}

	/**
	 * Adds a new <code>AccountancyEntry</code> to this <code>Accountancy</code>
	 * The new entry is persisted transparently into the underlying database.
	 * Once an <code>AccountancyEntry</code> has been written to the database,
	 * it cannot be added a second time.
	 * 
	 * @param accountancyEntry
	 *            <code>AccountancyEntry</code> which should be added to the
	 *            <code>Accountancy</code>
	 */
	public void addEntry(AbstractAccountancyEntry accountancyEntry) {
		EntityManager em = emf.createEntityManager();
		Objects.requireNonNull(accountancyEntry, "accountancyEntry");
		em.getTransaction().begin();
		em.persist(accountancyEntry);
		em.getTransaction().commit();
	}

	/**
	 * Adds multiple <code>AccountancyEntry</code>s to this
	 * <code>Accountancy</code> and persists them to underlying database. Once
	 * an <code>AccountancyEntry</code> has been added to the persistence layer,
	 * it cannot be modified again.
	 * 
	 * @param accountancyEntries
	 */
	public void addEntries(Iterable<AbstractAccountancyEntry> accountancyEntries) {
		EntityManager em = emf.createEntityManager();
		Objects.requireNonNull(accountancyEntries, "accountancyEntries");
		em.getTransaction().begin();
		for (AbstractAccountancyEntry e : accountancyEntries)
			em.persist(e);
		em.getTransaction().commit();
	}

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

		return SalespointIterable.from(tq.getResultList());
	}

	/**
	 * Returns all <code>AccountancyEntry</code>s of the specified type
	 * <code>class</code>. If no entries of the specified type exist, an empty
	 * Iterable is returned.
	 * 
	 * @param clazz
	 *            The type of the entries.
	 * @return an unmodifiable Iterable containing all entries of type clazz
	 */
	public <T> Iterable<T> getEntries(Class<T> clazz) {
		Objects.requireNonNull(clazz, "clazz");

		EntityManager em = emf.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(clazz);
		Root<T> r = q.from(clazz);
		q.where(r.type().in(clazz));
		TypedQuery<T> tq = em.createQuery(q);

		return SalespointIterable.from(tq.getResultList());
	}

	/**
	 * Returns all <code>AccountancyEntry</code>s in between the dates
	 * <code>from</code> and <code>to</code> of the specified class type
	 * <code>clazz</code>, including from and to. So every entry with an time
	 * stamp <= to and >= from is returned. If no entries within the specified
	 * time span exist, or no entries of the specified class type exist, an
	 * empty Iterable is returned.
	 * 
	 * @param <T>
	 *            type of the requested entries
	 * 
	 * @param from
	 *            time stamp denoting the start of the requested time period
	 * @param to
	 *            time stamp denoting the end of the requested time period
	 * @param clazz
	 *            class type of the requested entries
	 * @return an unmodifiable Iterable containing all entries between from and
	 *         to of type T
	 */
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

		return SalespointIterable.from(tq.getResultList());
	}

}
