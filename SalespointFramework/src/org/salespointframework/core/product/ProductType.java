package org.salespointframework.core.product;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.features.ProductFeatureType;

public interface ProductType {
	ProductIdentifier getProductIdentifier();
	String getName();
	Money getPrice();
	Iterable<ProductFeatureType> getProductFeatureTypes(); // TODO umbenennen, je nachdem wie die Klasse heißen wird
	void addProductFeatureType (ProductFeatureType pf);
	void removeProductFeatureType(ProductFeatureType pf);
	
}
