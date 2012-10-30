package org.salespointframework.core.inventory;

import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.salespointframework.core.database.Database;
import org.salespointframework.core.order.PersistentOrder;
import org.salespointframework.core.product.PersistentProduct_;
import org.salespointframework.core.product.ProductIdentifier;
import org.salespointframework.util.Iterables;

/**
 * 
 * @author Paul Henke
 * 
 */
public class PersistentInventory implements Inventory<PersistentInventoryItem> 
{
	private final EntityManagerFactory emf = Database.INSTANCE.getEntityManagerFactory();
	private final EntityManager entityManager;

	/**
	 * Creates a new PersistentInventory.
	 */
	public PersistentInventory() 
	{
		this.entityManager = null;
	}

	/**
	 * Creates an new PersistentInventory. 
	 * 
	 * @param entityManager
	 *            an {@link EntityManager}
	 * 
	 * @throws NullPointerException if entityManager is null
	 */
	public PersistentInventory(EntityManager entityManager) 
	{
		this.entityManager = Objects.requireNonNull(entityManager, "entityManager must not be null");
	}

	@Override
	public void add(PersistentInventoryItem inventoryItem) 
	{
		Objects.requireNonNull(inventoryItem, "inventoryItem must not be null");
		EntityManager em = getEntityManager();
		em.merge(inventoryItem);
		beginCommit(em);
	}

	/**
	 * Adds multiple {@link PersistentInventoryItem}s to this PersistentInventory
	 * 
	 * @param inventoryItems
	 *            an {@link Iterable} of {@link PersistentInventoryItem}s to be added
	 * 
	 * @throws NullPointerException if productInstances is null
	 */
	public void addAll(Iterable<? extends PersistentInventoryItem> inventoryItems) 
	{
		Objects.requireNonNull(inventoryItems, "inventoryItems must not be null");
		EntityManager em = getEntityManager();
		for (PersistentInventoryItem e : inventoryItems) 
		{
			em.merge(e);
		}
		beginCommit(em);
	}

	@Override
	public boolean remove(InventoryItemIdentifier inventoryItemIdentifier) 
	{
		Objects.requireNonNull(inventoryItemIdentifier, "inventoryItemIdentifier must not be null");
		EntityManager em = getEntityManager();
		System.out.println("Remove: " + em);
		Object product = em.find(PersistentInventoryItem.class, inventoryItemIdentifier);
		if (product != null) 
		{
			em.remove(product);
			beginCommit(em);
			return true;
		} else 	{
			return false;
		}
	}

	@Override
	public boolean contains(InventoryItemIdentifier inventoryItemIdentifier) {
		Objects.requireNonNull(inventoryItemIdentifier, "inventoryItemIdentifier must not be null");
		EntityManager em = getEntityManager();
		return em.find(PersistentInventoryItem.class, inventoryItemIdentifier) != null;
	}

	@Override
	public <E extends PersistentInventoryItem> E get(Class<E> clazz, InventoryItemIdentifier inventoryItemIdentifier) {
		Objects.requireNonNull(inventoryItemIdentifier, "inventoryItemIdentifier must not be null");
		Objects.requireNonNull(clazz, "clazz");
		EntityManager em = getEntityManager();
		return em.find(clazz, inventoryItemIdentifier);
	}

	@Override
	public <E extends PersistentInventoryItem> Iterable<E> find(Class<E> clazz) {
		Objects.requireNonNull(clazz, "clazz must not be null");

		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<E> cq = cb.createQuery(clazz);
		TypedQuery<E> tq = em.createQuery(cq);

		return Iterables.of(tq.getResultList());
	}

	@Override
	public <E extends PersistentInventoryItem> E getByProductIdentifier(Class<E> clazz, ProductIdentifier productIdentifier)
	{
		Objects.requireNonNull(clazz, "clazz must be not null");
		Objects.requireNonNull(productIdentifier, "productIdentifier must be not null"); 
		
		/*
		for(E item : this.find(clazz)) 
		{
			if(item.getProduct().getIdentifier().equals(productIdentifier)) 
			{
				return item;
			}
		}
		
		return null;
		*/
		
		
		
		// FIXME ?
		
		
		EntityManager em  = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<E> cq = cb.createQuery(clazz);
		Root<E> entry = cq.from(clazz);
		
	
		Predicate p1 = cb.equal(entry.get(PersistentInventoryItem_.product).get(PersistentProduct_.productIdentifier), productIdentifier);
		
		cq.where(p1);

		TypedQuery<E> tq = em.createQuery(cq);
		
		List<E> result = tq.getResultList();

		return result.size() == 0 ? null : result.get(0);
		
	}

	
	/*
	@Override
	public <E extends PersistentInventoryItem> Iterable<E> find(Class<E> clazz,	ProductIdentifier productIdentifier) {
		Objects.requireNonNull(clazz, "clazz must not be null");
		Objects.requireNonNull(productIdentifier, "productIdentifier must not be null");

		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<E> cq = cb.createQuery(clazz);
		Root<E> entry = cq.from(clazz);

	
		// error weil null
		Predicate p1 = null; //cb.equal(entry.get(PersistentInventoryItem_.productIdentifier), productIdentifier);

		cq.where(p1);

		TypedQuery<E> tq = em.createQuery(cq);

		return Iterables.of(tq.getResultList());
	}
	*/
	
	/*
	@Override
	public <E extends PersistentProductInstance> Iterable<E> find(Class<E> clazz, ProductIdentifier productIdentifier, Iterable<ProductFeature> productFeatures) {
		Objects.requireNonNull(clazz, "clazz must not be null");
		Objects.requireNonNull(productIdentifier, "productIdentifier must not be null");
		Objects.requireNonNull(productFeatures, "productFeatures must not be null");
		return Iterables.of(this.findInternal(clazz, productIdentifier, productFeatures));
	}

	private <E extends PersistentProductInstance> List<E> findInternal(Class<E> clazz, ProductIdentifier productIdentifier, Iterable<ProductFeature> productFeatures) {
		Set<ProductFeature> featureSet = Iterables.asSet(productFeatures);

		EntityManager em = getEntityManager();
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
		
		return result;
	}
	*/
	
	
	/**
	 * Creates an new instance of the PersistentInventory The
	 * {@link PersistentOrder} uses this method for transactional removal of {@link ProductInstance}s
	 * 
	 * @param entityManager the {@link EntityManager} to be used for all operations (methods)
	 * @return a new PersistentInventory
	 * @throws NullPointerException if entityManager is null
	 */
	public PersistentInventory newInstance(EntityManager entityManager) {
		Objects.requireNonNull(entityManager, "entityManager must not be null");
		return new PersistentInventory(entityManager);
	}

	private final EntityManager getEntityManager() {
		return entityManager != null ? entityManager : emf.createEntityManager();
	}

    /**
     * Updates and persists an existing {@link PersistentInventoryItem} to the
     * {@link PersistentInventory} and the Database
     * 
     * @param item
     *            the <code>PersistentInventoryItem</code> to be updated
     * @throws NullPointerException
     *             if <code>item</code> is <code>null</code>
     */
	public final void update(PersistentInventoryItem item)
	{
		java.util.Objects.requireNonNull(item, "item must not be null");
		EntityManager em = emf.createEntityManager();
		em.merge(item);
		beginCommit(em);
	}

	
	private final void beginCommit(EntityManager entityManager) {
		if (this.entityManager == null) {
			entityManager.getTransaction().begin();
			entityManager.getTransaction().commit();
		} 
	}


}
