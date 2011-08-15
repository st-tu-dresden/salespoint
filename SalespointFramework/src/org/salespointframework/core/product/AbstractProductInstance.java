package org.salespointframework.core.product;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.*;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.features.ProductFeature;

import org.salespointframework.util.Iterables;
import org.salespointframework.util.Objects;

@MappedSuperclass
public abstract class AbstractProductInstance implements ProductInstance {
	
	@EmbeddedId
	private SerialNumber serialNumber;
	
	@Embedded
	@AttributeOverride(name="id", column=@Column(name="PRODUCT_ID"))
	private ProductIdentifier productIdentifier;
	
	private Money price;
	
	@ElementCollection
	private Map<String, ProductFeature> productFeatures = new HashMap<String, ProductFeature>();
	
	@Deprecated
	protected AbstractProductInstance() { }
	
	public AbstractProductInstance(final ProductType productType) {
		this.productIdentifier = Objects.requireNonNull(productType, "productType").getProductIdentifier();
		//this.productType = productType;
		this.price = productType.getPrice(); //TODO CLONE
		this.serialNumber = new SerialNumber();
		
		calculatePrice();
	}

	
	// TODO
	private final void addFeatures(final ProductType productType) {
		
	}
	
	// Hook Method
	protected void calculatePrice() {
		for(ProductFeature pt : productFeatures.values()) {
			price = price.add_(pt.getPrice());
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
		return Iterables.from(productFeatures.values());
	}
	
	@Override
	public final ProductFeature getProductFeature(final String name) {
		Objects.requireNonNull(name, "name");
		return productFeatures.get(name);
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == null) return false;
		if(other == this) return true;
		if(!(other instanceof ProductInstance)) return false;
		return this.equals((ProductInstance)other);
	}
	
	public final boolean equals(ProductInstance other) {
		if(other == null) return false;
		if(other == this) return true;
		return this.serialNumber.equals(other.getSerialNumber());
	}
	
	@Override
	public final int hashCode() {
		return serialNumber.hashCode();
	}
}
