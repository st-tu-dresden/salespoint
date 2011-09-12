package org.salespointframework.core.inventory;

import org.salespointframework.core.product.Product;
import org.salespointframework.core.product.SerialNumber;

// TODO Legacy Code, DELETE


//Vollständig getypt
//public interface Inventory<T1 extends InventoryEntry<T2, T3>, T2 extends ProductInstance<T3>, T3 extends ProductType> {
public interface Inventory<T extends Product> {
	void addProductInstance(T productInstance);
	void removeProductInstance(SerialNumber serialNumber);
	boolean contains(SerialNumber serialNumber);
	T getProductInstance(SerialNumber serialNumber);
	Iterable<T> getProductInstances();
	
}
