package org.salespointframework.core.order;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.joda.time.DateTime;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.users.UserIdentifier;
import org.salespointframework.util.Iterables;
import org.salespointframework.util.Objects;

/**
 * This class provides functionality to manage orders (<code>OrderEntry</code>)
 * in the system.
 * 
 * @author Thomas Dedek
 * @author hannesweisbach
 * @author Paul Henke
 * 
 */

public class PersistentOrderManager implements OrderManager {
	
	private EntityManager em;

	/**
	 * Create a new <code>OrderManager</code>. For persistence, an
	 * <code>EntityManager</code> is created internal as required. The
	 * <code>Database.INSTANCE</code> has to be initialized first.
	 * 
	 */
	public PersistentOrderManager() {
		this.em = Database.INSTANCE.getEntityManagerFactory().createEntityManager();
	}
	
	/**
	 * Create a new <code>OrderManager</code> using the given EntityManager.
	 * 
	 * @param entityManager
	 *            the entityManager that shall be used for persistence
	 *            functionality
	 */
	public PersistentOrderManager(EntityManager entityManager) {
		this.em = Objects.requireNonNull(entityManager, "entityManager");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.salespointframework.core.order.IOrderManager#addOrder(org.
	 * salespointframework.core.order.OrderEntry)
	 */
	@Override
	public void addOrder(OrderEntry orderEntry) {
		
		Objects.requireNonNull(orderEntry, "orderEntry");
		
		em.getTransaction().begin();
		em.persist(orderEntry);
		em.getTransaction().commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.salespointframework.core.order.IOrderManager#findOrder(org.
	 * salespointframework.core.order.OrderIdentifier)
	 */
	@Override
	public OrderEntry findOrder(OrderIdentifier orderIdentifier) {
		Objects.requireNonNull(orderIdentifier, "orderIdentifier");

		em.getTransaction().begin();
		
		TypedQuery<OrderEntry> q = em
				.createQuery(
						"SELECT DISTINCT o FROM OrderEntry o WHERE o.orderIdentifier = :orderIdentifier",
						OrderEntry.class);
		q.setParameter("orderIdentifier", orderIdentifier);
		
		em.getTransaction().commit();

		return q.getSingleResult();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.salespointframework.core.order.IOrderManager#findOrders(org.joda.
	 * time.DateTime, org.joda.time.DateTime)
	 */
	@Override
	public Iterable<OrderEntry> findOrders(DateTime from, DateTime to) {
		Objects.requireNonNull(from, "from");
		Objects.requireNonNull(to, "to");

		em.getTransaction().begin();
		
		TypedQuery<OrderEntry> q = em
				.createQuery(
						"SELECT DISTINCT o FROM OrderEntry o WHERE o.timeStamp BETWEEN :from and :to",
						OrderEntry.class);
		q.setParameter("from", from.toDate(), TemporalType.TIMESTAMP);
		q.setParameter("to", to.toDate(), TemporalType.TIMESTAMP);
		
		em.getTransaction().commit();

		return Iterables.from(q.getResultList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.salespointframework.core.order.IOrderManager#findOrders(org.
	 * salespointframework.core.order.OrderStatus)
	 */
	@Override
	public Iterable<OrderEntry> findOrders(OrderStatus status) {
		Objects.requireNonNull(status, "status");

		em.getTransaction().begin();
		
		TypedQuery<OrderEntry> q = em.createQuery(
				"SELECT DISTINCT o FROM OrderEntry o WHERE o.status = :status",
				OrderEntry.class);
		q.setParameter("status", status);
		
		em.getTransaction().commit();

		return Iterables.from(q.getResultList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.salespointframework.core.order.IOrderManager#remove(org.
	 * salespointframework.core.order.OrderIdentifier)
	 */
	@Override
	public OrderEntry remove(OrderIdentifier orderIdentifier) {
		Objects.requireNonNull(orderIdentifier, "orderIdentifier");

		em.getTransaction().begin();
		
		OrderEntry orderEntry = em.find(OrderEntry.class, orderIdentifier);
		em.remove(orderEntry);
		
		em.getTransaction().commit();
		return orderEntry;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.salespointframework.core.order.IOrderManager#update(org.
	 * salespointframework.core.order.OrderEntry)
	 */
	@Override
	public void update(OrderEntry orderEntry) {
		Objects.requireNonNull(orderEntry, "orderEntry");

		em.getTransaction().begin();
		
		OrderEntry oe = em.find(OrderEntry.class,
				orderEntry.getOrderIdentifier());

		if (oe != null) {
			em.merge(orderEntry);
		}
		
		em.getTransaction().commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.salespointframework.core.order.IOrderManager#hasOpenOrders(org.salespointframework.core.users.UserIdentifier)
	 */
	@Override
	public boolean hasOpenOrders(UserIdentifier userIdentifier) {
		
		Objects.requireNonNull(userIdentifier, "userIdentifier");

		em.getTransaction().begin();
		
		TypedQuery<OrderEntry> q = em
				.createQuery(
						"SELECT DISTINCT o FROM OrderEntry o WHERE o.userIdentifier = :userIdentifier",
						OrderEntry.class);
		q.setParameter("userIdentifier", userIdentifier);
		List<OrderEntry> resultList = q.getResultList();
		
		em.getTransaction().commit();
		
		for(OrderEntry oe : resultList) {
			if(oe.getOrderStatus() == OrderStatus.PROCESSING ||
					oe.getOrderStatus() == OrderStatus.OPEN ||
					oe.getOrderStatus() == OrderStatus.INITIALIZED) {
				return true;
			}		
		}
		
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.salespointframework.core.order.IOrderManager#findOrders(org.salespointframework.core.users.UserIdentifier)
	 */
	@Override
	public Iterable<OrderEntry> findOrders(UserIdentifier userIdentifier) {
		
		Objects.requireNonNull(userIdentifier, "userIdentifier");

		em.getTransaction().begin();
		
		TypedQuery<OrderEntry> q = em
				.createQuery(
						"SELECT DISTINCT o FROM OrderEntry o WHERE o.userIdentifier = :userIdentifier",
						OrderEntry.class);
		q.setParameter("userIdentifier", userIdentifier);
		
		em.getTransaction().commit();
		
		return Iterables.from(q.getResultList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.salespointframework.core.order.IOrderManager#findOrders(org.salespointframework.core.users.UserIdentifier, org.joda.
	 * time.DateTime, org.joda.time.DateTime)
	 */
	@Override
	public Iterable<OrderEntry> findOrders(UserIdentifier userIdentifier,
			DateTime from, DateTime to) {
		
		Objects.requireNonNull(userIdentifier, "userIdentifier");
		Objects.requireNonNull(from, "from");
		Objects.requireNonNull(to, "to");

		em.getTransaction().begin();
		
		TypedQuery<OrderEntry> q = em
				.createQuery(
						"SELECT DISTINCT o FROM OrderEntry o WHERE o.userIdentifier = :userIdentifier and o.timeStamp BETWEEN :from and :to",
						OrderEntry.class);
		q.setParameter("userIdentifier", userIdentifier);
		q.setParameter("from", from.toDate(), TemporalType.TIMESTAMP);
		q.setParameter("to", to.toDate(), TemporalType.TIMESTAMP);
		
		em.getTransaction().commit();
		
		return Iterables.from(q.getResultList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.salespointframework.core.order.IOrderManager#containsOrderEntry(org.
	 * salespointframework.core.order.OrderEntry)
	 */
	@Override
	public boolean containsOrderEntry(OrderEntry orderEntry) {
		
		Objects.requireNonNull(orderEntry, "orderEntry");
		
		em.getTransaction().begin();
		em.find(OrderEntry.class, orderEntry.getOrderIdentifier());
		boolean ret = em.contains(orderEntry);
		em.getTransaction().commit();
		
		return ret;
	}

}
