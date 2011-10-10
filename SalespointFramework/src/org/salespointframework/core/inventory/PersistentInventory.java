package org.salespointframework.core.inventory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.salespointframework.core.database.Database;
import org.salespointframework.core.order.PersistentOrder;
import org.salespointframework.core.product.PersistentProductInstance;
import org.salespointframework.core.product.PersistentProductInstance_;
import org.salespointframework.core.product.ProductInstance;
import org.salespointframework.core.product.ProductFeature;
import org.salespointframework.core.product.ProductIdentifier;
import org.salespointframework.core.product.SerialNumber;
import org.salespointframework.util.ArgumentNullException;
import org.salespointframework.util.Iterables;
import org.salespointframework.util.Objects;

/**
 * 
 * @author Paul Henke
 * 
 */
public class PersistentInventory implements Inventory<PersistentProductInstance> {
	private final EntityManagerFactory emf = Database.INSTANCE
			.getEntityManagerFactory();
	private final EntityManager entityManager;

	/**
	 * Creates a new PersistentInventory.
	 */
	public PersistentInventory() {
		this.entityManager = null;
	}

	/**
	 * Creates an new PersistentInventory. TODO
	 * 
	 * @param entityManager
	 *            an {@link EntityManager}
	 */
	public PersistentInventory(EntityManager entityManager) {
		this.entityManager = Objects.requireNonNull(entityManager,
				"entityManager");
	}

	@Override
	public void add(PersistentProductInstance persistentProduct) {
		Objects.requireNonNull(persistentProduct, "persistentProduct");
		EntityManager em = getEntityManager();
		em.persist(persistentProduct);
		beginCommit(em);
	}

	/**
	 * Adds multiple {@link PersistentProductInstance}s to this PersistentInventory
	 * 
	 * @param productInstances
	 *            an {@link Iterable} of {@link PersistentProductInstance}s to be added
	 */
	public final void addAll(Iterable<? extends PersistentProductInstance> productInstances) {
		Objects.requireNonNull(productInstances, "products");
		EntityManager em = emf.createEntityManager();
		for (PersistentProductInstance e : productInstances) {
			em.persist(e);
		}
		beginCommit(em);
	}

	@Override
	public boolean remove(SerialNumber serialNumber) {
		Objects.requireNonNull(serialNumber, "serialNumber");
		EntityManager em = getEntityManager();
		Object product = em.find(PersistentProductInstance.class, serialNumber);
		if (product != null) {
			em.remove(product);
			beginCommit(em);
			return true;
		}
		{
			return false;
		}
	}

	@Override
	public boolean contains(SerialNumber serialNumber) {
		Objects.requireNonNull(serialNumber, "serialNumber");
		EntityManager em = getEntityManager();
		return em.find(PersistentProductInstance.class, serialNumber) != null;
	}

	@Override
	public <E extends PersistentProductInstance> E get(Class<E> clazz, SerialNumber serialNumber) {
		Objects.requireNonNull(serialNumber, "serialNumber");
		Objects.requireNonNull(clazz, "clazz");
		EntityManager em = getEntityManager();
		return em.find(clazz, serialNumber);
	}

	@Override
	public <E extends PersistentProductInstance> Iterable<E> find(Class<E> clazz) {
		Objects.requireNonNull(clazz, "clazz");

		EntityManager em = emf.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<E> cq = cb.createQuery(clazz);
		TypedQuery<E> tq = em.createQuery(cq);

		return Iterables.of(tq.getResultList());
	}

	@Override
	public <E extends PersistentProductInstance> Iterable<E> find(Class<E> clazz,
			ProductIdentifier productIdentifier) {
		Objects.requireNonNull(clazz, "clazz");
		Objects.requireNonNull(productIdentifier, "productIdentifier");

		EntityManager em = emf.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<E> cq = cb.createQuery(clazz);
		Root<E> entry = cq.from(clazz);

		Predicate p1 = cb.equal(
				entry.get(PersistentProductInstance_.productIdentifier),
				productIdentifier);

		cq.where(p1);

		TypedQuery<E> tq = em.createQuery(cq);

		return Iterables.of(tq.getResultList());
	}

