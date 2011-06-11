package org.salespointframework.core.products;

import org.salespointframework.core.money.Money;

public interface ProductInstance<T extends ProductType> {
	String getName();
	Money getPrice();
	T getProductType();
}
