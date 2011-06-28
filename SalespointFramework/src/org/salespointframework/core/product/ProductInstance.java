package org.salespointframework.core.product;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.features.ProductFeature;

public interface ProductInstance<T extends ProductType> {
	String getName();
	Money getPrice();
	T getProductType();
	int getSerialNumber();
	Iterable<ProductFeature> getProductTypes(); 
	ProductFeature getProductFeature(String name); //TODO notwendig?
}
