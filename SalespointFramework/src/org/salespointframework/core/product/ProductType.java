package org.salespointframework.core.product;

import org.salespointframework.core.money.Money;

public interface ProductType {
	String getName();
	int getProductIdentifier();
	Money getPrice();
}
