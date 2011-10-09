package org.salespointframework.core.inventory;

import org.salespointframework.core.product.ProductInstance;
import org.salespointframework.core.product.ProductFeature;
import org.salespointframework.core.product.ProductIdentifier;
import org.salespointframework.core.product.SerialNumber;
import org.salespointframework.util.ArgumentNullException;

/**
 * Inventory interface
 * An inventory aggregates products
 * @author Paul Henke
 * 
 * @param <T> Base type of the Products managed by the inventory; has to
 *            implement <code>Product</code>.

 */
public interface Inventory<T extends ProductInstance>
{

	/**
	 * Adds a new Product to the Inventory
	 * @param product the {@link ProductInstance} to be added
	 * @throws ArgumentNullException if product is null
	 */
	void add(T product);

	/**
	 * Removes a Product from the Inventory
	 * @param serialNumber the {@link SerialNumber} of the Product to be removed
	 * @return true is removal was successful, otherwise false
	 * @throws ArgumentNullException if serialNumber is null
	 */
	boolean remove(SerialNumber serialNumber);

	/**
	 * Checks if this Inventory contains a Product
	 * @param serialNumber the {@link SerialNumber} of the Product
	 * @return true if the Inventory contains the Product, otherwise false
	 * @throws ArgumentNullException if serialNumber is null
	 */
	boolean contains(SerialNumber serialNumber);

	/**
	 * Returns the <code>Product</code> of type <code>clazz</code> and
	 * all sub-types, identified by {@link SerialNumber}.
	 * @param clazz type of the Product to be returned; has to implement
	 *            <code>Product</code>
	 * @param serialNumber the {@link SerialNumber} of the Product to be returned
	 * @return  the Product or a subtype if the serialNumber matches, otherwise null 
	 * @throws ArgumentNullException if clazz or serialNumber are null
	 */
	<E extends T> E get(Class<E> clazz, SerialNumber serialNumber);

	/**
	 * 
	 * @param clazz type of the Product to be returned; has to implement
	 *            <code>Product</code>
	 * @return an {@link Iterable} containing all Products of type clazz
	 * @throws ArgumentNullException if clazz is null
	 */
	<E extends T> Iterable<E> find(Class<E> clazz);

	/**
	 * 
	 * @param clazz type of the Product to be returned; has to implement
	 *            <code>Product</code>
	 * @param productIdentifier the {@link ProductIdentifier} of the Product to be returned
	 * @return an {@link Iterable} containing all Products of type clazz and the given serialNumber
 	 * @throws ArgumentNullException if clazz or productIdentifier are null
	 */
	<E extends T> Iterable<E> find(Class<E> clazz, ProductIdentifier productIdentifier);

	/**
	 * 
	 * @param clazz type of the Product to be returned; has to implement
	 *            <code>Product</code>
	 * @param productIdentifier the {@link ProductIdentifier} of the Product to be returned
	 * @param productFeatures the {@link ProductFeature} of the Product to be returned
	 * @return an {@link Iterable} containing all {@link ProductInstance}s of type clazz, whose {@link ProductIdentifier} matches productIdentifier and whose {@link ProductFeature}s matches productFeatures
	 * @throws ArgumentNullException if clazz or productIdentifier or productFeatures are null
	 */
	<E extends T> Iterable<E> find(Class<E> clazz, ProductIdentifier productIdentifier, Iterable<ProductFeature> productFeatures);
	
	// TODO comment
	long count(ProductIdentifier productIdentifier);
	// TODO comment
	long count(ProductIdentifier productIdentifier, Iterable<ProductFeature> productFeatures);
}
