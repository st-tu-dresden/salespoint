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
public interface Inventory<T extends Product>
{

	/**
	 * 
	 * @param product
	 */
	void add(T product);

	/**
	 * 
	 * @param serialNumber
	 * @return
	 */
	boolean remove(SerialNumber serialNumber);

	/**
	 * 
	 * @param serialNumber
	 * @return
	 */
	boolean contains(SerialNumber serialNumber);

	/**
	 * 
	 * @param clazz
	 * @param serialNumber
	 * @return
	 */
	<E extends T> E get(Class<E> clazz, SerialNumber serialNumber);

	/**
	 * 
	 * @param clazz
	 * @return
	 */
	<E extends T> Iterable<E> find(Class<E> clazz);

	/**
	 * 
	 * @param clazz
	 * @param productType
	 * @return
	 */
	<E extends T> Iterable<E> find(Class<E> clazz, ProductIdentifier productType);

	/**
	 * 
	 * @param clazz
	 * @param productType
	 * @param productFeatures
	 * @return
	 */
	<E extends T> Iterable<E> find(Class<E> clazz, ProductIdentifier productType, Iterable<ProductFeature> productFeatures);
}
