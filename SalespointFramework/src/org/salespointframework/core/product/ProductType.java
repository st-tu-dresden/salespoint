package org.salespointframework.core.product;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.features.ProductFeatureType_old;

public interface ProductType {
	ProductIdentifier getProductIdentifier();
	String getName();
	Money getPrice();
	boolean addProductFeature(ProductFeature productFeature);
	boolean removeProductFeature(ProductFeature productFeature);
	Iterable<ProductFeature> getProductFeatures();
}
