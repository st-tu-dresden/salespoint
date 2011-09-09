package org.salespointframework.core.product;

import org.salespointframework.core.money.Money;

public interface ProductType {
	ProductIdentifier getProductIdentifier();
	String getName();
	Money getPrice();
	boolean addProductFeature(ProductFeature productFeature);
	boolean removeProductFeature(ProductFeature productFeature);
	Iterable<ProductFeature> getProductFeatures();
}
