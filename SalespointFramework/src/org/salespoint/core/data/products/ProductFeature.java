package org.salespoint.core.data.products;

import org.salespoint.core.data.money.Money;


public class ProductFeature {
	private String feature;
	private Money price;
	
	public ProductFeature(String feature, Money price) {
		this.feature = feature;
		this.price = price;
	}
	
	public String getFeature() {
		return feature;
	}

	public Money getPrice() {
		
		return price;
	}

	@Override
	public boolean equals(Object obj) {
		ProductFeature other = (ProductFeature) obj;
		return this.feature.equals(other.getFeature()) && this.price.equals(other.getPrice());
	};
}
