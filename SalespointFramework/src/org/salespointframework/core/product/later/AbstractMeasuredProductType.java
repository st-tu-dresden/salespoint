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

public AbstractMeasuredProductType(String name, Money price, Quantity quantityOnHand){
	super(name, price);
	this.quantityOnHand = Objects.requireNonNull(quantityOnHand, "quantityOnHand");
	this.preferredMetric = quantityOnHand.getMetric();
}

public AbstractMeasuredProductType(String name, Money price, Quantity quantityOnHand, Set<Metric> possibleMetrics){
	super(name, price);
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
}
