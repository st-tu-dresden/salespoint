package org.salespointframework.core.products;

import org.salespointframework.core.money.Money;

public interface ProductType {
	String getName();
	int getProductIdentifier();
	Money getPrice();
}
