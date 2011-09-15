package org.salespointframework.core.product;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.salespointframework.core.money.Money;
import org.salespointframework.util.Iterables;
import org.salespointframework.util.Objects;
import org.salespointframework.util.Tuple;


@Entity
public class PersistentProduct implements Product {
	
	@EmbeddedId
	private SerialNumber serialNumber;
	
	@Embedded
	@AttributeOverride(name="id", column=@Column(name="PRODUCT_ID"))
	private ProductIdentifier productIdentifier;
	
	protected String name; 
	private Money price;
	
	@ElementCollection
	private Set<ProductFeature> productFeatures = new HashSet<ProductFeature>();
	
    /**
     * Parameterless constructor required for JPA. Do not use.
     */
	@Deprecated
	protected PersistentProduct() { }
	

	public PersistentProduct(final ProductType productType)  {
		this(productType, Iterables.<Tuple<String, String>>empty());
	}
	
	public PersistentProduct(final ProductType productType, final Iterable<Tuple<String,String>> features) {
		Objects.requireNonNull(features, "features");
		this.productIdentifier = Objects.requireNonNull(productType, "productType").getProductIdentifier();
		this.name = productType.getName();
		this.price = productType.getPrice(); //TODO CLONE?
		this.serialNumber = new SerialNumber();
		
		// TODO zu hacky?
		for(Tuple<String,String> feature : features) {
			ProductFeature f = ProductFeature.create(feature.getItem1(), feature.getItem2());
			
			for(ProductFeature realFeature : productType.getProductFeatures()) {
				if(f.equals(realFeature)) {
					if(!productFeatures.contains(realFeature)) {
						productFeatures.add(realFeature);
					} else {
						//TODO Exception ?
					}
				}
			}
		}
	}
	
	@Override
	public final ProductIdentifier getProductIdentifier() { 
		return productIdentifier;
	}

	@Override
	public Money getPrice() {
		return price;
	}

	@Override
	public final SerialNumber getSerialNumber() {
		return serialNumber;
	}

	@Override
	public final Iterable<ProductFeature> getProductFeatures() {
		return Iterables.from(productFeatures);
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == null) return false;
		if(other == this) return true;
		if(!(other instanceof PersistentProduct)) return false;
		return this.equals((PersistentProduct)other);
	}
	
	public final boolean equals(PersistentProduct other) {
		if(other == null) return false;
		if(other == this) return true;
		return this.serialNumber.equals(other.serialNumber);
	}
	
	@Override
	public final int hashCode() {
		return serialNumber.hashCode();
	}
	
	@Override
	public String toString() {
		return name;
	}
}
