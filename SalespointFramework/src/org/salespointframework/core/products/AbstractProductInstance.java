package org.salespointframework.core.products;

import org.salespointframework.core.money.Money;

public abstract class AbstractProductInstance<T extends ProductType> implements ProductInstance<T> {
	private T productType;
	
	public AbstractProductInstance(T productType) {
		this.productType = productType;
	}

	@Override
	public T getProductType() { 
		return productType;
	}

	@Override
	public Money getPrice() {
		// TODO Auto-generated method stub
		return null;
	}
}
