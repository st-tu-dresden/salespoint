package org.salespointframework.core.catalogs;

import org.salespointframework.core.products.ProductType;

public interface CatalogEntry<T extends ProductType> {
	String getDescription();
	Iterable<String> getCategories();
	Iterable<T> getProductTypes();
}
