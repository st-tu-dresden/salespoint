package org.salespointframework.core.inventory;

import org.salespointframework.core.product.ProductIdentifier;

/**
 * 
 * @author Paul Henke
 * 
 */
public interface Inventory<T extends InventoryItem>
{
	void add(T inventoryItem);
	boolean remove(InventoryItemIdentifier inventoryItemIdentifier);
	boolean contains(InventoryItemIdentifier inventoryItemIdentifier);
	
	<E extends T> E get(Class<E> clazz, InventoryItemIdentifier inventoryItemIdentifier);
	<E extends T> E getByProductIdentifier(Class<E> clazz, ProductIdentifier productIdentifier);

	<E extends T> Iterable<E> find(Class<E> clazz);
}
