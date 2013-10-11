package org.salespointframework.core.inventory;

import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.salespointframework.core.catalog.ProductIdentifier;
import org.salespointframework.core.catalog.Product_;
import org.salespointframework.util.Iterables;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author Paul Henke
 * 
 */
@Service
@Transactional
class PersistentInventory implements Inventory 
{
	// private final EntityManagerFactory emf = Database.INSTANCE.getEntityManagerFactory();
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void add(InventoryItem inventoryItem) 
	{
		Objects.requireNonNull(inventoryItem, "inventoryItem must not be null");
		entityManager.merge(inventoryItem);
	}

	/**
	 * Adds multiple {@link InventoryItem}s to this PersistentInventory
	 * 
	 * @param inventoryItems
	 *            an {@link Iterable} of {@link InventoryItem}s to be added
	 * 
	 * @throws NullPointerException if inventoryItems is null
	 */
	public void addAll(Iterable<? extends InventoryItem> inventoryItems) 
	{
		Objects.requireNonNull(inventoryItems, "inventoryItems must not be null");
		for (InventoryItem e : inventoryItems) 
		{
			entityManager.merge(e);
		}
	}

	@Override
	public boolean remove(InventoryItemIdentifier inventoryItemIdentifier) 
	{
		Objects.requireNonNull(inventoryItemIdentifier, "inventoryItemIdentifier must not be null");
		System.out.println("Remove: " + entityManager);
		Object product = entityManager.find(InventoryItem.class, inventoryItemIdentifier);
		if (product != null) 
		{
			entityManager.remove(product);
			return true;
		} else 	{
			return false;
		}
	}

	@Override
	public boolean contains(InventoryItemIdentifier inventoryItemIdentifier) {
		Objects.requireNonNull(inventoryItemIdentifier, "inventoryItemIdentifier must not be null");
		return entityManager.find(InventoryItem.class, inventoryItemIdentifier) != null;
	}

	@Override
	public <E extends InventoryItem> E get(Class<E> clazz, InventoryItemIdentifier inventoryItemIdentifier) {
		Objects.requireNonNull(inventoryItemIdentifier, "inventoryItemIdentifier must not be null");
		Objects.requireNonNull(clazz, "clazz");
		return entityManager.find(clazz, inventoryItemIdentifier);
	}

	@Override
	public <E extends InventoryItem> Iterable<E> find(Class<E> clazz) {
		Objects.requireNonNull(clazz, "clazz must not be null");

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<E> cq = cb.createQuery(clazz);
		TypedQuery<E> tq = entityManager.createQuery(cq);

		return Iterables.of(tq.getResultList());
	}

	@Override
	public <E extends InventoryItem> E getByProductIdentifier(Class<E> clazz, ProductIdentifier productIdentifier)
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
		
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<E> cq = cb.createQuery(clazz);
		Root<E> entry = cq.from(clazz);
		
	
		Predicate p1 = cb.equal(entry.get(InventoryItem_.product).get(Product_.productIdentifier), productIdentifier);
		
		cq.where(p1);

		TypedQuery<E> tq = entityManager.createQuery(cq);
		
		List<E> result = tq.getResultList();

		return result.size() == 0 ? null : result.get(0);
		
	}

	
	/*
	@Override
	public <E extends InventoryItem> Iterable<E> find(Class<E> clazz,	ProductIdentifier productIdentifier) {
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
     * Updates and persists an existing {@link InventoryItem} to the
     * {@link PersistentInventory} and the Database
     * 
     * @param item
     *            the <code>InventoryItem</code> to be updated
     * @throws NullPointerException
     *             if <code>item</code> is <code>null</code>
     */
	public final void update(InventoryItem item)
	{
		java.util.Objects.requireNonNull(item, "item must not be null");
		entityManager.merge(item);
	}
}
