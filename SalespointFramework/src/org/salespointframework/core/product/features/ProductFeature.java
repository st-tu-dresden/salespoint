package org.salespointframework.core.product.features;

import javax.persistence.Embeddable;

import org.salespointframework.core.money.Money;
import org.salespointframework.util.Objects;

@Embeddable
public final class ProductFeature {

	private String name;
	private Money price;
	
	private ProductFeature() {}
	
	private ProductFeature(final String name,final Money price) {
		this.name = Objects.requireNonNull(name, "name");
		this.price = Objects.requireNonNull(price, "price");
	}
	
	public static ProductFeature create(final String name)  {
		return new ProductFeature(name, Money.ZERO);
	}
	
	public static ProductFeature create(final String name, final Money price)  {
		return new ProductFeature(name, price);
	}

	public final String getName() {
		return name;
	}

	public final Money getPrice() {
		return price;
	}
	
	@Override
	public final boolean equals(final Object other) {
		if(other == null) return false;
		if(other == this) return true;
		if(!(other instanceof ProductFeature)) return false;
		return this.equals((ProductFeature)other);
	}
	
	public final boolean equals(final ProductFeature other) {
		if(other == null) return false;
		if(other == this) return true;
		return this == other || (this.name.equals(other.name) && this.price.equals(other.price));
	}
	
	@Override
	public final int hashCode() {
		return Objects.hash(name, price);
	}
}
