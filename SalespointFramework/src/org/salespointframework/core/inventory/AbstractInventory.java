package org.salespointframework.core.inventory;

import org.salespointframework.core.product.AbstractProductInstance;
import org.salespointframework.core.product.AbstractProductType;

// TODO abstract

public class AbstractInventory<T1 extends AbstractInventoryEntry<T2, T3>, T2 extends AbstractProductInstance<T3>, T3 extends AbstractProductType> 
implements Inventory<T1,T2,T3> {

	@Override
	public void addInventoryEntry(T1 entry) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean removeInventoryEntry(int inventoryEntryIdentifier) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterable<T1> getInventoryEntries() {
		// TODO Auto-generated method stub
		return null;
	} 


}
