package org.salespointframework.core.catalog;

import org.salespointframework.core.product.ProductIdentifier;
import org.salespointframework.core.product.ProductType;

public class PersistentCatalog {
	
	public void add(ProductType productType) {

	}

	public void remove(ProductIdentifier productIdentifier) {

	}
	
	public void addCategory(ProductType productType, String category) {
		
	}
	
	public void removeCategory(ProductType productType, String category) {
		
	}
	
	public boolean contains(ProductType productType) {
		return false;
	}
	
	public Iterable<String> getCategories(ProductType productType) {
		return null;
	}

	public <T extends ProductType> T getProductType(Class<T> clazz,
			ProductIdentifier productIdentifier) {
		return null;
	}

	public <T extends ProductType> Iterable<T> getProductTypes(Class<T> clazz) {
		return null;
	}

	public <T extends ProductType> Iterable<T> findProductTypesByName(String name) {
		return null;
	}

	public <T extends ProductType> Iterable<T> findProductTypesByCategory(
			Class<T> clazz, String category) {
		return null;
	}
}
