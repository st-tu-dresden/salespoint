package org.salespointframework.core.products;

import java.util.Set;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.products.features.ProductFeatureType;

public abstract class AbstractProductType implements ProductType {
	private Set<ProductFeatureType> featureTypes;
	private Money price;

	@Override	
	public Money getPrice() {
		return price;
	}
	
	//TODO cast-sicher machen
	public Iterable<ProductFeatureType> getFeatureTypes() {
		return featureTypes;
	}
	
	
	
}
