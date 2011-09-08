package org.salespointframework.core.inventory;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.salespointframework.core.database.Database;
import org.salespointframework.core.database.ICanHasClass;
import org.salespointframework.core.product.PersistentProduct;
import org.salespointframework.core.product.SerialNumber;
import org.salespointframework.util.Iterables;
import org.salespointframework.util.Objects;

//Vollständig getypt
//public class AbstractInventory<T1 extends AbstractInventoryEntry<T2, T3>, T2 extends AbstractProductInstance<T3>, T3 extends AbstractProductType> 
//implements Inventory<T1,T2,T3> {

public abstract class AbstractInventory<T extends PersistentProduct> implements Inventory<T>, ICanHasClass<T> {

	final EntityManager entityManager;
	
	// Alles Instant
	public AbstractInventory() {
		entityManager = null;
	}
	
	// Kann per begin, commit, rollback gesteuert werden
	public AbstractInventory(final EntityManager entityManager) {
		this.entityManager = Objects.requireNonNull(entityManager, "entityManager");
	}
	
	@Override
	public void addProductInstance(final T productInstance) {
		Objects.requireNonNull(productInstance, "productInstance");
		EntityManager em = foobar();
		em.persist(productInstance);
		beginCommit(em);
	}

	@Override
	public void removeProductInstance(final SerialNumber serialNumber) {
		Objects.requireNonNull(serialNumber, "serialNumber");
		EntityManager em = foobar();
		em.remove(this.getProductInstance(serialNumber));
		beginCommit(em);
	}

	@Override
	public T getProductInstance(final SerialNumber serialNumber) {
		Objects.requireNonNull(serialNumber, "serialNumber");
		EntityManager em = foobar();
		return em.find(this.getContentClass(), serialNumber);
	}

	@Override
	public boolean contains(final SerialNumber serialNumber) {
		Objects.requireNonNull(serialNumber, "serialNumber");
		EntityManager em = foobar();
		return em.find(this.getContentClass(), serialNumber) != null;
	}
	
	@Override
	public Iterable<T> getProductInstances() {
		Class<T> cc = this.getContentClass();
		EntityManager em = foobar();
		TypedQuery<T> tquery = em.createQuery("Select t from " + cc.getSimpleName() + " t",cc);
		return Iterables.from(tquery.getResultList());
	}
	
	
	private final void beginCommit(final EntityManager em) {
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
