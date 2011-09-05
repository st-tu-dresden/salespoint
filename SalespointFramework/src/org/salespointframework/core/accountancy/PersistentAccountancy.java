package org.salespointframework.core.accountancy;

import java.io.Serializable;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.joda.time.DateTime;
import org.salespointframework.core.database.Database;
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

}
