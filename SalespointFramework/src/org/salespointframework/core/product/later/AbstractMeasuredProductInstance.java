package org.salespointframework.core.product.later;

import java.math.BigDecimal;

import javax.persistence.Entity;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.AbstractProductInstance;
import org.salespointframework.core.quantity.Quantity;
import org.salespointframework.util.Objects;

/**
 * 
 * This is an abstract representation of a MeasuredProductInstance which provides basic
 * functionality
 * 
 */


@Entity

public abstract class AbstractMeasuredProductInstance extends AbstractProductInstance implements MeasuredProductInstance {

	private Quantity quantity;
	private Money unitPrice;
	
	@Deprecated
	protected AbstractMeasuredProductInstance(){}
	
	/**
     * Parameterized constructor with 
     * @param productType The productType of this MeasuredProductInstance
     * @param quantity The quantity of this MeasuredProductInstance
     */
	public AbstractMeasuredProductInstance(MeasuredProductType productType, Quantity quantity) {
		super(productType);
		Objects.requireNonNull(productType, "productType");
		this.unitPrice = productType.getUnitPrice();
		this.quantity = Objects.requireNonNull(quantity, "quantity");
		productType.reduceQuantityOnHand(this.quantity);
	}
	
	/**
     * Parameterized constructor with 
     * @param productType The productType of this MeasuredProductInstance
     * @param amount The amount of the quantity, which will be used for this MeasuredProductInstance as Integer Value.
     */
	public AbstractMeasuredProductInstance(MeasuredProductType productType, int amount) {
		super(productType);
		Objects.requireNonNull(productType, "productType");
		this.unitPrice = productType.getUnitPrice();
		this.quantity = new Quantity(amount, productType.getQuantityOnHand().getMetric(), productType.getQuantityOnHand().getRoundingStrategy());
		productType.reduceQuantityOnHand(quantity);
	}
	
	/**
     * Parameterized constructor with 
     * @param productType The productType of this MeasuredProductInstance
     * @param amount The amount of the quantity, which will be used for this MeasuredProductInstance as BigDecimal Value.
     */
	public AbstractMeasuredProductInstance(MeasuredProductType productType, BigDecimal amount) {
		super(productType);
		Objects.requireNonNull(productType, "productType");
		this.unitPrice = productType.getUnitPrice();
		this.quantity = new Quantity(amount, productType.getQuantityOnHand().getMetric(), productType.getQuantityOnHand().getRoundingStrategy());
		productType.reduceQuantityOnHand(quantity);
	}
	
	/**
     * Parameterized constructor with 
     * @param productType The productType of this MeasuredProductInstance
     * @param amount The amount of the quantity, which will be used for this MeasuredProductInstance as Long Value.
     */
	public AbstractMeasuredProductInstance(MeasuredProductType productType, long amount) {
		super(productType);
		Objects.requireNonNull(productType, "productType");
		this.unitPrice = productType.getUnitPrice();
		this.quantity = new Quantity(amount, productType.getQuantityOnHand().getMetric(), productType.getQuantityOnHand().getRoundingStrategy());
		productType.reduceQuantityOnHand(quantity);
	}
	
	/**
     * Parameterized constructor with 
     * @param productType The productType of this MeasuredProductInstance
     * @param amount The amount of the quantity, which will be used for this MeasuredProductInstance as Float Value.
     */
	public AbstractMeasuredProductInstance(MeasuredProductType productType, float amount) {
		super(productType);
		Objects.requireNonNull(productType, "productType");
		this.unitPrice = productType.getUnitPrice();
		this.quantity = new Quantity(amount, productType.getQuantityOnHand().getMetric(), productType.getQuantityOnHand().getRoundingStrategy());
		productType.reduceQuantityOnHand(quantity);
	}
	
	/**
     * Parameterized constructor with 
     * @param productType The productType of this MeasuredProductInstance
     * @param amount The amount of the quantity, which will be used for this MeasuredProductInstance as Double Value.
     */
	public AbstractMeasuredProductInstance(MeasuredProductType productType, double amount) {
		super(productType);
		Objects.requireNonNull(productType, "productType");
		this.unitPrice = productType.getUnitPrice();
		this.quantity = new Quantity(amount, productType.getQuantityOnHand().getMetric(), productType.getQuantityOnHand().getRoundingStrategy());
		productType.reduceQuantityOnHand(quantity);
	}
	
	public Quantity getQuantity(){
		return quantity;
	}
	
	@Override
	public Money getPrice(){
		//return (Money) productType.getUnitPrice().multiply(quantity);
		return quantity.multiply_(this.unitPrice);
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == null) return false;
		if(other == this) return true;
		if(!(other instanceof MeasuredProductInstance)) return false;
		return this.equals((MeasuredProductInstance)other);
	}
	
	/**
	 * Determines if the given {@link MeasuredProductInstance} is equal to this one or
	 * not. Two MeasuredProductInstances are equal to each other, if their hash code is
	 * the same.
	 * 
	 * @param other
	 *            this one should be compared with
	 * @return <code>true</code> if and only if the hashCode of this Object
	 *         equals the hashCode of the object given as parameter.
	 *         <code>false</code> otherwise.
	 */
	
	public boolean equals(MeasuredProductInstance other) {
		if(other == null) return false;
		if(other == this) return true;
		return this.getSerialNumber().equals(other.getSerialNumber());
	}
	
}
