package org.salespointframework.core.catalog;

import org.salespointframework.core.product.ProductType;

public interface ProductCatalog<T extends ProductType> {
	void addProductType(T productType);
	boolean removeProductType(int productIdentifier);				// TODO pid wirklich int?
	T findProductTypeByProductIdentifier(int productIdentifier);	// TODO
	T findProductTypeByName(String name);
	Iterable<T> findProductTypesByCategory(String category);
	Iterable<CatalogEntry<T>> getCatalogEntries();
	void clear();
}
