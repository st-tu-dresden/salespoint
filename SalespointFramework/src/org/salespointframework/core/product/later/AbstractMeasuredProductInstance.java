package org.salespointframework.core.product.later;

import java.math.BigDecimal;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.quantity.Quantity;
import org.salespointframework.util.Objects;


public abstract class AbstractMeasuredProductInstance<T extends MeasuredProductType> implements MeasuredProductInstance<T> {

	private T productType;
	private Quantity quantity;
	
	@Deprecated
	protected AbstractMeasuredProductInstance(){}
	
	/**
     * Parameterized constructor with 
     * @param productType The productType of this MeasuredProductInstance
     * @param quantity The quantity of this MeasuredProductInstance
     */
	public AbstractMeasuredProductInstance(T productType, Quantity quantity) {
		this.productType = Objects.requireNonNull(productType, "productType");
		this.quantity = Objects.requireNonNull(quantity, "quantity");
		this.productType.reduceQuantity(this.quantity);
	}
	
	/**
     * Parameterized constructor with 
     * @param productType The productType of this MeasuredProductInstance
     * @param amount The amount of the quantity, which will be used for this MeasuredProductInstance as Integer Value.
     */
	public AbstractMeasuredProductInstance(T productType, int amount) {
		this.productType = Objects.requireNonNull(productType, "productType");
		this.quantity = new Quantity(amount, productType.getQuantityOnHand().getMetric(), productType.getQuantityOnHand().getRoundingStrategy());
		this.productType.reduceQuantity(quantity);
	}
	
	/**
     * Parameterized constructor with 
     * @param productType The productType of this MeasuredProductInstance
     * @param amount The amount of the quantity, which will be used for this MeasuredProductInstance as BigDecimal Value.
     */
	public AbstractMeasuredProductInstance(T productType, BigDecimal amount) {
		this.productType = Objects.requireNonNull(productType, "productType");
		this.quantity = new Quantity(amount, productType.getQuantityOnHand().getMetric(), productType.getQuantityOnHand().getRoundingStrategy());
		this.productType.reduceQuantity(quantity);
	}
	
	/**
     * Parameterized constructor with 
     * @param productType The productType of this MeasuredProductInstance
     * @param amount The amount of the quantity, which will be used for this MeasuredProductInstance as Long Value.
     */
	public AbstractMeasuredProductInstance(T productType, long amount) {
		this.productType = Objects.requireNonNull(productType, "productType");
		this.quantity = new Quantity(amount, productType.getQuantityOnHand().getMetric(), productType.getQuantityOnHand().getRoundingStrategy());
		this.productType.reduceQuantity(quantity);
	}
	
	/**
     * Parameterized constructor with 
     * @param productType The productType of this MeasuredProductInstance
     * @param amount The amount of the quantity, which will be used for this MeasuredProductInstance as Float Value.
     */
	public AbstractMeasuredProductInstance(T productType, float amount) {
		this.productType = Objects.requireNonNull(productType, "productType");
		this.quantity = new Quantity(amount, productType.getQuantityOnHand().getMetric(), productType.getQuantityOnHand().getRoundingStrategy());
		this.productType.reduceQuantity(quantity);
	}
	
	/**
     * Parameterized constructor with 
     * @param productType The productType of this MeasuredProductInstance
     * @param amount The amount of the quantity, which will be used for this MeasuredProductInstance as Double Value.
     */
	public AbstractMeasuredProductInstance(T productType, double amount) {
		this.productType = Objects.requireNonNull(productType, "productType");
		this.quantity = new Quantity(amount, productType.getQuantityOnHand().getMetric(), productType.getQuantityOnHand().getRoundingStrategy());
		this.productType.reduceQuantity(quantity);
	}
	
	public T getProductType(){
		return this.productType;
	}
	
	public Quantity getQuantity(){
		return quantity;
	}
	
	@Override
	public Money getPrice(){
		return (Money) productType.getUnitPrice().multiply(quantity);
	}
}
