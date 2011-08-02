package org.salespointframework.core.product;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.features.ProductFeature;
import org.salespointframework.core.product.features.ProductFeatureType;

public interface ProductInstance {
	Money getPrice();
	SerialNumber getSerialNumber();
	ProductIdentifier getProductIdentifier();
	Iterable<ProductFeature> getProductFeatures();
	ProductFeature getProductFeature(String name); // TODO notwendig?
	void addProductFeatures(ProductFeatureType pf);
	void removeProductFeatures();
}
