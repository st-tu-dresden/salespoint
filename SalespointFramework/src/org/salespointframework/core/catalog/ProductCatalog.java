package org.salespointframework.core.catalog;

import org.salespointframework.core.product.ProductType;

public interface ProductCatalog<T extends ProductType> {
	void addProductType(T productType);
	boolean removeProductType(String productIdentifier);				// TODO pid wirklich int?
	T findProductTypeByProductIdentifier(String productIdentifier);	// TODO
	T findProductTypeByName(String name);
	Iterable<T> findProductTypesByCategory(String category);
	Iterable<CatalogEntry<T>> getCatalogEntries();
	void clear();
}
