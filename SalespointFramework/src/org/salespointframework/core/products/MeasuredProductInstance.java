package org.salespointframework.core.products;

public abstract class MeasuredProductInstance<T extends MeasuredProductType> extends ProductInstance<T> {

	public MeasuredProductInstance(T productType) {
		super(productType);
	}

}
