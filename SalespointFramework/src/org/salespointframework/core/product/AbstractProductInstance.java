package org.salespointframework.core.product;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.*;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.features.ProductFeature;
import org.salespointframework.core.product.features.ProductFeatureType;
import org.salespointframework.util.Objects;
import org.salespointframework.util.SalespointIterable;

@MappedSuperclass
public abstract class AbstractProductInstance implements ProductInstance {
	
	@EmbeddedId
	private SerialNumber serialNumber;
	
	@Embedded
	@AttributeOverride(name="id", column=@Column(name="PRODUCT_ID"))
	private ProductIdentifier productIdentifier;
	
	private Money price;
	//TODO annot?
	private Map<String, ProductFeature> productFeatures = new HashMap<String, ProductFeature>();
	
	@Deprecated
	protected AbstractProductInstance() { }
	
	public AbstractProductInstance(ProductType productType) {
		this.productIdentifier = Objects.requireNonNull(productType, "productType").getProductIdentifier();
		//this.productType = productType;
		this.price = productType.getPrice(); //TODO CLONE
		this.serialNumber = new SerialNumber();
		
		calculatePrice();
	}

	// Hook Method
	protected void calculatePrice() {
		for(ProductFeature pt : productFeatures.values()) {
			price = price.add_(pt.getPrice());
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
	public void addProductFeatures(ProductFeatureType pf){
		if (productFeatures!=null){
			productFeatures.clear();
		}
		//productType.addProductFeatureType(pf);
		
		for (ProductFeature p: pf.getPossibleValues()){
			productFeatures.put(p.getName(), p);
		}
		calculatePrice();
	}
	
	@Override
	public void removeProductFeatures(){
			productFeatures.clear();
			//price = productType.getPrice(); Warum auskommentiert?
		
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
		return this.serialNumber.equals(other.getSerialNumber());
	}
	
	@Override
	public int hashCode() {
		return serialNumber.hashCode();
	}

}
