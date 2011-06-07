package org.salespointframework.core.catalogs;

import org.salespointframework.core.products.ProductType;

public class AbstractProductCatalog<T extends ProductType> implements ProductCatalog<T> {

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
		return null;
	}

	@Override
	public T findProductTypeByName(String name) {
		return null;
	}

	@Override
	public Iterable<T> findProductTypesByCategory(String category) {
		return null;
	}

	@Override
	public Iterable<CatalogEntry<T>> getCatalogEntries() {
		// TODO Auto-generated method stub
		return null;
	}

}
