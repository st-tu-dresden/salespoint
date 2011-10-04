package org.salespointframework.core.inventory;

import java.util.ArrayList;
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
import org.salespointframework.core.product.PersistentProduct;
import org.salespointframework.core.product.PersistentProduct_;
import org.salespointframework.core.product.Product;
import org.salespointframework.core.product.ProductFeature;
import org.salespointframework.core.product.ProductIdentifier;
import org.salespointframework.core.product.SerialNumber;
import org.salespointframework.util.Iterables;
import org.salespointframework.util.Objects;

/**
 * 
 * @author Paul Henke
 * 
 */
public final class PersistentInventory implements Inventory<PersistentProduct> {
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
	public void add(PersistentProduct persistentProduct) {
		Objects.requireNonNull(persistentProduct, "persistentProduct");
		EntityManager em = getEntityManager();
		em.persist(persistentProduct);
		beginCommit(em);
	}

	/**
	 * Adds multiple {@link PersistentProduct}s to this PersistentInventory
	 * 
	 * @param products
	 *            an Iterable of {@link PersistentProduct}s to be added
	 */
	public final void addAll(Iterable<? extends PersistentProduct> products) {
		Objects.requireNonNull(products, "products");
		EntityManager em = emf.createEntityManager();
		for (PersistentProduct e : products) {
			em.persist(e);
		}
		beginCommit(em);
	}

	@Override
	public boolean remove(SerialNumber serialNumber) {
		Objects.requireNonNull(serialNumber, "serialNumber");
		EntityManager em = getEntityManager();
		Object product = em.find(PersistentProduct.class, serialNumber);
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
		return em.find(PersistentProduct.class, serialNumber) != null;
	}

	@Override
	public <E extends PersistentProduct> E get(Class<E> clazz,
			SerialNumber serialNumber) {
		Objects.requireNonNull(serialNumber, "serialNumber");
		Objects.requireNonNull(clazz, "clazz");
		EntityManager em = getEntityManager();
		return em.find(clazz, serialNumber);
	}

	@Override
	public <E extends PersistentProduct> Iterable<E> find(Class<E> clazz) {
		Objects.requireNonNull(clazz, "clazz");

		EntityManager em = emf.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<E> cq = cb.createQuery(clazz);
		TypedQuery<E> tq = em.createQuery(cq);

		return Iterables.from(tq.getResultList());
	}

	@Override
	public <E extends PersistentProduct> Iterable<E> find(Class<E> clazz,
			ProductIdentifier productIdentifier) {
		Objects.requireNonNull(clazz, "clazz");
		Objects.requireNonNull(productIdentifier, "productIdentifier");

		EntityManager em = emf.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<E> cq = cb.createQuery(clazz);
		Root<E> entry = cq.from(clazz);

		Predicate p1 = cb.equal(
				entry.get(PersistentProduct_.productIdentifier),
				productIdentifier);

		cq.where(p1);

		TypedQuery<E> tq = em.createQuery(cq);

		return Iterables.from(tq.getResultList());
	}

	@Override
	public <E extends PersistentProduct> Iterable<E> find(Class<E> clazz,
			ProductIdentifier productIdentifier,
			Iterable<ProductFeature> productFeatures) {
		Objects.requireNonNull(clazz, "clazz");
		Objects.requireNonNull(productIdentifier, "productIdentifier");
		Objects.requireNonNull(productFeatures, "productFeatures");

		Set<ProductFeature> featureSet = Iterables.toSet(productFeatures);

		EntityManager em = emf.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<E> cq = cb.createQuery(clazz);
		Root<E> entry = cq.from(clazz);

		Predicate p1 = cb.equal(
				entry.get(PersistentProduct_.productIdentifier),
				productIdentifier);
		// Predicate p2 = cb.equal(
		// entry.<Set<ProductFeature>> get("productFeatures"), featureSet);

		cq.where(p1);

		TypedQuery<E> tq = em.createQuery(cq);
		List<E> query = tq.getResultList();
		List<E> result = new ArrayList<E>();
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
			/*
			 * same length and all entries matched. if lengths differ, all
			 * entries may match, but they are still not the same
			 */
			if (query_entries == featureSet.size() && match)
				result.add(e);
		}

		return Iterables.from(result);
	}

	/**
	 * Creates an new Instance of the PersistentInventory The
	 * {@link PersistentOrder} uses this method for transactional removal of
	 * {@link Product}s
	 * 
	 * @param entityManager
	 *            the {@link EntityManager} to be used for all operations
	 *            (methods)
	 * @return a new PersistentInventory
	 */
	public final PersistentInventory newInstance(EntityManager entityManager) {
		return new PersistentInventory(entityManager);
	}

	private final EntityManager getEntityManager() {
		return entityManager != null ? entityManager : emf
				.createEntityManager();
	}

	private final void beginCommit(EntityManager entityManager) {
		if (this.entityManager == null) {
			entityManager.getTransaction().begin();
			entityManager.getTransaction().commit();
		} else {
			this.entityManager.getTransaction().begin();
			this.entityManager.getTransaction().commit();
		}
	}
}
