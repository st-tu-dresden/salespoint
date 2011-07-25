package org.salespointframework.core.inventory;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.salespointframework.core.database.ICanHasClass;
import org.salespointframework.core.product.AbstractProductInstance;
import org.salespointframework.core.product.AbstractProductType;
import org.salespointframework.core.product.SerialNumber;
import org.salespointframework.util.Objects;
import org.salespointframework.util.SalespointIterable;

// TODO abstract

//public class AbstractInventory<T1 extends AbstractInventoryEntry<T2, T3>, T2 extends AbstractProductInstance<T3>, T3 extends AbstractProductType> 
//implements Inventory<T1,T2,T3> {

public abstract class AbstractInventory<T extends AbstractProductInstance> implements Inventory<T>, ICanHasClass<T> {

	EntityManager entityManager;
	
	public AbstractInventory(EntityManager entityManager) {
		this.entityManager = Objects.requireNonNull(entityManager, "entityManager");
	}
	
	@Override
	public void addProductInstance(T productInstance) {
		Objects.requireNonNull(productInstance, "productInstance");
		entityManager.persist(productInstance);
	}

	@Override
	public void removeProductInstance(SerialNumber serialNumber) {
		Objects.requireNonNull(serialNumber, "serialNumber");
		entityManager.remove(serialNumber);
	}

	@Override
	public T getProductInstance(SerialNumber serialNumber) {
		Objects.requireNonNull(serialNumber, "serialNumber");
		return entityManager.find(this.getContentClass(), serialNumber);
	}

	@Override
	public boolean contains(SerialNumber serialNumber) {
		Objects.requireNonNull(serialNumber, "serialNumber");
		return entityManager.contains(serialNumber);
	}
	
	@Override
	public Iterable<T> getProductInstances() {
		Class<T> cc = this.getContentClass();
		TypedQuery<T> tquery = entityManager.createQuery("Select t from " + cc.getCanonicalName() + " t",cc);
		return SalespointIterable.from(tquery.getResultList());
	}
}
