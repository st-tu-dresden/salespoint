package org.salespointframework.core.catalog;

import org.salespointframework.core.product.ProductIdentifier;
import org.salespointframework.core.product.ProductType;
import org.salespointframework.util.ArgumentNullException;

/**
 * Catalog interface
 * TODO
 * @author Paul Henke
 * 
 * @param <T> Base type of the ProductTypes managed by the catalog; has to
 *            implement {@link ProductType}.
 */
public interface Catalog<T extends ProductType>
{
	/**
	 * Adds a new {@link ProductType} to this <code>Catalog</code>
	 * @param productType the {@link ProductType} to be added
	 * @throws ArgumentNullException if productType is null
	 */
	void add(T productType);

	/**
	 * Removes a {@link ProductType} from the Catalog
	 * @param productIdentifier the {@link ProductIdentifier} of the {@link ProductType} to be removed
	 * @return true if removal was successful, otherwise false
	 * @throws ArgumentNullException if productIdentifier is null
	 */
	boolean remove(ProductIdentifier productIdentifier);

	/**
	 * Checks if this Catalog contains a {@link ProductType}
	 * @param productIdentifier the {@link ProductIdentifier} of the {@link ProductType}
	 * @return true if the catalog contains the ProductType, otherwise false
	 * @throws ArgumentNullException if productIdentifier is null
	 */
	boolean contains(ProductIdentifier productIdentifier);

	//TODO mehrzahl?
	/**
	 * Returns the {@link ProductType} of type <code>clazz</code> and
	 * all sub-types, identified by <code>productIdentifier</code>.
	 * 
	 * @param clazz type of the {@link ProductType} to be returned; has to implement {@link ProductType}
	 * @param productIdentifier	the {@link ProductIdentifier} of the {@link ProductType} to be returned
	 * @return the ProductType or a subtype if the productIdentifier matches, otherwise null 
	 * @throws ArgumentNullException if clazz or productIdentifier are null
	 */
	<E extends T> E get(Class<E> clazz, ProductIdentifier productIdentifier);

	/**
	 * TODO
	 * @param clazz type of the {@link ProductType} to be returned; has to implement {@link ProductType}
	 * @return an Iterable containing all {@link ProductType}s of type clazz
	 * @throws ArgumentNullException if clazz is null
	 */
	<E extends T> Iterable<E> find(Class<E> clazz);

	/**
	 * @param clazz type of the {@link ProductType} to be returned; has to implement {@link ProductType}
	 * @param name the name of the {@link ProductType}
	 * @return an Iterable containing all {@link ProductType}s of type clazz, whose productName matches name
	 * @throws ArgumentNullException if clazz or name are null
	 */
	<E extends T> Iterable<E> findByName(Class<E> clazz, String name);
	
	/**
	 * @param clazz type of the {@link ProductType} to be returned; has to implement {@link ProductType}
	 * @param category the category of the {@link ProductType}
	 * @return an Iterable containing all {@link ProductType}s of type clazz, whose productCategory is category
	 * @throws ArgumentNullException if clazz or category are null
	 */
	<E extends T> Iterable<E> findByCategory(Class<E> clazz, String category);
}