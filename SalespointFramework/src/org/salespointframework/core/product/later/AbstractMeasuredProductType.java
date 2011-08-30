package org.salespointframework.core.product.later;

import java.math.BigDecimal;

import javax.persistence.Entity;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.AbstractProductType;
import org.salespointframework.core.quantity.Metric;
import org.salespointframework.core.quantity.Quantity;
import org.salespointframework.util.Objects;

/**
 * 
 * This is an abstract representation of a MeasuredProductType which provides basic
 * functionality
 * 
 */

@Entity

public abstract class AbstractMeasuredProductType extends AbstractProductType implements MeasuredProductType{
	
	private Quantity quantityOnHand;
	private Metric preferredMetric;
	private Money unitPrice;

	@Deprecated
	protected AbstractMeasuredProductType(){ }
	
	/**
     * Parameterized constructor with
     * @param productIdentifier The id of this MeasuredProductType 
     * @param name The name of this MeasuredProductType
     * @param price The price of the quantity of this MeasuredProductType
     * @param quantityOnHand The quantity of this MeasuredProductType, which is available.
     */
	public AbstractMeasuredProductType(String name, Money price, Quantity quantityOnHand){
		super( name, price);
		Objects.requireNonNull(name, "name");
		this.quantityOnHand = Objects.requireNonNull(quantityOnHand, "quantityOnHand");
		this.preferredMetric = quantityOnHand.getMetric();
		this.unitPrice = price.divide_(new Money(quantityOnHand.getAmount()));
	}
	
	public Quantity getQuantityOnHand(){
		return this.quantityOnHand;
	}
	
	public Metric getPreferredMetric(){
		return this.preferredMetric;
	}
	
	@Override
	public Money getPrice(){
		return quantityOnHand.multiply_(unitPrice);
	}
	
	public Money getUnitPrice(){
		return this.unitPrice;
	}
	
	public void addQuantity(Quantity quantity){
	
		 if (quantity.getAmount().intValue()<0)
	          throw new IllegalArgumentException("The amount of the quantity cannot be a negative value.");
	
		quantityOnHand = quantityOnHand.add(quantity);
	}
	
	/**
	 * Add the quantity of this amount to the available quantity of the MeasuredProductType
	 * @param amount of the quantity which add to the {@link AbstractMeasuredProductType#quantityOnHand} of this MeasuredProductType
	 * @throws IllegalArgumentException
	 * The {@link IllegalArgumentException} will be thrown, if the amount 
	 * of the quantity is negative. 
	 */
	
	public void addQuantity(int amount){
	
		 if (amount<0)
	          throw new IllegalArgumentException("The amount of the quantity cannot be a negative value.");
	
		Quantity q = new Quantity(amount, quantityOnHand.getMetric(), quantityOnHand.getRoundingStrategy());
		quantityOnHand = quantityOnHand.add(q);
	}
	
	/**
	 * Add the quantity of this amount to the available quantity of the MeasuredProductType
	 * @param amount of the quantity which add to the {@link AbstractMeasuredProductType#quantityOnHand} of this MeasuredProductType
	 * @throws IllegalArgumentException
	 * The {@link IllegalArgumentException} will be thrown, if the amount 
	 * of the quantity is negative. 
	 */
	
	public void addQuantity(double amount){
	
		 if (amount<0)
	           throw new IllegalArgumentException("The amount of the quantity cannot be a negative value.");
	
		Quantity q = new Quantity(amount, quantityOnHand.getMetric(), quantityOnHand.getRoundingStrategy());
		quantityOnHand = quantityOnHand.add(q);
	}
	
	/**
	 * Add the quantity of this amount to the available quantity of the MeasuredProductType
	 * @param amount of the quantity which add to the {@link AbstractMeasuredProductType#quantityOnHand} of this MeasuredProductType
	 * @throws IllegalArgumentException
	 * The {@link IllegalArgumentException} will be thrown, if the amount 
	 * of the quantity is negative. 
	 */
	
	public void addQuantity(float amount){
	
		 if (amount<0)
	           throw new IllegalArgumentException("The amount of the quantity cannot be a negative value.");
	
		Quantity q = new Quantity(amount, quantityOnHand.getMetric(), quantityOnHand.getRoundingStrategy());
		quantityOnHand = quantityOnHand.add(q);
	}
	
