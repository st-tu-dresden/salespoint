package org.salespointframework.core.catalog;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Predicate;

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
	
	// TODO Category
	@Override
	public void addCategory(PersistentProductType productType, String category) {
		Objects.requireNonNull(productType, "productType");
		Objects.requireNonNull(category, "category");
		
	}
	
	// TODO Category
	@Override
	public void removeCategory(PersistentProductType productType, String category) {
		Objects.requireNonNull(productType, "productType");
		Objects.requireNonNull(category, "category");
	}
	
	@Override
	public boolean contains(PersistentProductType productType) {
		Objects.requireNonNull(productType, "productType");
		EntityManager em = emf.createEntityManager();
		return em.find(ProductType.class, productType.getProductIdentifier()) != null;
	}
	
	// TODO Category
	@Override
	public Iterable<String> getCategories(PersistentProductType productType) {
		Objects.requireNonNull(productType, "productType");
		return null;
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
		CriteriaQuery<T> q = cb.createQuery(clazz);
		Root<T> entry = q.from(clazz);
		q.where(entry.type().in(clazz));
		TypedQuery<T> tq = em.createQuery(q);

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
		
		cq.where(cb.and(p1,p2));
		
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
		
		cq.where(p1);
		
		TypedQuery<T> tq = em.createQuery(cq);

		return Iterables.from(tq.getResultList());

	}
	
	private void beginCommit(EntityManager entityManager) {
		entityManager.getTransaction().begin();
		entityManager.getTransaction().commit();
	}
}
