package org.salespointframework.core.product;

import org.salespointframework.core.money.Money;

/**
 * 
 * @author Paul Henke
 * 
 */
public interface Product {
	Money getPrice();
	SerialNumber getSerialNumber();
	ProductIdentifier getProductIdentifier();
	Iterable<ProductFeature> getProductFeatures();
}