	/**
	 * Add the quantity of this amount to the available quantity of the MeasuredProductType
	 * @param amount of the quantity which add to the {@link AbstractMeasuredProductType#quantityOnHand} of this MeasuredProductType
	 * @throws IllegalArgumentException
	 * The {@link IllegalArgumentException} will be thrown, if the amount 
	 * of the quantity is negative. 
	 */
	
	public void addQuantity(long amount){
	
		 if (amount<0)
	           throw new IllegalArgumentException("The amount of the quantity cannot be a negative value.");
	
		Quantity q = new Quantity(amount, quantityOnHand.getMetric(), quantityOnHand.getRoundingStrategy());
		quantityOnHand = quantityOnHand.add(q);
	}
	
	/**
	 * Add the quantity of this amount to the available quantity of the MeasuredProductType
	 * @param amount of the quantity which add to the {@link AbstractMeasuredProductType#quantityOnHand} of this MeasuredProductType
	 * @throws IllegalArgumentException
	 * The {@link IllegalArgumentException} will be thrown, if the amount 
	 * of the quantity is negative. 
	 */
	
	public void addQuantity(BigDecimal amount){
	
		 if (amount.intValue()<0)
	           throw new IllegalArgumentException("The amount of the quantity cannot be a negative value.");
	
		Quantity q = new Quantity(amount, quantityOnHand.getMetric(), quantityOnHand.getRoundingStrategy());
		quantityOnHand = quantityOnHand.add(q);
	}
	
	public void reduceQuantityOnHand(Quantity quantity){
		
		 if (quantity.getAmount().intValue()<0)
	          throw new IllegalArgumentException("The amount of the quantity cannot be a negative value.");
		 
		 if (quantityOnHand.compareTo(quantity)<0)
	       throw new IllegalArgumentException("There isn't enough quantity of this product available. There are only "+ quantityOnHand.getAmount().intValue() + quantityOnHand.getMetric().getSymbol() + ".");
		
		quantityOnHand = quantityOnHand.subtract(quantity);
	}
	
	/**
     * Subtract this amount from the available quantity of the MeasuredProductType  
     * @param amount of the quantity which reduces the {@link AbstractMeasuredProductType#quantityOnHand} of the MeasuredProductType
     * @throws IllegalArgumentException
     * The {@link IllegalArgumentException} will be thrown, if the amount 
     * of the quantity is negative and if the quantity, which will be subtract, is greater than
     * the quantityOnHand of this MeasuredProductType. 
     */
	
	public void reduceQuantityOnHand(int amount){
		
		 if (amount<0)
	         throw new IllegalArgumentException("The amount of the quantity cannot be a negative value.");
		 
		 if (quantityOnHand.getAmount().intValue()<amount)
	       throw new IllegalArgumentException("There isn't enough quantity of this product available. There are only "+ quantityOnHand.getAmount().intValue() + quantityOnHand.getMetric().getSymbol() + ".");
		
		quantityOnHand = quantityOnHand.subtract(new Quantity(amount, quantityOnHand.getMetric(), quantityOnHand.getRoundingStrategy()));
		
	}
	
	/**
     * Subtract this amount from the available quantity of the MeasuredProductType  
     * @param amount of the quantity which reduces the {@link AbstractMeasuredProductType#quantityOnHand} of the MeasuredProductType
     * @throws IllegalArgumentException
     * The {@link IllegalArgumentException} will be thrown, if the amount 
     * of the quantity is negative and if the quantity, which will be subtract, is greater than
     * the quantityOnHand of this MeasuredProductType. 
     */
	
	public void reduceQuantityOnHand(double amount){
		
		 if (amount<0)
	        throw new IllegalArgumentException("The amount of the quantity cannot be a negative value.");
		 
		 if (quantityOnHand.getAmount().intValue()<amount)
	      throw new IllegalArgumentException("There isn't enough quantity of this product available. There are only "+ quantityOnHand.getAmount().intValue() + quantityOnHand.getMetric().getSymbol() + ".");
		
		quantityOnHand = quantityOnHand.subtract(new Quantity(amount, quantityOnHand.getMetric(), quantityOnHand.getRoundingStrategy()));
		
	}
	
