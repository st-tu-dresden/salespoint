package org.salespointframework.core.order;

import javax.persistence.EntityManager;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.joda.time.DateTime;
import org.salespointframework.core.accountancy.AccountancyEntry;
import org.salespointframework.util.Objects;
import org.salespointframework.util.SalespointIterable;

public class OrderManager {
	private EntityManager entityManager;

	public OrderManager(EntityManager enitityManager) {
		this.entityManager = Objects.requireNonNull(entityManager,
				"entityManager");
	}

	public void addOrder(Order order) {
		entityManager.getTransaction().begin();
		entityManager.persist(order);
		entityManager.getTransaction().commit();
	}

	public Order findOrder(OrderIdentifier orderIdentifier) {
		Objects.requireNonNull(orderIdentifier, "orderIdentifier");

		TypedQuery<Order> q = entityManager
				.createQuery(
						"SELECT o FROM Order o WHERE o.orderIdentifier == :orderIdentifier",
						Order.class);
		q.setParameter("orderIdentifier", orderIdentifier);

		return q.getSingleResult();
	}

	public Iterable<Order> findOrders(DateTime from, DateTime to) {
		Objects.requireNonNull(from, "from");
		Objects.requireNonNull(to, "to");

		TypedQuery<Order> q = entityManager
				.createQuery(
						"SELECT o FROM Order o WHERE o.timeStamp BETWEEN :from and :to",
						Order.class);
		q.setParameter("from", from.toDate(), TemporalType.TIMESTAMP);
		q.setParameter("to", to.toDate(), TemporalType.TIMESTAMP);

		return SalespointIterable.from(q.getResultList());
	}
}
