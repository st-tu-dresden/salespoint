package org.salespointframework.core.products;

import java.util.Set;

import org.salespointframework.core.money.Money;

public abstract class ProductType {
	private Set<ProductFeatureType> featureTypes;
	private Money price;
	
	public Money getPrice() {
		return price;
	}
	
	//TODO dau
	public Iterable<ProductFeatureType> getFeatureTypes() {
		return featureTypes;
	}
	
	
	
}
