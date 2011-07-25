package org.salespointframework.core.inventory;

import org.salespointframework.core.database.ICanHasClass;
import org.salespointframework.core.product.AbstractProductInstance;
import org.salespointframework.core.product.AbstractProductType;
import org.salespointframework.core.product.ProductType;
import org.salespointframework.core.product.SerialNumber;

// TODO abstract

//public class AbstractInventory<T1 extends AbstractInventoryEntry<T2, T3>, T2 extends AbstractProductInstance<T3>, T3 extends AbstractProductType> 
//implements Inventory<T1,T2,T3> {

public class AbstractInventory<T extends AbstractProductInstance<AbstractProductType>> implements Inventory<T>, ICanHasClass<T> {

	@Override
	public Class<T> getContentClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addProductInstance(T entry) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeProductInstance(SerialNumber serialNumber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public T getProductInstance(SerialNumber serialNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<T> getProductInstances() {
		// TODO Auto-generated method stub
		return null;
	}


	
}