	/**
     * Subtract this amount from the available quantity of the MeasuredProductType  
     * @param amount of the quantity which reduces the {@link AbstractMeasuredProductType#quantityOnHand} of the MeasuredProductType
     * @throws IllegalArgumentException
     * The {@link IllegalArgumentException} will be thrown, if the amount 
     * of the quantity is negative and if the quantity, which will be subtract, is greater than
     * the quantityOnHand of this MeasuredProductType. 
     */
	
	public void reduceQuantityOnHand(float amount){
		
		 if (amount<0)
	        throw new IllegalArgumentException("The amount of the quantity cannot be a negative value.");
		 
		 if (quantityOnHand.getAmount().intValue()<amount)
	      throw new IllegalArgumentException("There isn't enough quantity of this product available. There are only "+ quantityOnHand.getAmount().intValue() + quantityOnHand.getMetric().getSymbol() + ".");
		
		quantityOnHand = quantityOnHand.subtract(new Quantity(amount, quantityOnHand.getMetric(), quantityOnHand.getRoundingStrategy()));
		
	}
	
	/**
     * Subtract this amount from the available quantity of the MeasuredProductType  
     * @param amount of the quantity which reduces the {@link AbstractMeasuredProductType#quantityOnHand} of the MeasuredProductType
     * @throws IllegalArgumentException
     * The {@link IllegalArgumentException} will be thrown, if the amount 
     * of the quantity is negative and if the quantity, which will be subtract, is greater than
     * the quantityOnHand of this MeasuredProductType. 
     */
	
	public void reduceQuantityOnHand(long amount){
		
		 if (amount<0)
	        throw new IllegalArgumentException("The amount of the quantity cannot be a negative value.");
		 
		 if (quantityOnHand.getAmount().intValue()<amount)
	      throw new IllegalArgumentException("There isn't enough quantity of this product available. There are only "+ quantityOnHand.getAmount().intValue() + quantityOnHand.getMetric().getSymbol() + ".");
		
		quantityOnHand = quantityOnHand.subtract(new Quantity(amount, quantityOnHand.getMetric(), quantityOnHand.getRoundingStrategy()));
		
	}
	
	/**
     * Subtract this amount from the available quantity of the MeasuredProductType  
     * @param amount of the quantity which reduces the {@link AbstractMeasuredProductType#quantityOnHand} of the MeasuredProductType
     * @throws IllegalArgumentException
     * The {@link IllegalArgumentException} will be thrown, if the amount 
     * of the quantity is negative and if the quantity, which will be subtract, is greater than
     * the quantityOnHand of this MeasuredProductType. 
     */
	
	public void reduceQuantityOnHand(BigDecimal amount){
		
		 if (amount.intValue()<0)
	        throw new IllegalArgumentException("The amount of the quantity cannot be a negative value.");
		 
		 if (quantityOnHand.getAmount().compareTo(amount)<0)
	      throw new IllegalArgumentException("There isn't enough quantity of this product available. There are only "+ quantityOnHand.getAmount().intValue() + quantityOnHand.getMetric().getSymbol() + ".");
		
		quantityOnHand = quantityOnHand.subtract(new Quantity(amount, quantityOnHand.getMetric(), quantityOnHand.getRoundingStrategy()));
		
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == null) return false;
		if(other == this) return true;
		if(!(other instanceof MeasuredProductType)) return false;
		return this.equals((MeasuredProductType)other);
	}
	
	/**
	 * Determines if the given {@link MeasuredProductType} is equal to this one or
	 * not. Two MeasuredProductTypes are equal to each other, if their hash code is
	 * the same.
	 * 
	 * @param other
	 *            this one should be compared with
	 * @return <code>true</code> if and only if the hashCode of this Object
	 *         equals the hashCode of the object given as parameter.
	 *         <code>false</code> otherwise.
	 */
	
	public final boolean equals(MeasuredProductType other) {
		if(other == null) return false;
		if(other == this) return true;
		return this.getProductIdentifier().equals(other.getProductIdentifier());
	}
}
