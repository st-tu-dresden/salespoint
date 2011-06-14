package org.salespointframework.core.catalog;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import org.salespointframework.core.database.Database;
import org.salespointframework.core.product.AbstractProductType;

public abstract class AbstractProductCatalog<T extends AbstractProductType> implements ProductCatalog<T> {

	//JPA
	private Class<T> clazz;
	private EntityManagerFactory emf = Database.getInstance().getEntityManagerFactory();
	
	@Deprecated
	protected AbstractProductCatalog() {
		
	}
	
	public AbstractProductCatalog(Class<T> clazz) {
		this.clazz = clazz;
		
	}
	
	
	@Override
	public void addProductType(T productType) {
		emf.createEntityManager().persist(productType);
	}
	
	@Override
	public boolean removeProductType(int productIdentifier) {
		return false;
	}

	@Override
	public T findProductTypeByProductIdentifier(int productIdentifier) {
		EntityManager em = emf.createEntityManager();
		return em.find(clazz, productIdentifier);
	}

	@Override
	public T findProductTypeByName(String name) {
		return null;
	}

	@Override
	public Iterable<T> findProductTypesByCategory(String category) {
		EntityManager em = emf.createEntityManager();
		TypedQuery<T> tquery = em.createQuery("",clazz);
		return tquery.getResultList();
	}

	@Override
	public Iterable<CatalogEntry<T>> getCatalogEntries() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

}
