package org.salespointframework.core.order;

import javax.persistence.EntityManager;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.joda.time.DateTime;
import org.salespointframework.util.Objects;
import org.salespointframework.util.SalespointIterable;

public class OrderManager {
	private EntityManager entityManager;

	public OrderManager(EntityManager entityManager) {
		this.entityManager = Objects.requireNonNull(entityManager,
				"entityManager");
	}

	public void addOrder(OrderEntry order) {
		entityManager.getTransaction().begin();
		entityManager.persist(order);
		entityManager.getTransaction().commit();
	}

	public OrderEntry findOrder(OrderIdentifier orderIdentifier) {
		Objects.requireNonNull(orderIdentifier, "orderIdentifier");

		TypedQuery<OrderEntry> q = entityManager
				.createQuery(
						"SELECT o FROM OrderEntry o WHERE o.orderIdentifier == :orderIdentifier",
						OrderEntry.class);
		q.setParameter("orderIdentifier", orderIdentifier);

		return q.getSingleResult();
	}

	public Iterable<OrderEntry> findOrders(DateTime from, DateTime to) {
		Objects.requireNonNull(from, "from");
		Objects.requireNonNull(to, "to");

		TypedQuery<OrderEntry> q = entityManager
				.createQuery(
						"SELECT o FROM OrderEntry o WHERE o.timeStamp BETWEEN :from and :to",
						OrderEntry.class);
		q.setParameter("from", from.toDate(), TemporalType.TIMESTAMP);
		q.setParameter("to", to.toDate(), TemporalType.TIMESTAMP);

		return SalespointIterable.from(q.getResultList());
	}

	public void remove(OrderIdentifier orderIdentifier) {
		Objects.requireNonNull(orderIdentifier, "orderIdentifier");
		entityManager.getTransaction().begin();
		entityManager.remove(entityManager.find(OrderEntry.class,
				orderIdentifier.getIdentifier()));
		entityManager.getTransaction().commit();
	}

	public void remove(OrderEntry orderEntry) {
		Objects.requireNonNull(orderEntry, "orderEntry");
		entityManager.getTransaction().begin();
		entityManager.remove(orderEntry);
		entityManager.getTransaction().commit();
	}
}
