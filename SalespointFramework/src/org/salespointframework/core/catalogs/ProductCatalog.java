package org.salespointframework.core.catalogs;

import org.salespointframework.core.products.ProductType;

public interface ProductCatalog<T extends ProductType> {
	void addProductType(T productType);
	boolean removeProductType(int productIdentifier);				// TODO pid wirklich int?
	T findProductTypeByProductIdentifier(int productIdentifier);	// TODO
	T findProductTypeByName(String name);
	Iterable<T> findProductTypesByCategory(String category);
	Iterable<CatalogEntry<T>> getCatalogEntries();
}
