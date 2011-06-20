package org.salespointframework.core.product;

import javax.persistence.*;

import org.salespointframework.core.money.Money;
import org.salespointframework.util.Objects;

// TODO equals und hashCode überschreiben

//@Entity
public abstract class AbstractProductInstance<T extends AbstractProductType> implements ProductInstance<T> {
	
	@Id
	@GeneratedValue
	private int serialNumber;
	
	// TODO richtig?
	//@ManyToOne
	private T productType;
	
	@Deprecated
	protected AbstractProductInstance() {
		
	}
	
	public AbstractProductInstance(T productType) {
		this.productType = Objects.requireNonNull(productType, "productType");
	}

	@Override
	public T getProductType() { 
		return productType;
	}

	@Override
	public Money getPrice() {
		return productType.getPrice();
	}

	@Override
	public String getName() {
		return productType.getName();
	}

	@Override
	public int getSerialNumber() {
		return serialNumber;
	}
}
