package org.salespointframework.core.product;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.salespointframework.core.money.Money;
import org.salespointframework.util.Iterables;
import org.salespointframework.util.Objects;

/**
 * 
 * @author Paul Henke
 * 
 */
@Entity
@Embeddable
public class PersistentProductType implements ProductType {
	
	@EmbeddedId
	private ProductIdentifier productIdentifier;

	private String name;
	private Money price;
	
	//@OneToMany(cascade = CascadeType.ALL)
	@ElementCollection
	private Set<ProductFeature> productFeatures = new HashSet<ProductFeature>();
	
	@ElementCollection
	Set<String> categories = new HashSet<String>();
	
	@Deprecated
	protected PersistentProductType() { }
	
	public PersistentProductType(String name, Money price) {
		this.name = Objects.requireNonNull(name, "name");
		this.price = Objects.requireNonNull(price, "price");
		this.productIdentifier = new ProductIdentifier();
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == null) return false;
		if(other == this) return true;
		if(!(other instanceof ProductType)) return false;
		return this.equals((ProductType)other);
	}
	
	public final boolean equals(ProductType other) {
		if(other == null) return false;
		if(other == this) return true;
		return this.productIdentifier.equals(other.getProductIdentifier());
	}
	
	@Override
	public final int hashCode() {
		return productIdentifier.hashCode();
	}
	
	@Override
	public final String getName() {
		return name;
	}
	
	@Override	
	public Money getPrice() {
		return price;
	}

	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public final Iterable<ProductFeature> getProductFeatures() {
		return Iterables.from(productFeatures);
	}
	
	@Override
	public final ProductIdentifier getProductIdentifier() {
		return productIdentifier;
	}

	@Override
	public final boolean addProductFeature(ProductFeature productFeature){
		Objects.requireNonNull(productFeature, "productFeature");
		return productFeatures.add(productFeature);
	}
	
	@Override
	public final boolean removeProductFeature(ProductFeature productFeature){
		Objects.requireNonNull(productFeature, "productFeature");
		return productFeatures.remove(productFeature);
	}

	@Override
	public boolean addCategory(String category) {
		return categories.add(category);
	}

	@Override
	public boolean removeCategory(String category) {
		return categories.remove(category);
	}

	@Override
	public Iterable<String> getCategories() {
		return Iterables.from(categories);
	}
}
