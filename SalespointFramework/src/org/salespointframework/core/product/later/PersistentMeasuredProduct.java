package org.salespointframework.core.product.later;

import java.math.BigDecimal;

import javax.persistence.Entity;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.PersistentProduct;
import org.salespointframework.core.quantity.Quantity;
import org.salespointframework.util.Objects;

/**
 * 
 * This is an abstract representation of a MeasuredProductInstance which provides basic
 * functionality
 * 
 */


@Entity

public class PersistentMeasuredProduct extends PersistentProduct implements MeasuredProduct {

	private Quantity quantity;
	private Money unitPrice;
	
	@Deprecated
	protected PersistentMeasuredProduct(){}
	
	/**
     * Parameterized constructor with 
     * @param productType The productType of this MeasuredProductInstance
     * @param quantity The quantity of this MeasuredProductInstance
     */
	public PersistentMeasuredProduct(MeasuredProductType productType, Quantity quantity) {
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
	public PersistentMeasuredProduct(MeasuredProductType productType, int amount) {
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
	public PersistentMeasuredProduct(MeasuredProductType productType, BigDecimal amount) {
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
	public PersistentMeasuredProduct(MeasuredProductType productType, long amount) {
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
	public PersistentMeasuredProduct(MeasuredProductType productType, float amount) {
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
	public PersistentMeasuredProduct(MeasuredProductType productType, double amount) {
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
		if(!(other instanceof MeasuredProduct)) return false;
		return this.equals((MeasuredProduct)other);
	}
	
	/**
	 * Determines if the given {@link MeasuredProduct} is equal to this one or
	 * not. Two MeasuredProductInstances are equal to each other, if their hash code is
	 * the same.
	 * 
	 * @param other
	 *            this one should be compared with
	 * @return <code>true</code> if and only if the hashCode of this Object
	 *         equals the hashCode of the object given as parameter.
	 *         <code>false</code> otherwise.
	 */
	
	public boolean equals(MeasuredProduct other) {
		if(other == null) return false;
		if(other == this) return true;
		return this.getSerialNumber().equals(other.getSerialNumber());
	}
	
}
