package org.salespointframework.core.inventory;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.salespointframework.core.database.Database;
import org.salespointframework.core.database.ICanHasClass;
import org.salespointframework.core.product.AbstractProductInstance;
import org.salespointframework.core.product.SerialNumber;
import org.salespointframework.util.Objects;
import org.salespointframework.util.SalespointIterable;

//public class AbstractInventory<T1 extends AbstractInventoryEntry<T2, T3>, T2 extends AbstractProductInstance<T3>, T3 extends AbstractProductType> 
//implements Inventory<T1,T2,T3> {

public abstract class AbstractInventory<T extends AbstractProductInstance> implements Inventory<T>, ICanHasClass<T> {

	EntityManager entityManager;
	
	
	// Alles Instant
	public AbstractInventory() {
	
	}
	
	// Kann per begin, commit, rollback gesteuert werden
	public AbstractInventory(EntityManager entityManager) {
		this.entityManager = Objects.requireNonNull(entityManager, "entityManager");
	}
	
	@Override
	public void addProductInstance(T productInstance) {
		Objects.requireNonNull(productInstance, "productInstance");
		EntityManager em = foobar();
		em.persist(productInstance);
		beginCommit(em);
	}

	@Override
	public void removeProductInstance(SerialNumber serialNumber) {
		Objects.requireNonNull(serialNumber, "serialNumber");
		EntityManager em = foobar();
		em.remove(serialNumber);
		beginCommit(em);
	}

	@Override
	public T getProductInstance(SerialNumber serialNumber) {
		Objects.requireNonNull(serialNumber, "serialNumber");
		EntityManager em = foobar();
		return em.find(this.getContentClass(), serialNumber);
	}

	@Override
	public boolean contains(SerialNumber serialNumber) {
		Objects.requireNonNull(serialNumber, "serialNumber");
		EntityManager em = foobar();
		return em.contains(serialNumber);
	}
	
	@Override
	public Iterable<T> getProductInstances() {
		Class<T> cc = this.getContentClass();
		EntityManager em = foobar();
		TypedQuery<T> tquery = em.createQuery("Select t from " + cc.getCanonicalName() + " t",cc);
		return SalespointIterable.from(tquery.getResultList());
	}
	
	
	private final void beginCommit(EntityManager em) {
		if(entityManager == null) {
			em.getTransaction().begin();
			em.getTransaction().commit();
		}
	}
	
	// ?? Operator in C#, gibts hier leider nicht, deswegen die Methode
	private final EntityManager foobar() {
		return entityManager != null ? entityManager : Database.INSTANCE.getEntityManagerFactory().createEntityManager();
	}
	
	
}
