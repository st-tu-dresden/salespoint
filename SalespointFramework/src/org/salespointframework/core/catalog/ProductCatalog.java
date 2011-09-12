package org.salespointframework.core.catalog;

import org.salespointframework.core.product.ProductIdentifier;
import org.salespointframework.core.product.ProductType;

/**
 * 
 * @author Paul Henke
 *
 * @param <T>
 */
public interface ProductCatalog<T extends ProductType> {

	void add(T productType);
	
	void remove(ProductIdentifier productIdentifier);

	boolean contains(ProductIdentifier productIdentifier);

	<E extends T> E get(Class<E> clazz, ProductIdentifier productIdentifier);

	<E extends T> Iterable<E> findProductTypes(Class<E> clazz);

	<E extends T> Iterable<E> findProductTypesByName(Class<E> clazz, String name);
	
	<E extends T> Iterable<E> findProductTypesByCategory(Class<E> clazz, String category);
}