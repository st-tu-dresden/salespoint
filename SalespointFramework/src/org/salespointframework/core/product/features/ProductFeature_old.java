package org.salespointframework.core.product.features;

import javax.persistence.Embeddable;

import org.salespointframework.core.money.Money;
import org.salespointframework.util.Objects;

@Embeddable
public final class ProductFeature_old {

	private String name;
	private Money price;
	
	private ProductFeature_old() {}
	
	private ProductFeature_old(final String name,final Money price) {
		this.name = Objects.requireNonNull(name, "name");
		this.price = Objects.requireNonNull(price, "price");
	}
	
	public static ProductFeature_old create(final String name)  {
		return new ProductFeature_old(name, Money.ZERO);
	}
	
	public static ProductFeature_old create(final String name, final Money price)  {
		return new ProductFeature_old(name, price);
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
		if(!(other instanceof ProductFeature_old)) return false;
		return this.equals((ProductFeature_old)other);
	}
	
	public final boolean equals(final ProductFeature_old other) {
		if(other == null) return false;
		if(other == this) return true;
		return this == other || (this.name.equals(other.name) && this.price.equals(other.price));
	}
	
	@Override
	public final int hashCode() {
		return Objects.hash(name, price);
	}
}
