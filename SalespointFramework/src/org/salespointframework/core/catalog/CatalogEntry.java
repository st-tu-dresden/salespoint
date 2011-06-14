package org.salespointframework.core.catalog;

import org.salespointframework.core.product.ProductType;

public interface CatalogEntry<T extends ProductType> {
	String getDescription();
	Iterable<String> getCategories();
	Iterable<T> getProductTypes();
}
