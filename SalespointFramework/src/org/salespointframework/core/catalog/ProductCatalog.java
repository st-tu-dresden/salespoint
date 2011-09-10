package org.salespointframework.core.catalog;

import org.salespointframework.core.product.ProductIdentifier;
import org.salespointframework.core.product.ProductType;

public interface ProductCatalog<T extends ProductType> {

	void add(T productType);
	
	void remove(ProductIdentifier productIdentifier);

	boolean contains(T productType);

	<E extends T> E getProductType(Class<E> clazz,
			ProductIdentifier productIdentifier);

	<E extends T> Iterable<E> getProductTypes(Class<E> clazz);

	<E extends T> Iterable<E> findProductTypesByName(
			Class<E> clazz, String name);

	<E extends T> Iterable<E> findProductTypesByCategory(
			Class<E> clazz, String category);
}