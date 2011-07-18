package org.salespointframework.core.product;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.features.ProductFeatureType;
import org.salespointframework.util.Objects;
import org.salespointframework.util.SalespointIterable;

@MappedSuperclass
public class AbstractProductType implements ProductType {
	
	@Id
	protected String productIdentifier;
	protected String name;
	@OneToOne
	protected Money price;
	
	//TODO Map?
	protected Set<ProductFeatureType> featureTypes = new HashSet<ProductFeatureType>() ;
	
	@Deprecated
	protected AbstractProductType() {
		
	}
	
	// TODO public oder protected?
	protected AbstractProductType(String productIdentifier, Money price) {
		this(productIdentifier, productIdentifier, price);
	}
	
	protected AbstractProductType(String productIdentifier, String name, Money price) {
		this.productIdentifier = Objects.requireNonNull(productIdentifier, "productIdentifier");
		this.name = Objects.requireNonNull(name, "name");
		this.price = Objects.requireNonNull(price, "price");
	}
	
	@Override
	public boolean equals(Object productType) {
		if(productType instanceof ProductType) {
			return this.equals((AbstractProductType)productType);
		} else {
			return false;
		}
	}
	
	public boolean equals(AbstractProductType other) {
		return this.productIdentifier.equals(other.productIdentifier);
	}
	
	@Override
	public int hashCode() {
		return productIdentifier.hashCode();
	}
	
	@Override	
	public Money getPrice() {
		return price;
	}

	@Override
	public Iterable<ProductFeatureType> getProductFeatureTypes() {
		return SalespointIterable.from(featureTypes);
	}
	
	@Override
	public String getProductIdentifier() {
		return productIdentifier;
	}


	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
