package org.salespointframework.core.catalog;

import org.salespointframework.core.product.ProductIdentifier;
import org.salespointframework.core.product.Product;

/**
 * Catalog interface
 *
 * @author Paul Henke
 * 
 * @param <T> Base type of the products managed by the catalog; has to
 *            implement {@link Product}.
 */
public interface Catalog<T extends Product>
{
	/**
	 * Adds a new {@link Product} to this <code>Catalog</code>
	 * @param product the {@link Product} to be added
	 * @throws NullPointerException if product is null
	 */
	void add(T product);

	/**
	 * Removes a {@link Product} from the Catalog
	 * @param productIdentifier the {@link ProductIdentifier} of the {@link Product} to be removed
	 * @return true if removal was successful, otherwise false
	 * @throws NullPointerException if productIdentifier is null
	 */
	boolean remove(ProductIdentifier productIdentifier);

	/**
	 * Checks if this Catalog contains a {@link Product}
	 * @param productIdentifier the {@link ProductIdentifier} of the {@link Product}
	 * @return true if the catalog contains the product, otherwise false
	 * @throws NullPointerException if productIdentifier is null
	 */
	boolean contains(ProductIdentifier productIdentifier);

	//TODO mehrzahl?
	/**
	 * Returns the {@link Product} of type <code>clazz</code> and
	 * all sub-types, identified by <code>productIdentifier</code>.
	 * 
	 * @param clazz type of the {@link Product} to be returned; has to implement {@link Product}
	 * @param productIdentifier	the {@link ProductIdentifier} of the {@link Product} to be returned
	 * @return the {@link Product} or a subtype if the productIdentifier matches, otherwise null 
	 * @throws NullPointerException if clazz or productIdentifier are null
	 */
	<E extends T> E get(Class<E> clazz, ProductIdentifier productIdentifier);


	/**
	 * Returns an Iterable of {@link Product}s of Type <code>clazz</code> 
	 * @param clazz type of the {@link Product} to be returned; has to implement {@link Product}
	 * @return an {@link Iterable} containing all {@link Product}s of type clazz
	 * @throws NullPointerException if clazz is null
	 */
	<E extends T> Iterable<E> find(Class<E> clazz);

	/**
	 * Returns an Iterable of {@link Product}s of Type <code>clazz</code> with the name <code>name</name>
	 * @param clazz type of the {@link Product} to be returned; has to implement {@link Product}
	 * @param name the name of the {@link Product}
	 * @return an {@link Iterable} containing all {@link Product}s of type clazz, whose productName matches name
	 * @throws NullPointerException if clazz or name are null
	 */
	<E extends T> Iterable<E> findByName(Class<E> clazz, String name);
	
	/**
	 * Returns an Iterable of {@link Product}s of Type <code>clazz</code> with the category <code>category</name>
	 * @param clazz type of the {@link Product} to be returned; has to implement {@link Product}
	 * @param category the category of the {@link Product}
	 * @return an {@link Iterable} containing all {@link Product}s of type clazz, whose productCategory is category
	 * @throws NullPointerException if clazz or category are null
	 */
	<E extends T> Iterable<E> findByCategory(Class<E> clazz, String category);
}