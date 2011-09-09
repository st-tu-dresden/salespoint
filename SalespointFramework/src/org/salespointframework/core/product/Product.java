package org.salespointframework.core.product;

import org.salespointframework.core.money.Money;


public interface Product {
	Money getPrice();
	SerialNumber getSerialNumber();
	ProductIdentifier getProductIdentifier();
	Iterable<ProductFeature> getProductFeatures();
}
