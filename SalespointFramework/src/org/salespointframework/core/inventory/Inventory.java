package org.salespointframework.core.inventory;

import org.salespointframework.core.product.ProductIdentifier;

/**
 * An Inventory, aggregates {@link InventoryItem}s
 * 
 * @author Paul Henke
 * @param <T> Base type of the InventoryItems managed by the Inventory; has to
 *            implement {@link InventoryItem}
 * 
 */
public interface Inventory<T extends InventoryItem>
{
	/**
	 * Adds a {@link InventoryItem} to the Inventory
	 * @param inventoryItem an {@link InventoryItem} to be added
	 */
	void add(T inventoryItem);
	
	/**
	 * Removes the InventoryItem with the given identifier from this Inventory
	 * @param inventoryItemIdentifier The identifier of the InventoryItem
	 * @return <code>true</code> if removal was successful, <code>false</code>
     *         otherwise.
     * @throws NullPointerException if inventoryItemIdentifier is null
     * 
	 */
	boolean remove(InventoryItemIdentifier inventoryItemIdentifier);
	
	/**
	 * Checks if Inventorys contains an InventoryItem
	 * @param inventoryItemIdentifier the Identifier of an InventoryItem
	 * @return true if Inventory contains the InventoryItem, otherwise false
	 * @throws NullPointerException if inventoryItemIdentifier is null
	 */
	boolean contains(InventoryItemIdentifier inventoryItemIdentifier);

	/**
	 * Gets all InventoryItems class type {@code clazz}
	 * @param clazz common supertype of InventoryItems returned
	 * @return all InventoryItems of {@code clazz}
	 * @throws NullPointerException if inventoryItemIdentifier is null
	 */
	<E extends T> Iterable<E> find(Class<E> clazz);
	
	/**
	 * Gets an InventoryItem by its unique Identifier
	 * @param clazz common supertype of the InventoryItem returned
	 * @param inventoryItemIdentifier
	 * @return the InventoryItem of type {@code clazz} with the given identifier
	 * @throws NullPointerException if clazz or inventoryItemIdentifier are null
	 */
	<E extends T> E get(Class<E> clazz, InventoryItemIdentifier inventoryItemIdentifier);
	
	/**
	 * Gets an InventoryItem by the unique {@link ProductIdentifier} of its Product
	 * @param clazz
	 * @param productIdentifier the {@link ProductIdentifier} of the Product in this InventoryItem
	 * @return an InventoryItem or null
	 * @throws NullPointerException if clazz or productIdentifier are null
	 */
	<E extends T> E getByProductIdentifier(Class<E> clazz, ProductIdentifier productIdentifier);


}
