package org.salespointframework.core.inventory;

import org.salespointframework.core.product.ProductInstance;
import org.salespointframework.core.product.ProductFeature;
import org.salespointframework.core.product.ProductIdentifier;
import org.salespointframework.core.product.SerialNumber;
import org.salespointframework.util.ArgumentNullException;

/**
 * Inventory interface
 * An inventory aggregates productinstances
 * @author Paul Henke
 * 
 * @param <T> Base type of the productinstances managed by the inventory; has to
 *            implement {@link ProductInstance}.

 */
public interface Inventory<T extends ProductInstance>
{

	/**
	 * Adds a new {@link ProductInstance} to the Inventory
	 * @param product the {@link ProductInstance} to be added
	 * @throws ArgumentNullException if product is null
	 */
	void add(T product);

	/**
	 * Removes a {@link ProductInstance} from the Inventory
	 * @param serialNumber the {@link SerialNumber} of the {@link ProductInstance} to be removed
	 * @return true is removal was successful, otherwise false
	 * @throws ArgumentNullException if serialNumber is null
	 */
	boolean remove(SerialNumber serialNumber);

	/**
	 * Checks if this Inventory contains a {@link ProductInstance}
	 * @param serialNumber the {@link SerialNumber} of the {@link ProductInstance}
	 * @return true if the Inventory contains the {@link ProductInstance}, otherwise false
	 * @throws ArgumentNullException if serialNumber is null
	 */
	boolean contains(SerialNumber serialNumber);

	/**
	 * Returns the {@link ProductInstance} of type <code>clazz</code> and
	 * all sub-types, identified by {@link SerialNumber}.
	 * @param clazz type of the productinstance to be returned; has to implement {@link ProductInstance} 
	 * @param serialNumber the {@link SerialNumber} of the {@link ProductInstance} to be returned
	 * @return a {@link ProductInstance} or a subtype if the serialNumber matches, otherwise null 
	 * @throws ArgumentNullException if clazz or serialNumber are null
	 */
	<E extends T> E get(Class<E> clazz, SerialNumber serialNumber);

	/**
	 * Returns all {@link ProductInstance}s of type <code>clazz</code>
	 * @param clazz type of the productinstance to be returned; has to implement {@link ProductInstance}
	 * @return an {@link Iterable} containing all {@link ProductInstance}s of type clazz
	 * @throws ArgumentNullException if clazz is null
	 */
	<E extends T> Iterable<E> find(Class<E> clazz);

	/**
	 * Returns all {@link ProductInstance}s of type <code>clazz</code>, whose {@link ProductIdentifier} matches productIdentifier
	 * @param clazz type of the productinstance to be returned; has to implement {@link ProductInstance}
	 * @param productIdentifier the {@link ProductIdentifier} of the {@link ProductInstance}s to be returned
	 * @return an {@link Iterable} containing all {@link ProductInstance}s of type clazz and the given productIdentifier
 	 * @throws ArgumentNullException if clazz or productIdentifier are null
	 */
	<E extends T> Iterable<E> find(Class<E> clazz, ProductIdentifier productIdentifier);

	/**
	 * 
	 * @param clazz type of the Product to be returned; has to implement
	 *            <code>Product</code>
	 * @param productIdentifier the {@link ProductIdentifier} of the {@link ProductInstance} to be returned
	 * @param productFeatures the {@link ProductFeature} of the {@link ProductInstance} to be returned
	 * @return an {@link Iterable} containing all {@link ProductInstance}s of type clazz, whose {@link ProductIdentifier} matches productIdentifier and whose {@link ProductFeature}s matches productFeatures
	 * @throws ArgumentNullException if clazz or productIdentifier or productFeatures are null
	 */
	<E extends T> Iterable<E> find(Class<E> clazz, ProductIdentifier productIdentifier, Iterable<ProductFeature> productFeatures);
	
	/**
	 * Returns the number of {@link ProductInstance}s in this inventory whose {@link ProductIdentifier} is productIdentifier
	 * @param productIdentifier a {@link ProductIdentifier}
	 * @return a long value
	 * @throws ArgumentNullException if productIdentifier is null
	 */
	long count(ProductIdentifier productIdentifier);
	
	/**
	 * Returns the number of {@link ProductInstance}s in this inventory whose {@link ProductIdentifier} is productIdentifier and whose {@link ProductFeature}s is productFeatures
	 * @param productIdentifier a {@link ProductIdentifier}
	 * @param productFeatures  {@link ProductFeature}
	 * @return a long value
	 * @throws ArgumentNullException if productIdentifier or productFeatures are null
	 */
	long count(ProductIdentifier productIdentifier, Iterable<ProductFeature> productFeatures);
}
