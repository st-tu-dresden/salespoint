package org.salespointframework.core.catalog;

import org.salespointframework.core.product.ProductIdentifier;
import org.salespointframework.core.product.ProductType;

/**
 * 
 * @author Paul Henke
 * 
 * @param <T>
 */
//TODO fscking javadoc, biatch.
public interface Catalog<T extends ProductType>
{
	void add(T productType);

	boolean remove(ProductIdentifier productIdentifier);

	boolean contains(ProductIdentifier productIdentifier);

	<E extends T> E get(Class<E> clazz, ProductIdentifier productIdentifier);

	<E extends T> Iterable<E> find(Class<E> clazz);

	<E extends T> Iterable<E> findByName(Class<E> clazz, String name);

	<E extends T> Iterable<E> findByCategory(Class<E> clazz, String category);
}