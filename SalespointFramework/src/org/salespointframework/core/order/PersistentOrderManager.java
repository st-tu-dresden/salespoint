package org.salespointframework.core.order;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.joda.time.DateTime;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.user.UserIdentifier;
import org.salespointframework.util.ArgumentNullException;
import org.salespointframework.util.Iterables;
import org.salespointframework.util.Objects;

/**
 * 
 * @author Thomas Dedek
 * @author Paul Henke
 * 
 */
public class PersistentOrderManager implements OrderManager<PersistentOrder, PersistentOrderLine>
{
	private final EntityManagerFactory emf = Database.INSTANCE.getEntityManagerFactory();

	/**
	 * creates a new PersistentOrderManager
	 */
	public PersistentOrderManager() {
		
	}
	
	@Override
	public void add(PersistentOrder order)
	{
		Objects.requireNonNull(order, "order");
		EntityManager em = emf.createEntityManager();
		em.persist(order);
		beginCommit(em);
	}
	
	/**
	 * Adds multiple {@link PersistentOrder}s to this PersistentOrderManager
	 * 
	 * @param orders
	 *            an {@link Iterable} of {@link PersistentOrders}s to be added
	 * 
	 * @throws ArgumentNullException if orders is null
	 */
	public void addAll(Iterable<? extends PersistentOrder> orders) {
		Objects.requireNonNull(orders, "orders");
		EntityManager em = emf.createEntityManager();
		for(PersistentOrder order : orders) {
			em.persist(order);
		}
		beginCommit(em);
	}
	

	@Override
	public final <T extends PersistentOrder> T get(Class<T> clazz, OrderIdentifier orderIdentifier)
	{
		Objects.requireNonNull(orderIdentifier, "orderIdentifier");
		EntityManager em = emf.createEntityManager();
		return em.find(clazz, orderIdentifier);
	}

	@Override
	public final boolean contains(OrderIdentifier orderIdentifier)
	{
		Objects.requireNonNull(orderIdentifier, "orderIdentifier");
		EntityManager em = emf.createEntityManager();
		return em.find(PersistentOrder.class, orderIdentifier) != null;
	}

	@Override
	public final <T extends PersistentOrder> Iterable<T> find(Class<T> clazz, DateTime from, DateTime to)
	{
		Objects.requireNonNull(from, "from");
		Objects.requireNonNull(to, "to");

		EntityManager em = emf.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(clazz);
		Root<T> entry = cq.from(clazz);
		cq.where(cb.between(entry.get(PersistentOrder_.dateCreated), from.toDate(), to.toDate()));
		TypedQuery<T> tq = em.createQuery(cq);

		return Iterables.of(tq.getResultList());
	}

	@Override
	public final <T extends PersistentOrder> Iterable<T> find(Class<T> clazz, OrderStatus orderStatus)
	{
		Objects.requireNonNull(orderStatus, "orderStatus");

		EntityManager em = emf.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(clazz);
		Root<T> entry = cq.from(clazz);
		cq.where(cb.equal(entry.get(PersistentOrder_.orderStatus), orderStatus));
		TypedQuery<T> tq = em.createQuery(cq);

		return Iterables.of(tq.getResultList());
	}

	@Override
	public final <T extends PersistentOrder> Iterable<T> find(Class<T> clazz, UserIdentifier userIdentifier)
	{
		Objects.requireNonNull(userIdentifier, "userIdentifier");

		EntityManager em = emf.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(clazz);
		Root<T> entry = cq.from(clazz);
		cq.where(cb.equal(entry.get(PersistentOrder_.userIdentifier), userIdentifier));
		TypedQuery<T> tq = em.createQuery(cq);

		return Iterables.of(tq.getResultList());
	}

	@Override
	public final <T extends PersistentOrder> Iterable<T> find(Class<T> clazz, UserIdentifier userIdentifier, DateTime from, DateTime to)
	{
		Objects.requireNonNull(userIdentifier, "userIdentifier");
		Objects.requireNonNull(from, "from");
		Objects.requireNonNull(to, "to");

		EntityManager em = emf.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(clazz);
		Root<T> entry = cq.from(clazz);
		Predicate p1 = cb.equal(entry.get(PersistentOrder_.userIdentifier), userIdentifier);
		Predicate p2 = cb.between(entry.get(PersistentOrder_.dateCreated), from.toDate(), to.toDate());
		cq.where(p1, p2);
		TypedQuery<T> tq = em.createQuery(cq);

		return Iterables.of(tq.getResultList());
	}

	/**
	 * Updates and persists an existing {@link PersistentOrder} to the PersistentOrderManager and the Database
	 * @param order the {@link PersistentOrder} to be updated
	 * @throws ArgumentNullException if order is null
	 */
	public final void update(PersistentOrder order)
	{
		Objects.requireNonNull(order, "order");
		EntityManager em = emf.createEntityManager();
		em.merge(order);
		beginCommit(em);
	}

	private final void beginCommit(EntityManager entityManager)
	{
		entityManager.getTransaction().begin();
		entityManager.getTransaction().commit();
	}
}
