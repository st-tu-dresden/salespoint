package org.salespointframework.core.product;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.features.ProductFeature_old;

public interface Product {
	Money getPrice();
	SerialNumber getSerialNumber();
	ProductIdentifier getProductIdentifier();
	Iterable<ProductFeature> getProductFeatures();
}
