package org.salespointframework.core.inventory;

import org.salespointframework.core.product.Product;
import org.salespointframework.core.product.ProductFeature;
import org.salespointframework.core.product.ProductTypeIdentifier;
import org.salespointframework.core.product.ProductIdentifier;
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
	 * @param product the {@link Product} to be added
	 * @throws ArgumentNullException if product is null
	 */
	void add(T product);

	/**
	 * Removes a Product from the Inventory
	 * @param productIdentifier the {@link ProductIdentifier} of the Product to be removed
	 * @return true is removal was successful, otherwise false
	 * @throws ArgumentNullException if productIdentifier is null
	 */
	boolean remove(ProductIdentifier productIdentifier);

	/**
	 * Checks if this Inventory contains a Product
	 * @param productIdentifier the {@link ProductIdentifier} of the Product
	 * @return true if the Inventory contains the Product, otherwise false
	 * @throws ArgumentNullException if productIdentifier is null
	 */
	boolean contains(ProductIdentifier productIdentifier);

	/**
	 * Returns the <code>Product</code> of type <code>clazz</code> and
	 * all sub-types, identified by {@link ProductIdentifier}.
	 * @param clazz type of the Product to be returned; has to implement
	 *            <code>Product</code>
	 * @param productIdentifier the {@link ProductIdentifier} of the Product to be returned
	 * @return  the Product or a subtype if the productIdentifier matches, otherwise null 
	 * @throws ArgumentNullException if clazz or productIdentifier are null
	 */
	<E extends T> E get(Class<E> clazz, ProductIdentifier productIdentifier);

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
	 * @param productTypeIdentifier the {@link ProductTypeIdentifier} of the Product to be returned
	 * @return an {@link Iterable} containing all Products of type clazz and the given productIdentifier
 	 * @throws ArgumentNullException if clazz or productTypeIdentifier are null
	 */
	<E extends T> Iterable<E> find(Class<E> clazz, ProductTypeIdentifier productTypeIdentifier);

	/**
	 * 
	 * @param clazz type of the Product to be returned; has to implement
	 *            <code>Product</code>
	 * @param productTypeIdentifier the {@link ProductTypeIdentifier} of the Product to be returned
	 * @param productFeatures the {@link ProductFeature} of the Product to be returned
	 * @return an {@link Iterable} containing all {@link Product}s of type clazz, whose {@link ProductTypeIdentifier} matches productTypeIdentifier and whose {@link ProductFeature}s matches productFeatures
	 * @throws ArgumentNullException if clazz or productTypeIdentifier or productFeatures are null
	 */
	<E extends T> Iterable<E> find(Class<E> clazz, ProductTypeIdentifier productTypeIdentifier, Iterable<ProductFeature> productFeatures);
	
	// TODO comment
	long count(ProductTypeIdentifier productTypeIdentifier);
	// TODO comment
	long count(ProductTypeIdentifier productTypeIdentifier, Iterable<ProductFeature> productFeatures);
}
