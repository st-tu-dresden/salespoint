package org.salespointframework.core.catalog;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.salespointframework.core.database.Database;
import org.salespointframework.core.product.PersistentProductType;
import org.salespointframework.core.product.PersistentProductType_;
import org.salespointframework.core.product.ProductIdentifier;
import org.salespointframework.core.product.ProductType;
import org.salespointframework.util.Iterables;
import org.salespointframework.util.Objects;

/**
 * 
 * @author Paul Henke
 * 
 */
public class PersistentCatalog implements Catalog<PersistentProductType>
{
	private EntityManagerFactory emf = Database.INSTANCE.getEntityManagerFactory();

	/**
	 * 
	 */
	public PersistentCatalog()
	{

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void add(PersistentProductType productType)
	{
		Objects.requireNonNull(productType, "productType");
		EntityManager em = emf.createEntityManager();
		em.persist(productType);
		beginCommit(em);
	}

	/**
	 * {@inheritDoc}
	 */
	public final void addAll(Iterable<? extends PersistentProductType> productTypes)
	{
		Objects.requireNonNull(productTypes, "productTypes");
		EntityManager em = emf.createEntityManager();
		for (ProductType productType : productTypes)
		{
			em.persist(productType);
		}
		beginCommit(em);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean remove(ProductIdentifier productIdentifier)
	{
		Objects.requireNonNull(productIdentifier, "productIdentifier");
		EntityManager em = emf.createEntityManager();
		Object productType = em.find(PersistentProductType.class, productIdentifier);
		if (productType != null)
		{
			em.remove(productType);
			beginCommit(em);
			return true;
		} else
		{
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean contains(ProductIdentifier productIdentifier)
	{
		Objects.requireNonNull(productIdentifier, "productIdentifier");
		EntityManager em = emf.createEntityManager();
		return em.find(ProductType.class, productIdentifier) != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final <T extends PersistentProductType> T get(Class<T> clazz, ProductIdentifier productIdentifier)
	{
		Objects.requireNonNull(clazz, "clazz");
		Objects.requireNonNull(productIdentifier, "productIdentifier");
		EntityManager em = emf.createEntityManager();
		return em.find(clazz, productIdentifier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final <T extends PersistentProductType> Iterable<T> find(Class<T> clazz)
	{
		Objects.requireNonNull(clazz, "clazz");

		EntityManager em = emf.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(clazz);
		Root<T> entry = cq.from(clazz);
		cq.where(entry.type().in(clazz));
		TypedQuery<T> tq = em.createQuery(cq);

		return Iterables.from(tq.getResultList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final <T extends PersistentProductType> Iterable<T> findByName(Class<T> clazz, String name)
	{
		Objects.requireNonNull(clazz, "clazz");
		Objects.requireNonNull(name, "name");

		EntityManager em = emf.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(clazz);
		Root<T> entry = cq.from(clazz);

		Predicate p1 = entry.type().in(clazz);
		Predicate p2 = cb.like(entry.get(PersistentProductType_.name), name);

		cq.where(p1, p2);

		TypedQuery<T> tq = em.createQuery(cq);

		return Iterables.from(tq.getResultList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final <T extends PersistentProductType> Iterable<T> findByCategory(Class<T> clazz, String category)
	{
		Objects.requireNonNull(clazz, "clazz");
		Objects.requireNonNull(category, "category");

		EntityManager em = emf.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(clazz);
		Root<T> entry = cq.from(clazz);
		Predicate p1 = entry.type().in(clazz);
		Predicate p2 = cb.isMember(category, entry.<Set<String>> get("categories"));

		// Overload Resolution fail?
		// Predicate p2 = cb.isMember(category,
		// entry.get(PersistentProductType_.categories));
		// PluralAttribute<PersistentProductType,Set<String>,String> collection
		// = PersistentProductType_.categories;
		// Expression<Set<String>> ex = entry.get(collection);

		cq.where(p1, p2);
		TypedQuery<T> tq = em.createQuery(cq);

		return Iterables.from(tq.getResultList());
	}

	/**
	 * 
	 * @param productType
	 */
	public final void update(PersistentProductType productType)
	{
		Objects.requireNonNull(productType, "productType");
		EntityManager em = emf.createEntityManager();
		em.merge(productType);
		beginCommit(em);
	}

	private final void beginCommit(EntityManager entityManager)
	{
		entityManager.getTransaction().begin();
		entityManager.getTransaction().commit();
	}
}
