package org.salespointframework.core.catalog;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.PluralAttribute;

import org.salespointframework.core.database.Database;
import org.salespointframework.core.product.PersistentProductType;
import org.salespointframework.core.product.PersistentProductType_;
import org.salespointframework.core.product.ProductIdentifier;
import org.salespointframework.core.product.ProductType;
import org.salespointframework.util.Iterables;
import org.salespointframework.util.Objects;

public class PersistentCatalog implements ProductCatalog<PersistentProductType> {
	
	EntityManagerFactory emf = Database.INSTANCE.getEntityManagerFactory();
	
	@Override
	public void add(PersistentProductType productType) {
		Objects.requireNonNull(productType, "productType");
		EntityManager em = emf.createEntityManager();
		em.persist(productType);
		beginCommit(em);
	}

	@Override
	public void remove(ProductIdentifier productIdentifier) {
		Objects.requireNonNull(productIdentifier, "productIdentifier");
		EntityManager em = emf.createEntityManager();
		em.remove(productIdentifier);
		beginCommit(em);
	}
	
	@Override
	public boolean contains(PersistentProductType productType) {
		Objects.requireNonNull(productType, "productType");
		EntityManager em = emf.createEntityManager();
		return em.find(ProductType.class, productType.getProductIdentifier()) != null;
	}
	
	@Override
	public <T extends PersistentProductType> T getProductType(Class<T> clazz, ProductIdentifier productIdentifier) {
		Objects.requireNonNull(clazz, "clazz");
		Objects.requireNonNull(productIdentifier, "productIdentifier");
		EntityManager em = emf.createEntityManager();
		return em.find(clazz, productIdentifier);
	}

	@Override
	public <T extends PersistentProductType> Iterable<T> getProductTypes(Class<T> clazz) {
		Objects.requireNonNull(clazz, "clazz");
		
		EntityManager em = emf.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(clazz);
		Root<T> entry = cq.from(clazz);
		cq.where(entry.type().in(clazz));
		TypedQuery<T> tq = em.createQuery(cq);

		return Iterables.from(tq.getResultList());
	}

	@Override
	public <T extends PersistentProductType> Iterable<T> findProductTypesByName(Class<T> clazz, String name) {
		Objects.requireNonNull(clazz, "clazz");
		Objects.requireNonNull(name, "name");

		EntityManager em = emf.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(clazz);
		Root<T> entry = cq.from(clazz);

		Predicate p1 = entry.type().in(clazz); 
		Predicate p2 = cb.like(entry.get(PersistentProductType_.name), name);
		
		cq.where( p1, p2);
		 
		TypedQuery<T> tq = em.createQuery(cq);

		return Iterables.from(tq.getResultList());
	}

	// TODO Category
	@Override
	public <T extends PersistentProductType> Iterable<T> findProductTypesByCategory(Class<T> clazz, String category) {
		Objects.requireNonNull(clazz, "clazz");
		Objects.requireNonNull(category, "category");
		
		EntityManager em = emf.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(clazz);
		Root<T> entry = cq.from(clazz);

		Predicate p1 = entry.type().in(clazz);
		
		Predicate p2 = cb.isMember(category, entry.<Set<String>>get("categories"));

		//Predicate p2 = cb.isMember(category, entry.get(PersistentProductType_.categories));
		
		// interface SetAttribute<X,E> extends PluralAttribute<X,java.util.Set<E>,E>
		// interface SetAttribute<PersistentProductType,String> extends PluralAttribute<PersistentProductType,Set<String>,String>
				
		//<E,C extends java.util.Collection<E>> Expression<C> get(PluralAttribute<X,C,E> collection)
		//<String,Set<String> extends java.util.Collection<String>> Expression<Set<String>> get(PluralAttribute<PersistentProductType,Set<String>,String> collection)		

		//PluralAttribute<PersistentProductType,Set<String>,String> collection = PersistentProductType_.categories;
		//Expression<Set<String>> ex = entry.get(collection);
		
		
		cq.where(p1, p2);
		
		TypedQuery<T> tq = em.createQuery(cq);

		return Iterables.from(tq.getResultList());

	}
	
	private void beginCommit(EntityManager entityManager) {
		entityManager.getTransaction().begin();
		entityManager.getTransaction().commit();
	}
}
