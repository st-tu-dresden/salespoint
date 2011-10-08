package org.salespointframework.core.inventory;

import org.salespointframework.core.product.Product;
import org.salespointframework.core.product.ProductFeature;
import org.salespointframework.core.product.ProductTypeIdentifier;
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
public interface Inventory<T extends Product>
{

	/**
	 * Adds a new Product to the Inventory
	 * @param product the Product to be added
	 * @throws ArgumentNullException if product is null
	 */
	void add(T product);

	/**
	 * Removes a Product from the Inventory
	 * @param serialNumber the SerialNumber of the Product to be removed
	 * @return true is removal was successful, otherwise false
	 * @throws ArgumentNullException if serialNumber is null
	 */
	boolean remove(SerialNumber serialNumber);

	/**
	 * Checks if this Inventory contains a Product
	 * @param serialNumber the SerialNumer of the Product
	 * @return true if the Inventory contains the Product, otherwise false
	 * @throws ArgumentNullException if serialNumber is null
	 */
	boolean contains(SerialNumber serialNumber);

	/**
	 * Returns the <code>Product</code> of type <code>clazz</code> and
	 * all sub-types, identified by SerialNumber.
	 * @param clazz type of the Product to be returned; has to implement
	 *            <code>Product</code>
	 * @param serialNumber the SerialNumber of the Product to be returned
	 * @return  the Product or a subtype if the serialNumber matches, otherwise null 
	 * @throws ArgumentNullException if clazz or serialNumber are null
	 */
	<E extends T> E get(Class<E> clazz, SerialNumber serialNumber);

	/**
	 * 
	 * @param clazz type of the Product to be returned; has to implement
	 *            <code>Product</code>
	 * @return an Iterable containing all Products of type clazz
	 * @throws ArgumentNullException if clazz is null
	 */
	<E extends T> Iterable<E> find(Class<E> clazz);

	/**
	 * 
	 * @param clazz type of the Product to be returned; has to implement
	 *            <code>Product</code>
	 * @param productIdentifier the {@link ProductTypeIdentifier} of the Product to be returned
	 * @return an Iterable containing all Products of type clazz and the given productIdentifier
 	 * @throws ArgumentNullException if clazz or productType are null
	 */
	<E extends T> Iterable<E> find(Class<E> clazz, ProductTypeIdentifier productIdentifier);

	/**
	 * 
	 * @param clazz type of the Product to be returned; has to implement
	 *            <code>Product</code>
	 * @param productIdentifier the {@link ProductTypeIdentifier} of the Product to be returned
	 * @param productFeatures the ProductFeatures of the Product to be returned
	 * @return an Iterable containing all {@link Product}s of type clazz, whose {@link ProductTypeIdentifier} matches productIdentifier and whose {@link ProductFeature}s matches productFeatures
	 * @throws ArgumentNullException if clazz or productType or productFeatures are null
	 */
	<E extends T> Iterable<E> find(Class<E> clazz, ProductTypeIdentifier productIdentifier, Iterable<ProductFeature> productFeatures);
}
