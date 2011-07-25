package org.salespointframework.core.product;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.*;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.features.ProductFeature;
import org.salespointframework.util.Objects;
import org.salespointframework.util.SalespointIterable;

@MappedSuperclass
public abstract class AbstractProductInstance implements ProductInstance {
	
	@EmbeddedId
	private SerialNumber serialNumber;
	
	@Embedded
	@AttributeOverride(name="id", column=@Column(name="PRODUCT_ID"))
	private ProductIdentifier productIdentifier;
	
	@OneToOne(cascade = CascadeType.PERSIST)
	private Money price;
	
	//TODO annot
	private Map<String, ProductFeature> productFeatures = new HashMap<String, ProductFeature>();
	
	@Deprecated
	protected AbstractProductInstance() { }
	
	public AbstractProductInstance(ProductType productType) {
		this.productIdentifier = Objects.requireNonNull(productType, "productType").getProductIdentifier();
		this.price = productType.getPrice();
		this.serialNumber = new SerialNumber();
		
		calculatePrice();
	}

	// Hook Method
	protected void calculatePrice() {
		for(ProductFeature pt : productFeatures.values()) {
			price.add(pt.getPrice());
		}
	}
	
	@Override
	public ProductIdentifier getProductIdentifier() { 
		return productIdentifier;
	}

	@Override
	public Money getPrice() {
		return price;
	}

	@Override
	public SerialNumber getSerialNumber() {
		return serialNumber;
	}

	@Override
	public Iterable<ProductFeature> getProductFeatures() {
		return SalespointIterable.from(productFeatures.values());
	}
	
	@Override
	public ProductFeature getProductFeature(String name) {
		return productFeatures.get(name);
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == null) return false;
		if(other == this) return true;
		if(!(other instanceof ProductInstance)) return false;
		return this.equals((ProductInstance)other);
	}
	
	public boolean equals(ProductInstance other) {
		if(other == null) return false;
		if(other == this) return true;
		return this.productIdentifier.equals(other.getSerialNumber());
	}
	
	@Override
	public int hashCode() {
		return productIdentifier.hashCode();
	}

}
