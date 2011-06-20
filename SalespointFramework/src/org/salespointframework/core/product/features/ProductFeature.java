package org.salespointframework.core.product.features;

import org.salespointframework.core.money.Money;
import org.salespointframework.util.Objects;


public class ProductFeature {
	private String feature;
	private Money price;
	
	public ProductFeature(String feature, Money price) {
		this.feature =  Objects.requireNonNull(feature, "feature");
		this.price = Objects.requireNonNull(price, "price");
		
	}
	
	public String getFeature() {
		return feature;
	}

	public Money getPrice() {
		return price;
	}

	// TODO
	@Override
	public boolean equals(Object obj) {
		ProductFeature other = (ProductFeature) obj;
		return this.feature.equals(other.getFeature()) && this.price.equals(other.getPrice());
	};
}
