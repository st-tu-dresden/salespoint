package org.salespointframework.inventory;

import org.salespointframework.catalog.ProductIdentifier;

/**
 * An Inventory, aggregates {@link InventoryItem}s
 * 
 * @author Paul Henke
 * 
 */
public interface Inventory
{
	/**
	 * Adds a {@link InventoryItem} to the Inventory
	 * @param inventoryItem an {@link InventoryItem} to be added
	 */
	void add(InventoryItem inventoryItem);
	
	/**
	 * Removes the InventoryItem with the given identifier from this Inventory
	 * @param inventoryItemIdentifier The identifier of the InventoryItem
	 * @return {@literal true} if removal was successful, {@literal false}
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
	<E extends InventoryItem> Iterable<E> find(Class<E> clazz);
	
	/**
	 * Gets an InventoryItem by its unique Identifier
	 * @param clazz common supertype of the InventoryItem returned
	 * @param inventoryItemIdentifier
	 * @return the InventoryItem of type {@code clazz} with the given identifier
	 * @throws NullPointerException if {@code clazz} or {@code inventoryItemIdentifier} are null
	 */
	<E extends InventoryItem> E get(Class<E> clazz, InventoryItemIdentifier inventoryItemIdentifier);
	
	/**
	 * Gets an InventoryItem by the unique {@link ProductIdentifier} of its Product
	 * @param clazz
	 * @param productIdentifier the {@link ProductIdentifier} of the Product in this InventoryItem
	 * @return an InventoryItem or null
	 * @throws NullPointerException if clazz or productIdentifier are null
	 */
	<E extends InventoryItem> E getByProductIdentifier(Class<E> clazz, ProductIdentifier productIdentifier);

	
    /**
     * Updates and persists an existing {@link InventoryItem} to the
     * {@link PersistentInventory} and the Database
     * 
     * @param inventoryItem
     *            the <code>InventoryItem</code> to be updated
     * @throws NullPointerException
     *             if {@code inventoryItem} is {@literal null}
     */
	void update(InventoryItem inventoryItem);
}
