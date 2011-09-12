package org.salespointframework.core.inventory;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.salespointframework.core.database.Database;
import org.salespointframework.core.order.paul.PersistentOrder;
import org.salespointframework.core.product.PersistentProduct;
import org.salespointframework.core.product.PersistentProductType;
import org.salespointframework.core.product.PersistentProduct_;
import org.salespointframework.core.product.ProductFeature;
import org.salespointframework.core.product.SerialNumber;
import org.salespointframework.util.Iterables;
import org.salespointframework.util.Objects;

/**
 * 
 * @author Paul Henke
 * 
 */
public final class PersistentInventory implements __Inventory__<PersistentProduct, PersistentProductType> {
	
	private final EntityManagerFactory emf = Database.INSTANCE.getEntityManagerFactory();
	private final EntityManager entityManager;
	
	
	public PersistentInventory() {
		this.entityManager = null;
	}

	public PersistentInventory(EntityManager entityManager) {
		this.entityManager = Objects.requireNonNull(entityManager, "entityManager");
	}
	
	@Override
	public void add(PersistentProduct persistentProduct) {
			Objects.requireNonNull(persistentProduct, "persistentProduct");
			EntityManager em = getEntityManager();
			em.persist(persistentProduct);
			beginCommit(em);
	}
	
	@Override
	public void remove(SerialNumber serialNumber) {
		Objects.requireNonNull(serialNumber, "serialNumber");
		EntityManager em = getEntityManager();
		Object order = em.find(PersistentProduct.class, serialNumber);
		if(order != null) {
			em.remove(serialNumber);
			beginCommit(em);
		}
		em.remove(serialNumber);
		beginCommit(em);
	}
	
	@Override
	public boolean contains(SerialNumber serialNumber) {
		Objects.requireNonNull(serialNumber, "serialNumber");
		EntityManager em = getEntityManager();
		return em.find(PersistentProduct.class, serialNumber) != null;
	}
	
	
	@Override
	public <E extends PersistentProduct> E get(Class<E> clazz,	SerialNumber serialNumber) {
		Objects.requireNonNull(serialNumber, "serialNumber");
		Objects.requireNonNull(clazz, "clazz");
		EntityManager em = getEntityManager();
		return em.find(clazz, serialNumber);
	}

	// convenience
	public PersistentProduct get(SerialNumber serialNumber) {
		return this.get(PersistentProduct.class, serialNumber);
	}

	@Override
	public <E extends PersistentProduct> Iterable<E> findProducts(Class<E> clazz) {
		Objects.requireNonNull(clazz, "clazz");
		
		EntityManager em = emf.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<E> cq = cb.createQuery(clazz);
		Root<E> entry = cq.from(clazz);
		
		cq.where(entry.type().in(clazz));
		
		TypedQuery<E> tq = em.createQuery(cq);

		return Iterables.from(tq.getResultList());
	}
	
	// convenience
	public Iterable<PersistentProduct> findProducts() {
		return this.findProducts(PersistentProduct.class);
	}
	

	
	@Override
	public <E extends PersistentProduct> Iterable<E> findProductByProductType(Class<E> clazz, PersistentProductType productType) {
		Objects.requireNonNull(clazz, "clazz");
		Objects.requireNonNull(productType, "productType");
		
		EntityManager em = emf.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<E> cq = cb.createQuery(clazz);
		Root<E> entry = cq.from(clazz);
	
		Predicate p0 = entry.type().in(clazz);
		Predicate p1 = cb.equal(entry.get(PersistentProduct_.productIdentifier), productType.getProductIdentifier());
	
		cq.where(p0, p1);
		
		TypedQuery<E> tq = em.createQuery(cq);
	
		return Iterables.from(tq.getResultList());
	}
	
	// convenience
	public Iterable<PersistentProduct> findProductByProductType(PersistentProductType productType) {
		return this.findProductByProductType(PersistentProduct.class, productType);
	}
	
	@Override
	public <E extends PersistentProduct> Iterable<E> findProductByProductTypeAndFeatures(Class<E> clazz, PersistentProductType productType,	Iterable<ProductFeature> productFeatures) {
		Objects.requireNonNull(clazz, "clazz");
		Objects.requireNonNull(productType, "productType");
		Objects.requireNonNull(productFeatures, "productFeatures");
		
		Set<ProductFeature> featureSet = Iterables.toSet(productFeatures);
		
		EntityManager em = emf.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<E> cq = cb.createQuery(clazz);
		Root<E> entry = cq.from(clazz);
		
		Predicate p0 = entry.type().in(clazz);
		Predicate p1 = cb.equal(entry.get(PersistentProduct_.productIdentifier), productType.getProductIdentifier());
		Predicate p2 = cb.equal(entry.<Set<ProductFeature>>get("productFeatures"), featureSet);
		
		cq.where(p0, p1, p2);
		
		TypedQuery<E> tq = em.createQuery(cq);
		return Iterables.from(tq.getResultList());
	}
	
	// convenience
	public Iterable<PersistentProduct> findProductByProductTypeAndFeatures(PersistentProductType productType, Iterable<ProductFeature> productFeatures) {
		return this.findProductByProductTypeAndFeatures(PersistentProduct.class, productType, productFeatures);
	}
	
	
	public static PersistentInventory createNew(EntityManager entityManager) {
		return new PersistentInventory(entityManager);
	}

	
	private EntityManager getEntityManager() {
		return entityManager != null ? entityManager : emf.createEntityManager();
	}
	
	// TODO not pretty
	private void beginCommit(EntityManager entityManager) {
		if(this.entityManager == null) {
			entityManager.getTransaction().begin();
			entityManager.getTransaction().commit();
		} else {
			this.entityManager.getTransaction().begin();
			this.entityManager.getTransaction().commit();
		}
	}
}
