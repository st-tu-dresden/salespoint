package org.salespointframework.core.product.features;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.salespointframework.core.money.Money;
import org.salespointframework.util.Objects;

@Entity
public class ProductFeature {

	@Id
	@GeneratedValue
	@SuppressWarnings("unused")
	private int id;
	
	private String name;
	private Money price;
	
	@Deprecated
	protected ProductFeature() { }
	
	private ProductFeature(String name, Money price) {
		this.name = Objects.requireNonNull(name, "name");
		this.price = Objects.requireNonNull(price, "price");
	}
	
	public static ProductFeature create(String name, Money price)  {
		return new ProductFeature(name, price);
	}
	
	public String getName() {
		return name;
	}

	public Money getPrice() {
		return price;
	}
	
	
	@Override
	public boolean equals(Object other) {
		if(other == null) return false;
		if(other == this) return true;
		if(!(other instanceof ProductFeature)) return false;
		return this.equals((ProductFeature)other);
	}
	
	public boolean equals(ProductFeature other) {
		if(other == null) return false;
		if(other == this) return true;
		return this == other || (this.name.equals(other.name) && this.price.equals(other.price));
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(name, price);
	}
	
}
