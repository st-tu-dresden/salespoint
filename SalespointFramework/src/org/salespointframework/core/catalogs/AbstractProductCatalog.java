package org.salespointframework.core.catalogs;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import org.salespointframework.core.database.Database;
import org.salespointframework.core.products.ProductType;

public class AbstractProductCatalog<T extends ProductType> implements ProductCatalog<T> {

	EntityManagerFactory emf = Database.getInstance().getEntityManagerFactory();
	
	//JPA
	Class<T> clazz;
	
	public AbstractProductCatalog(Class<T> clazz) {
		this.clazz = clazz;
		
	}
	
	
	@Override
	public void addProductType(T productType) {
		
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

}
