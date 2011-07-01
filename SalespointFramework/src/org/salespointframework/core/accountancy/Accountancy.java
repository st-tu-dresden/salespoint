package org.salespointframework.core.accountancy;

import java.io.Serializable;
import javax.persistence.*;

import org.joda.time.DateTime;
import org.salespointframework.core.database.Database;
import org.salespointframework.util.Objects;
import org.salespointframework.util.SalespointIterable;

/**
 * This class represents an accountancy. An accountancy consists of
 * <code>AccountancyEntries</code>.
 * 
 * @author hannesweisbach
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
	public void addEntry(AccountancyEntry accountancyEntry) {
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
	public void addEntries(Iterable<AccountancyEntry> accountancyEntries) {
		EntityManager em = emf.createEntityManager();
		Objects.requireNonNull(accountancyEntries, "accountancyEntries");
		em.getTransaction().begin();
		for (AccountancyEntry e : accountancyEntries)
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
	public Iterable<AccountancyEntry> getEntries(DateTime from, DateTime to) {
		Objects.requireNonNull(from, "from");
		Objects.requireNonNull(to, "to");

		TypedQuery<AccountancyEntry> q = emf
				.createEntityManager()
				.createQuery(
						"SELECT e FROM AccountancyEntry e WHERE e.timeStamp BETWEEN :from and :to",
						AccountancyEntry.class);
		q.setParameter("from", from.toDate(), TemporalType.TIMESTAMP);
		q.setParameter("to", to.toDate(), TemporalType.TIMESTAMP);

		return SalespointIterable.from(q.getResultList());
	}

}
