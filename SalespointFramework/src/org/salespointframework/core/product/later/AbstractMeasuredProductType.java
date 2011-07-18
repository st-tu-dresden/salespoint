package org.salespointframework.core.product.later;

import java.util.HashSet;
import java.util.Set;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.AbstractProductType;
import org.salespointframework.core.quantity.Metric;
import org.salespointframework.core.quantity.Quantity;
import org.salespointframework.util.Objects;

public abstract class AbstractMeasuredProductType extends AbstractProductType {
	
	private Quantity quantityOnHand;
	private Metric preferredMetric;

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
public AbstractMeasuredProductType(String productIdentifier, String name, Money price, Quantity quantityOnHand){
	super(productIdentifier, name, price);
	this.quantityOnHand = Objects.requireNonNull(quantityOnHand, "quantityOnHand");
	this.preferredMetric = quantityOnHand.getMetric();
}

public Quantity getQuantityOnHand(){
	return this.quantityOnHand;
}

public Metric getPreferredMetric(){
	return this.preferredMetric;
}

public Money getUnitPrice(){
	Money m = new Money(quantityOnHand.getAmount());
	return (Money) this.getPrice().divide(m);
}

public void reduceQuantityOnHand(Quantity quantity){
	if (quantityOnHand.compareTo(quantity)>0)
        throw new IllegalArgumentException("There isn't enough quantity of this product available.");
	quantityOnHand = quantityOnHand.subtract(quantity);
}
}
