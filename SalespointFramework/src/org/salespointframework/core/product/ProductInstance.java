package org.salespointframework.core.product;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.features.ProductFeature;

public interface ProductInstance<T extends ProductType> {
	Money getPrice();
	T getProductType();
	long getSerialNumber();
	Iterable<ProductFeature> getProductFeatures(); 
	ProductFeature getProductFeature(String name); //TODO notwendig?
}
