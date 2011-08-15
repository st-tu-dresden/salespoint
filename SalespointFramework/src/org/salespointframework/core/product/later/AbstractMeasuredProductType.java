package org.salespointframework.core.product.later;

import java.math.BigDecimal;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.AbstractProductType;
import org.salespointframework.core.quantity.Metric;
import org.salespointframework.core.quantity.Quantity;
import org.salespointframework.util.Objects;

public abstract class AbstractMeasuredProductType extends AbstractProductType implements MeasuredProductType{
	
	private Quantity quantityOnHand;
	private Metric preferredMetric;
	private Money unitPrice;

	@Deprecated
public AbstractMeasuredProductType(){
		
	}
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

/**
 * Returns an {@link Quantity} of this MeasuredProductType, which is available.
 * @return the quantityOnHand of the MeasuredProductType
 */

public Quantity getQuantityOnHand(){
	return this.quantityOnHand;
}

/**
 * Returns the {@link Metric} of this MeasuredProductType, which is preffered.
 * @return the Metric of the MeasuredProductType
 */

public Metric getPreferredMetric(){
	return this.preferredMetric;
}

/**
 * Returns the Price of the Quantity of this MeasuredProductType.
 * @return the Price of the Quantity on Hand of this MeasuredProductType
 */

@Override
public Money getPrice(){
	return quantityOnHand.multiply_(unitPrice);
}

/**
 * Returns the Price of an unit of this MeasuredProductType.
 * @return the Unit Price of the MeasuredProductType
 */

public Money getUnitPrice(){
	
	return this.unitPrice;
}

/**
 * Adds a quantity to the quantity of the MeasuredProductType
 * @param quantity which will be added to the quantityOnHand of this MeasuredProductType.
 * @throws IllegalArgumentException
 * The {@link IllegalArgumentException} will be thrown, if the amount 
 * of the quantity is negative. 
 */

public void addQuantity(Quantity quantity){

	 if (quantity.getAmount().intValue()<0)
          throw new IllegalArgumentException("The amount of the quantity cannot be a negative value.");

	quantityOnHand = quantityOnHand.add(quantity);
}

public void addQuantity(int amount){

	 if (amount<0)
          throw new IllegalArgumentException("The amount of the quantity cannot be a negative value.");

	Quantity q = new Quantity(amount, quantityOnHand.getMetric(), quantityOnHand.getRoundingStrategy());
	quantityOnHand = quantityOnHand.add(q);
}

public void addQuantity(double amount){

	 if (amount<0)
           throw new IllegalArgumentException("The amount of the quantity cannot be a negative value.");

	Quantity q = new Quantity(amount, quantityOnHand.getMetric(), quantityOnHand.getRoundingStrategy());
	quantityOnHand = quantityOnHand.add(q);
}


public void addQuantity(float amount){

	 if (amount<0)
           throw new IllegalArgumentException("The amount of the quantity cannot be a negative value.");

	Quantity q = new Quantity(amount, quantityOnHand.getMetric(), quantityOnHand.getRoundingStrategy());
	quantityOnHand = quantityOnHand.add(q);
}


public void addQuantity(long amount){

	 if (amount<0)
           throw new IllegalArgumentException("The amount of the quantity cannot be a negative value.");

	Quantity q = new Quantity(amount, quantityOnHand.getMetric(), quantityOnHand.getRoundingStrategy());
	quantityOnHand = quantityOnHand.add(q);
}


public void addQuantity(BigDecimal amount){

	 if (amount.intValue()<0)
           throw new IllegalArgumentException("The amount of the quantity cannot be a negative value.");

	Quantity q = new Quantity(amount, quantityOnHand.getMetric(), quantityOnHand.getRoundingStrategy());
	quantityOnHand = quantityOnHand.add(q);
}

/**
 * Subtracts a quantity to the quantity of the MeasuredProductType
 * @param quantity which will be subtract
 * @throws IllegalArgumentException
 * The {@link IllegalArgumentException} will be thrown, if the amount 
 * of the quantity is negative and if the quantity, which will be subtract, is greater than
 * the quantityOnHand of this MeasuredProductType. 
 */
public void reduceQuantityOnHand(Quantity quantity){
	
	 if (quantity.getAmount().intValue()<0)
          throw new IllegalArgumentException("The amount of the quantity cannot be a negative value.");
	 
	 if (quantityOnHand.compareTo(quantity)<0)
       throw new IllegalArgumentException("There isn't enough quantity of this product available. There are only "+ quantityOnHand.getAmount().intValue() + quantityOnHand.getMetric().getSymbol() + ".");
	
	quantityOnHand = quantityOnHand.subtract(quantity);
}

public void reduceQuantityOnHand(int amount){
	
	 if (amount<0)
         throw new IllegalArgumentException("The amount of the quantity cannot be a negative value.");
	 
	 if (quantityOnHand.getAmount().intValue()<amount)
       throw new IllegalArgumentException("There isn't enough quantity of this product available. There are only "+ quantityOnHand.getAmount().intValue() + quantityOnHand.getMetric().getSymbol() + ".");
	
	quantityOnHand = quantityOnHand.subtract(new Quantity(amount, quantityOnHand.getMetric(), quantityOnHand.getRoundingStrategy()));
	
}

public void reduceQuantityOnHand(double amount){
	
	 if (amount<0)
        throw new IllegalArgumentException("The amount of the quantity cannot be a negative value.");
	 
	 if (quantityOnHand.getAmount().intValue()<amount)
      throw new IllegalArgumentException("There isn't enough quantity of this product available. There are only "+ quantityOnHand.getAmount().intValue() + quantityOnHand.getMetric().getSymbol() + ".");
	
	quantityOnHand = quantityOnHand.subtract(new Quantity(amount, quantityOnHand.getMetric(), quantityOnHand.getRoundingStrategy()));
	
}

public void reduceQuantityOnHand(float amount){
	
	 if (amount<0)
        throw new IllegalArgumentException("The amount of the quantity cannot be a negative value.");
	 
	 if (quantityOnHand.getAmount().intValue()<amount)
      throw new IllegalArgumentException("There isn't enough quantity of this product available. There are only "+ quantityOnHand.getAmount().intValue() + quantityOnHand.getMetric().getSymbol() + ".");
	
	quantityOnHand = quantityOnHand.subtract(new Quantity(amount, quantityOnHand.getMetric(), quantityOnHand.getRoundingStrategy()));
	
}

public void reduceQuantityOnHand(long amount){
	
	 if (amount<0)
        throw new IllegalArgumentException("The amount of the quantity cannot be a negative value.");
	 
	 if (quantityOnHand.getAmount().intValue()<amount)
      throw new IllegalArgumentException("There isn't enough quantity of this product available. There are only "+ quantityOnHand.getAmount().intValue() + quantityOnHand.getMetric().getSymbol() + ".");
	
	quantityOnHand = quantityOnHand.subtract(new Quantity(amount, quantityOnHand.getMetric(), quantityOnHand.getRoundingStrategy()));
	
}

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

public boolean equals(MeasuredProductType other) {
	if(other == null) return false;
	if(other == this) return true;
	return productIdentifier.equals(other.getProductIdentifier());
}

@Override
public int hashCode() {
	return this.getProductIdentifier().hashCode();
}
}
