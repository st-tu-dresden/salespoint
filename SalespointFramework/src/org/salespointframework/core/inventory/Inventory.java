package org.salespointframework.core.inventory;

import org.salespointframework.core.product.Product;
import org.salespointframework.core.product.ProductFeature;
import org.salespointframework.core.product.ProductIdentifier;
import org.salespointframework.core.product.SerialNumber;

/**
 * 
 * @author Paul
 *
 * @param <T>
 */
public interface Inventory<T extends Product> {

	void add(T product);
	
	boolean remove(SerialNumber serialNumber);
	
	boolean contains(SerialNumber serialNumber);
	
	<E extends T> E get(Class<E> clazz, SerialNumber serialNumber);

	<E extends T> Iterable<E> find(Class<E> clazz);

	<E extends T> Iterable<E> find(Class<E> clazz, ProductIdentifier productType);
	
	<E extends T> Iterable<E> find(Class<E> clazz, ProductIdentifier productType, Iterable<ProductFeature> productFeatures);
}
