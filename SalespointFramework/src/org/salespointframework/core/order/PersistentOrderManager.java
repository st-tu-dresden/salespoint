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
import org.salespointframework.core.users.UserIdentifier;
import org.salespointframework.util.Iterables;
import org.salespointframework.util.Objects;

/**
 * 
 * @author Thomas Dedek
 * @author Paul Henke
 * 
 */
public class PersistentOrderManager implements OrderManager<PersistentOrder, PersistentOrderLine, PersistentChargeLine>
{
	private final EntityManagerFactory emf = Database.INSTANCE.getEntityManagerFactory();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void add(PersistentOrder order)
	{
		Objects.requireNonNull(order, "order");
		EntityManager em = emf.createEntityManager();
		em.persist(order);
		beginCommit(em);
	}

	// TODO REALLY????
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void remove(OrderIdentifier orderIdentifier)
	{
		Objects.requireNonNull(orderIdentifier, "orderIdentifier");
		EntityManager em = emf.createEntityManager();
		Object order = em.find(PersistentOrder.class, orderIdentifier);
		if (order != null)
		{
			em.remove(order);
			beginCommit(em);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final PersistentOrder get(OrderIdentifier orderIdentifier)
	{
		Objects.requireNonNull(orderIdentifier, "orderIdentifier");
		EntityManager em = emf.createEntityManager();
		return em.find(PersistentOrder.class, orderIdentifier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean contains(OrderIdentifier orderIdentifier)
	{
		Objects.requireNonNull(orderIdentifier, "orderIdentifier");
		EntityManager em = emf.createEntityManager();
		return em.find(PersistentOrder.class, orderIdentifier) != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Iterable<PersistentOrder> find(DateTime from, DateTime to)
	{
		Objects.requireNonNull(from, "from");
		Objects.requireNonNull(to, "to");

		EntityManager em = emf.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<PersistentOrder> cq = cb.createQuery(PersistentOrder.class);
		Root<PersistentOrder> entry = cq.from(PersistentOrder.class);
		cq.where(cb.between(entry.get(PersistentOrder_.dateCreated), from.toDate(), to.toDate()));
		TypedQuery<PersistentOrder> tq = em.createQuery(cq);

		return Iterables.from(tq.getResultList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Iterable<PersistentOrder> find(OrderStatus orderStatus)
	{
		Objects.requireNonNull(orderStatus, "orderStatus");

		EntityManager em = emf.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<PersistentOrder> cq = cb.createQuery(PersistentOrder.class);
		Root<PersistentOrder> entry = cq.from(PersistentOrder.class);
		cq.where(cb.equal(entry.get(PersistentOrder_.orderStatus), orderStatus));
		TypedQuery<PersistentOrder> tq = em.createQuery(cq);

		return Iterables.from(tq.getResultList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Iterable<PersistentOrder> find(UserIdentifier userIdentifier)
	{
		Objects.requireNonNull(userIdentifier, "userIdentifier");

		EntityManager em = emf.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<PersistentOrder> cq = cb.createQuery(PersistentOrder.class);
		Root<PersistentOrder> entry = cq.from(PersistentOrder.class);
		cq.where(cb.equal(entry.get(PersistentOrder_.userIdentifier), userIdentifier));
		TypedQuery<PersistentOrder> tq = em.createQuery(cq);

		return Iterables.from(tq.getResultList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Iterable<PersistentOrder> find(UserIdentifier userIdentifier, DateTime from, DateTime to)
	{
		Objects.requireNonNull(userIdentifier, "userIdentifier");
		Objects.requireNonNull(from, "from");
		Objects.requireNonNull(to, "to");

		EntityManager em = emf.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<PersistentOrder> cq = cb.createQuery(PersistentOrder.class);
		Root<PersistentOrder> entry = cq.from(PersistentOrder.class);
		Predicate p1 = cb.equal(entry.get(PersistentOrder_.userIdentifier), userIdentifier);
		Predicate p2 = cb.between(entry.get(PersistentOrder_.dateCreated), from.toDate(), to.toDate());
		cq.where(p1, p2);
		TypedQuery<PersistentOrder> tq = em.createQuery(cq);

		return Iterables.from(tq.getResultList());
	}

	/**
	 * 
	 * @param order
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
