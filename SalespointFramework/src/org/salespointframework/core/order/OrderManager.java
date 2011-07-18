package org.salespointframework.core.order;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.joda.time.DateTime;
import org.salespointframework.core.database.Database;
import org.salespointframework.util.Objects;
import org.salespointframework.util.SalespointIterable;


/**
 * This class provides functionality to manage orders (<code>OrderEntry</code>) in the system.
 * 
 * @author Thomas Dedek
 * @author hannesweisbach
 * 
 */

public class OrderManager {
	private EntityManagerFactory emf;

	
	/**
	 * Create a new <code>OrderManager</code>. For persistence, an
	 * <code>EntityManager</code> is created internally as required. The
	 * <code>Database.INSTANCE</code> has to be initialized first.
	 * 
	 */
	public OrderManager() {
		this.emf = Database.INSTANCE.getEntityManagerFactory();
	}

	
	/**
	 * Add an <code>OrderEntry</code> to this
	 * <code>OrderManager</code> and persists them to underlying database.
	 * 
	 * @param order The <code>OrderEntry</code> which shall be added.
	 */
	public void addOrder(OrderEntry order) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(order);
		em.getTransaction().commit();
	}

	public OrderEntry findOrder(OrderIdentifier orderIdentifier) {
		Objects.requireNonNull(orderIdentifier, "orderIdentifier");

		EntityManager em = emf.createEntityManager();
		
		TypedQuery<OrderEntry> q = em
				.createQuery(
						"SELECT o FROM OrderEntry o WHERE o.orderIdentifier == :orderIdentifier",
						OrderEntry.class);
		q.setParameter("orderIdentifier", orderIdentifier);

		return q.getSingleResult();
	}

	
	/**
	 * Returns all <code>OrderEntry</code>s in between the dates
	 * <code>from</code> and <code>to</code>, including from and to. So every
	 * entry with an time stamp <= to and >= from is returned. If no entries
	 * within the specified time span exist, an empty Iterable is returned.
	 * 
	 * @param from
	 *            time stamp denoting the start of the requested time period
	 * @param to
	 *            time stamp denoting the end of the requested time period
	 * @return an unmodifiable Iterable containing all OrderEntries between from and
	 *         to
	 */
	public Iterable<OrderEntry> findOrders(DateTime from, DateTime to) {
		Objects.requireNonNull(from, "from");
		Objects.requireNonNull(to, "to");

		EntityManager em = emf.createEntityManager();
		
		TypedQuery<OrderEntry> q = em
				.createQuery(
						"SELECT o FROM OrderEntry o WHERE o.timeStamp BETWEEN :from and :to",
						OrderEntry.class);
		q.setParameter("from", from.toDate(), TemporalType.TIMESTAMP);
		q.setParameter("to", to.toDate(), TemporalType.TIMESTAMP);

		return SalespointIterable.from(q.getResultList());
	}
	
	
	/**
	 * Returns all <code>OrderEntry</code>s having the OrderEntryStatus
	 * <code>status</code>. If no entries
	 * with the specified status exist, an empty Iterable is returned.
	 * 
	 * @param status
	 *            Denoting the OrderEntryStatus on which the OrderEntrys will be requested.
	 * @return an unmodifiable Iterable containing all OrderEntries whith the specified OrderEntryStatus
	 */
	public Iterable<OrderEntry> findOrders(OrderEntryStatus status) {
		Objects.requireNonNull(status, "status");

		EntityManager em = emf.createEntityManager();
		
		TypedQuery<OrderEntry> q = em
				.createQuery(
						"SELECT o FROM OrderEntry o WHERE o.status == :status",
						OrderEntry.class);
		q.setParameter("status", status);

		return SalespointIterable.from(q.getResultList());
	}

	/**
	 * Remove an <code>OrderEntry</code> from this
	 * <code>OrderManager</code> and the underlying database.
	 * 
	 * @param orderIdentifier The Identifier of the <code>OrderEntry</code> which shall be removed.
	 */
	public void remove(OrderIdentifier orderIdentifier) {
		Objects.requireNonNull(orderIdentifier, "orderIdentifier");
		
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();
		em.remove(em.find(OrderEntry.class,
				orderIdentifier.getIdentifier()));
		em.getTransaction().commit();
	}

	
	/**
	 * Remove an <code>OrderEntry</code> from this
	 * <code>OrderManager</code> and the underlying database.
	 * 
	 * @param orderEntry The <code>OrderEntry</code> which shall be removed.
	 */
	public void remove(OrderEntry orderEntry) {
		Objects.requireNonNull(orderEntry, "orderEntry");
		
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();
		em.remove(orderEntry);
		em.getTransaction().commit();
	}
}
