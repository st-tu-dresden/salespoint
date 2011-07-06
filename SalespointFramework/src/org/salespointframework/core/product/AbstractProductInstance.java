package org.salespointframework.core.product;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.*;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.features.ProductFeature;
import org.salespointframework.util.Objects;
import org.salespointframework.util.SalespointIterable;

// TODO equals und hashCode überschreiben

@MappedSuperclass
public abstract class AbstractProductInstance<T extends AbstractProductType> implements ProductInstance<T> {
	
	@Id
	@GeneratedValue
	private int serialNumber;
	
	// TODO richtig?
	//@ManyToOne
	private T productType;
	
	private Money price;
	
	private Map<String, ProductFeature> productFeatures = new HashMap<String, ProductFeature>();
	
	@Deprecated
	protected AbstractProductInstance() { }
	
	public AbstractProductInstance(T productType) {
		this.productType = Objects.requireNonNull(productType, "productType");
		
		calculatePrice();
	}

	
	private void calculatePrice() {
		price = productType.getPrice();
		for(ProductFeature pt : productFeatures.values()) {
			price.add(pt.getPrice());
		}
	}
	
	@Override
	public T getProductType() { 
		return productType;
	}

	@Override
	public Money getPrice() {
		return price;
	}

	@Override
	public int getSerialNumber() {
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

}
