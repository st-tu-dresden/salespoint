package org.salespointframework.core.inventory;

import org.salespointframework.core.product.ProductInstance;
import org.salespointframework.core.product.ProductType;

//TODO public interface Inventory<T1 extends InventoryEntry<T2>, T2 extends ProductInstance<T3>, T3 extends ProductType> {

public interface Inventory<T1 extends InventoryEntry<T2>, T2 extends ProductInstance<ProductType>> {	 
	void addInventoryEntry(T1 entry);
	boolean removeInventoryEntry(int inventoryEntryIdentifier); // TODO wirklich int?
	Iterable<T1> getInventoryEntries();
}
