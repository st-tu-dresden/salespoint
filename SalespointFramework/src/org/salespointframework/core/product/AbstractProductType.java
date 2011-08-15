package org.salespointframework.core.product;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.features.ProductFeatureType;
import org.salespointframework.util.Iterables;
import org.salespointframework.util.Objects;


@MappedSuperclass
public class AbstractProductType implements ProductType {
	
	@EmbeddedId
	private ProductIdentifier productIdentifier;

	private String name;
	private Money price;
	
	//TODO Map?
	@OneToMany(cascade = CascadeType.ALL)
	private Set<ProductFeatureType> featureTypes = new HashSet<ProductFeatureType>();
	
	
	@Deprecated
	protected AbstractProductType() { }
	
	public AbstractProductType(String name, Money price) {
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
	public final Iterable<ProductFeatureType> getProductFeatureTypes() {
		return Iterables.from(featureTypes);
	}
	
	@Override
	public final ProductIdentifier getProductIdentifier() {
		return productIdentifier;
	}

	@Override
	public final void addProductFeatureType (ProductFeatureType productFeatureType){
		Objects.requireNonNull(productFeatureType, "productFeatureType");
		featureTypes.add(productFeatureType);
		
	}
	
	@Override
	public final void removeProductFeatureType (ProductFeatureType productFeatureType){
		Objects.requireNonNull(productFeatureType, "productFeatureType");
		featureTypes.remove(productFeatureType);
	}
	

	
}
