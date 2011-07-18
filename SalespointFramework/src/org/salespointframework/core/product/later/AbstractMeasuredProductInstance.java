package org.salespointframework.core.product.later;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.quantity.Quantity;
import org.salespointframework.util.Objects;


public abstract class AbstractMeasuredProductInstance<T extends MeasuredProductType> implements MeasuredProductInstance<T> {

	private T productType;
	private Quantity quantity;
	
	@Deprecated
	protected AbstractMeasuredProductInstance(){}
	
	public AbstractMeasuredProductInstance(T productType, Quantity quantity) {
		this.productType = Objects.requireNonNull(productType, "productType");
		this.quantity = Objects.requireNonNull(quantity, "quantity");
	}
	/*
	public AbstractMeasuredProductInstance(T productType, int amount) {
		this.productType = Objects.requireNonNull(productType, "productType");
		this.quantity = new Quantity(amount, productType.getQuantityOnHand().getMetric(), productType.getQuantityOnHand().getRoundingStrategy());
	}
	*/
	public T getProductType(){
		return this.productType;
	}
	
	public Quantity getQuantity(){
		return quantity;
	}
	
	public Money getPrice(){
		return (Money) quantity.multiply(productType.getUnitPrice());
	}
}
