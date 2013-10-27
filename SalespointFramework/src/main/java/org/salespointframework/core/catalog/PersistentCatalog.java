package org.salespointframework.core.catalog;

import java.util.Objects;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.salespointframework.util.Iterables;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * A persistent implementation of the {@link Catalog} interface.
 * 
 * @author Paul Henke
 * 
 */
@Service
@Transactional
class PersistentCatalog implements Catalog
{

	@PersistenceContext
	private EntityManager em;
	

	@Override
	public final void add(Product product)
	{
		Objects.requireNonNull(product, "product must not be null");
		em.persist(product);
	}

	
	/**
	 * Adds multiple {@link Product}s to this PersistentCatalog
	 * @param products an {@link Iterable} of {@link Product}s or subtypes to be added
	 */
	public final void addAll(Iterable<? extends Product> products)
	{
		Objects.requireNonNull(products, "products must not be null");
		for (Product product : products)
		{
			em.persist(product);
		}
	}


	
	@Override
	public final boolean remove(ProductIdentifier productIdentifier)
	{
		Objects.requireNonNull(productIdentifier, "productIdentifier must not be null");
		Object product = em.find(Product.class, productIdentifier);
		if(product != null)
		{
			em.remove(product);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public final boolean contains(ProductIdentifier productIdentifier)
	{
		Objects.requireNonNull(productIdentifier, "productIdentifier must not be null");
		return em.find(Product.class, productIdentifier) != null;
	}

	@Override
	public final <T extends Product> T get(Class<T> clazz, ProductIdentifier productIdentifier)
	{
		Objects.requireNonNull(clazz, "clazz must not be null");
		Objects.requireNonNull(productIdentifier, "productIdentifier must not be null");
		return em.find(clazz, productIdentifier);
	}

	@Override
	public final <T extends Product> Iterable<T> find(Class<T> clazz)
	{
		Objects.requireNonNull(clazz, "clazz must not be null");

		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<T> cq = cb.createQuery(clazz);
		Root<T> from = cq.from(clazz);
		cq.select(from);
		
		TypedQuery<T> tq = em.createQuery(cq);
		
		return Iterables.of(tq.getResultList());
	}

	@Override
	public final <T extends Product> Iterable<T> findByName(Class<T> clazz, String name)
	{
		Objects.requireNonNull(clazz, "clazz must not be null");
		Objects.requireNonNull(name, "name must not be null");

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(clazz);
		Root<T> entry = cq.from(clazz);
		cq.where(cb.like(entry.get(Product_.name), name));
		TypedQuery<T> tq = em.createQuery(cq);

		return Iterables.of(tq.getResultList());
	}

	@Override
	public final <T extends Product> Iterable<T> findByCategory(Class<T> clazz, String category)
	{
		
		Objects.requireNonNull(clazz, "clazz must not be null");
		Objects.requireNonNull(category, "category must not be null");

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(clazz);
		Root<T> entry = cq.from(clazz);
		
		Predicate p2 = cb.isMember(category, entry.<Set<String>> get("categories"));

		// Overload Resolution fail?
//		 Predicate p2 = cb.isMember(category,
//		 entry.get(Product_.categories));
//		 PluralAttribute<PersistentProductType, Set<String>, String>
//		 collection = PersistentProductType_.categories;
//		 Expression<Set<String>> ex = entry.get(collection);

		cq.where(p2);
		TypedQuery<T> tq = em.createQuery(cq);

		return Iterables.of(tq.getResultList());
	}
}
