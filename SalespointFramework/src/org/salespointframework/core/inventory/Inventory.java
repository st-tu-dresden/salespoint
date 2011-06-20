package org.salespointframework.core.inventory;

import org.salespointframework.core.product.ProductInstance;
import org.salespointframework.core.product.ProductType;

public interface Inventory<T1 extends InventoryEntry<T2, T3>, T2 extends ProductInstance<T3>, T3 extends ProductType> {
	void addInventoryEntry(T1 entry);
	boolean removeInventoryEntry(int inventoryEntryIdentifier); // TODO wirklich int?
	Iterable<T1> getInventoryEntries();
}
