package org.salespointframework.core.products;

import java.util.Set;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.products.features.ProductFeatureType;

public abstract class AbstractProductType implements ProductType {
	
	private String name;
	private Money price;
	private Set<ProductFeatureType> featureTypes;
	
	public AbstractProductType(String name, Money price) {
		this.name = name;
		this.price = price;
	}
	
	@Override	
	public Money getPrice() {
		return price;
	}
	
	@Override
	public String getName() {
		return name;
	}

	//TODO cast-sicher machen
	public Iterable<ProductFeatureType> getFeatureTypes() {
		return featureTypes;
	}
}
