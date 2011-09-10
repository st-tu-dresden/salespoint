package org.salespointframework.core.order;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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
		OrderEntry o = em.find(OrderEntry.class, orderIdentifier);
		em.getTransaction().commit();
		return o;
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
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<OrderEntry> q = cb
				.createQuery(OrderEntry.class);
		Root<OrderEntry> r = q.from(OrderEntry.class);
		Predicate p = cb.between(r.get(OrderEntry_.dateCreated), from.toDate(), to.toDate());
		q.where(p);
		TypedQuery<OrderEntry> tq = em.createQuery(q);
		
		em.getTransaction().commit();

		return Iterables.from(tq.getResultList());
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
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<OrderEntry> q = cb
				.createQuery(OrderEntry.class);
		Root<OrderEntry> r = q.from(OrderEntry.class);
		Predicate p = cb.equal(r.get(OrderEntry_.status), status);
		q.where(p);
		TypedQuery<OrderEntry> tq = em.createQuery(q);
		
		em.getTransaction().commit();

		return Iterables.from(tq.getResultList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.salespointframework.core.order.IOrderManager#removeOrder(org.
	 * salespointframework.core.order.OrderIdentifier)
	 */
    @Override
    public OrderEntry removeOrder(OrderIdentifier orderIdentifier) {
        Objects.requireNonNull(orderIdentifier, "orderIdentifier");

        OrderEntry orderEntry = em.find(OrderEntry.class, orderIdentifier);

        if (orderEntry != null) {
            em.getTransaction().begin();
            em.remove(orderEntry);
            em.getTransaction().commit();
        }
        return orderEntry;
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.salespointframework.core.order.IOrderManager#updateOrder(org.
	 * salespointframework.core.order.OrderEntry)
	 */
	@Override
	public void updateOrder(OrderEntry orderEntry) {
		Objects.requireNonNull(orderEntry, "orderEntry");

		em.getTransaction().begin();
		
		OrderEntry oe = em.find(OrderEntry.class,
				orderEntry.getOrderIdentifier());

		/* TODO: srsly? silently failing?! */
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
	/* TODO: add extensive testing for all circumstances:
	 * 1) ui has no order(s) at all
	 * 2) ui has order(s), but only (a) closed one(s)
	 * 3) ui has open order(s) but not proccesing ones
	 * 4) ui has processing order(s), but not open one(s)
	 * 5) ui has open and processing order(s)
	 * 6) 3, 4, and 5 with order(s) in non Open/processing states
	 * (non-Javadoc)
	 * @see org.salespointframework.core.order.OrderManager#hasOpenOrders(org.salespointframework.core.users.UserIdentifier)
	 */
	@Override
	public boolean hasOpenOrders(UserIdentifier userIdentifier) {
		
		Objects.requireNonNull(userIdentifier, "userIdentifier");
		// this should not be necessary
		//em.getTransaction().begin();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<OrderEntry> q = cb
				.createQuery(OrderEntry.class);
		Root<OrderEntry> r = q.from(OrderEntry.class);
		Predicate p_ui = cb.equal(r.get(OrderEntry_.userIdentifier), userIdentifier);
		Predicate p_statusProc = cb.equal(r.get(OrderEntry_.status), OrderStatus.PROCESSING);
		Predicate p_statusOpen = cb.equal(r.get(OrderEntry_.status), OrderStatus.OPEN);
		/* status = (PROCESSING | OPEN) */
		Predicate status_or = cb.or(p_statusProc, p_statusOpen);
		/* ui & (PROCESSING | OPEN) */
		Predicate status_and_ui = cb.and(p_ui, status_or);
		q.where(status_and_ui);
		TypedQuery<OrderEntry> tq = em.createQuery(q);
		
		List<OrderEntry> oeList = tq.getResultList();
		
		//em.getTransaction().commit();
		
		return !oeList.isEmpty();
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

		//em.getTransaction().begin();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<OrderEntry> q = cb
				.createQuery(OrderEntry.class);
		Root<OrderEntry> r = q.from(OrderEntry.class);
		Predicate p = cb.equal(r.get(OrderEntry_.userIdentifier), userIdentifier);
		q.where(p);
		TypedQuery<OrderEntry> tq = em.createQuery(q);

		//em.getTransaction().commit();
		
		return Iterables.from(tq.getResultList());
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

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<OrderEntry> q = cb
				.createQuery(OrderEntry.class);
		Root<OrderEntry> r = q.from(OrderEntry.class);
		Predicate p_ui = cb.equal(r.get(OrderEntry_.userIdentifier), userIdentifier);
		Predicate date = cb.between(r.get(OrderEntry_.dateCreated), from.toDate(), to.toDate());
		Predicate conjunction = cb.and(p_ui, date);
		q.where(conjunction);
		TypedQuery<OrderEntry> tq = em.createQuery(q);

		
		return Iterables.from(tq.getResultList());
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
		boolean ret;
		
		em.getTransaction().begin();
		if(em.find(OrderEntry.class, orderEntry.getOrderIdentifier())!=null)
			ret = true;
		else {
			ret = false;
		}
		em.getTransaction().commit();
		
		return ret;
	}

}
