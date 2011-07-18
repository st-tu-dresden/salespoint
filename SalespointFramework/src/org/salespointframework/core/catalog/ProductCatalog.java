package org.salespointframework.core.catalog;

import org.salespointframework.core.product.ProductType;

public interface ProductCatalog<T extends ProductType> {
	void addProductType(T productType);
	void removeProductType(String productIdentifier);				
	T findProductTypeByProductIdentifier(String productIdentifier);	
	T findProductTypeByName(String name);	// TODO sinnvoll?
	Iterable<T> getProductTypes();
	Iterable<T> findProductTypesByCategory(String category);
	Iterable<CatalogEntry<T>> getCatalogEntries();
	void clear();
}
 