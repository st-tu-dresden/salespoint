package org.salespointframework.core.product;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.features.ProductFeature;

public interface ProductInstance {
	Money getPrice();
	SerialNumber getSerialNumber();
	ProductIdentifier getProductIdentifier();
	Iterable<ProductFeature> getProductFeatures(); 
	ProductFeature getProductFeature(String name); //TODO notwendig?
}
