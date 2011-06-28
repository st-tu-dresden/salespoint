package org.salespointframework.core.product;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.features.ProductFeature;
import org.salespointframework.core.product.features.ProductFeatureType;
import org.salespointframework.util.Objects;
import org.salespointframework.util.SalespointIterable;

@Entity
public class AbstractProductType implements ProductType {
	
	@Id
	@GeneratedValue
	private int productIdentifier;
	
	protected String name;
	protected Money price;
	
	//TODO Map?
	protected Set<ProductFeatureType> featureTypes = new HashSet<ProductFeatureType>() ;
	
	@Deprecated
	protected AbstractProductType() {
		
	}
	
	
	public AbstractProductType(String name, Money price) {
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
	
	public boolean equals(AbstractProductType productType) {
		return this.productIdentifier == productType.productIdentifier;
	}
	
	@Override
	public int hashCode() {
		return Integer.valueOf(productIdentifier).hashCode();
	}
	
	@Override	
	public Money getPrice() {
		return price;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public Iterable<ProductFeatureType> getProductFeatureTypes() {
		return SalespointIterable.from(featureTypes);
	}
	
	@Override
	public int getProductIdentifier() {
		return productIdentifier;
	}
	
}
