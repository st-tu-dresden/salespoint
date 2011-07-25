package org.salespointframework.core.catalog;

import org.salespointframework.core.product.ProductIdentifier;
import org.salespointframework.core.product.ProductType;

public interface ProductCatalog<T extends ProductType> {
	void addProductType(T productType);
	void removeProductType(ProductIdentifier productIdentifier);				
	T findProductTypeByProductIdentifier(ProductIdentifier productIdentifier);	
	T findProductTypeByName(String name);	// TODO sinnvoll?
	Iterable<T> getProductTypes();
	Iterable<T> findProductTypesByCategory(String category);
	Iterable<CatalogEntry<T>> getCatalogEntries();
	void clear();
}
 