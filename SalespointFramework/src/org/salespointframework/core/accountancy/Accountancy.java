package org.salespointframework.core.accountancy;

import java.io.Serializable;
import javax.persistence.*;

import org.joda.time.DateTime;
import org.salespointframework.util.Objects;
import org.salespointframework.util.SalespointIterable;

/**
 * Entity implementation class for Entity: Accountancy
 *
 */

public class Accountancy implements Serializable {
	private EntityManager entityManager;
	private static final long serialVersionUID = 1L;

	public Accountancy(EntityManager entityManager) {
		this.entityManager = Objects.requireNonNull(entityManager, "entityManger");
	}
	
	public void addEntry(AccountancyEntry accountancyEntry) {
		Objects.requireNonNull(accountancyEntry, "accountancyEntry");
		entityManager.getTransaction().begin();
		entityManager.persist(accountancyEntry);
		entityManager.getTransaction().commit();
	}
	
	public void addEntries(Iterable<AccountancyEntry> accountancyEntries) {
		Objects.requireNonNull(accountancyEntries, "accountancyEntries");
		entityManager.getTransaction().begin();
		for(AccountancyEntry e : accountancyEntries)
			entityManager.persist(e);
		entityManager.getTransaction().commit();
	}
	
	public Iterable<AccountancyEntry> getEntries(DateTime from, DateTime to) {
		Objects.requireNonNull(from, "from");
		Objects.requireNonNull(to, "to");
		
		TypedQuery<AccountancyEntry> q = entityManager.createQuery("SELECT e FROM AccountancyEntry e WHERE e.timeStamp BETWEEN :from and :to", AccountancyEntry.class);
		q.setParameter("from", from.toDate(), TemporalType.TIMESTAMP);
		q.setParameter("to", to.toDate(), TemporalType.TIMESTAMP);
		
		return SalespointIterable.from(q.getResultList());
	}
   
}
