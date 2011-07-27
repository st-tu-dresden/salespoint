package org.salespointframework.core.inventory;

import org.salespointframework.core.product.ProductInstance;
import org.salespointframework.core.product.SerialNumber;

//public interface Inventory<T1 extends InventoryEntry<T2, T3>, T2 extends ProductInstance<T3>, T3 extends ProductType> {
public interface Inventory<T extends ProductInstance> {
	
	void addProductInstance(T productInstance);
	void removeProductInstance(SerialNumber serialNumber);
	boolean contains(SerialNumber serialNumber);
	T getProductInstance(SerialNumber serialNumber);
	Iterable<T> getProductInstances();
	
}
