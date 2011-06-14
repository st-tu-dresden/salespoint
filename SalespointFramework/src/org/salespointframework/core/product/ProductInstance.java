package org.salespointframework.core.product;

import org.salespointframework.core.money.Money;

public interface ProductInstance<T extends ProductType> {
	String getName();
	Money getPrice();
	T getProductType();
	int getSerialNumber();
}