	@Override
	public <E extends PersistentProductInstance> Iterable<E> find(Class<E> clazz, ProductIdentifier productIdentifier, Iterable<ProductFeature> productFeatures) {
		Objects.requireNonNull(clazz, "clazz");
		Objects.requireNonNull(productIdentifier, "productIdentifier");
		Objects.requireNonNull(productFeatures, "productFeatures");
		return Iterables.of(this.findInternal(clazz, productIdentifier, productFeatures));
	}

	private <E extends PersistentProductInstance> List<E> findInternal(Class<E> clazz, ProductIdentifier productIdentifier, Iterable<ProductFeature> productFeatures) {
		Set<ProductFeature> featureSet = Iterables.asSet(productFeatures);

		EntityManager em = emf.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<E> cq = cb.createQuery(clazz);
		Root<E> entry = cq.from(clazz);

		Predicate p1 = cb.equal(entry.get(PersistentProductInstance_.productIdentifier), productIdentifier);
		// Predicate p2 = cb.equal(entry.<Set<ProductFeature>> get("productFeatures"), featureSet);

		cq.where(p1);

		TypedQuery<E> tq = em.createQuery(cq);
		List<E> query = tq.getResultList();
		List<E> result = new LinkedList<E>();
		
		if(featureSet.size() == 0) {
			for(E e : query) {
				if(Iterables.isEmpty(e.getProductFeatures())) result.add(e);
			}
		} else {
			for(E e : query) {
				Set<ProductFeature> entryFeatureSet = Iterables.asSet(e.getProductFeatures());
				if(featureSet.equals(entryFeatureSet)) result.add(e);
			}
		}
		
		/*
		int query_entries = 0;
		boolean match = true;
		for (E e : query) {
			query_entries = 0;
			for (ProductFeature have : e.getProductFeatures()) {
				query_entries += 1;
				match = false;

				for (ProductFeature should : featureSet) {
					if (have.equals(should)) {
						// System.out.println("Found match between " + have +
						// " ("
						// + query_entries + ")" + " and " + should);
						match = true;
						break;
					}
				}

				if (!match)
					break;
			}
			
			 // same length and all entries matched. if lengths differ, all
			 // entries may match, but they are still not the same
			
			if (query_entries == featureSet.size() && match)
				result.add(e);
		}
	*/

		return result;
	}
	
	
	/**
	 * Creates an new instance of the PersistentInventory The
	 * {@link PersistentOrder} uses this method for transactional removal of {@link ProductInstance}s
	 * 
	 * @param entityManager the {@link EntityManager} to be used for all operations (methods)
	 * @return a new PersistentInventory
	 * @throws ArgumentNullException if entityManager is null
	 */
	public final PersistentInventory newInstance(EntityManager entityManager) {
		Objects.requireNonNull(entityManager, "entityManager");
		return new PersistentInventory(entityManager);
	}

	private final EntityManager getEntityManager() {
		return entityManager != null ? entityManager : emf.createEntityManager();
	}

	private final void beginCommit(EntityManager entityManager) {
		if (this.entityManager == null) {
			entityManager.getTransaction().begin();
			entityManager.getTransaction().commit();
		} else {
			/*
			this.entityManager.getTransaction().begin();
			this.entityManager.getTransaction().commit();
			*/
		}
	}

	
	// TODO comment & test
	@Override
	public long count(ProductIdentifier productIdentifier) {
		Objects.requireNonNull(productIdentifier, "productIdentifier");

		EntityManager em = emf.createEntityManager();

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<PersistentProductInstance> entry = cq.from(PersistentProductInstance.class);

		cq.where(cb.equal(entry.get(PersistentProductInstance_.productIdentifier), productIdentifier));
		cq.select(cb.count(entry));

		return em.createQuery(cq).getSingleResult();
		
	}
	
	// TODO comment & test
	@Override
	public long count(ProductIdentifier productIdentifier, Iterable<ProductFeature> productFeatures) {
		Objects.requireNonNull(productIdentifier, "productIdentifier");
		Objects.requireNonNull(productFeatures, "productFeatures");
		return findInternal(PersistentProductInstance.class, productIdentifier, productFeatures).size();
	}
}
