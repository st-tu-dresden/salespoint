package org.salespointframework.core.product.later;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.ProductInstance;
import org.salespointframework.core.quantity.Quantity;

public interface MeasuredProductInstance<T extends MeasuredProductType> extends ProductInstance {

	/**
     * Return the MeasuredProductType of this MeasuredProductInstance
     * @return the {@link MeasuredProductType} of this MeasuredProductInstance 
     */
	public T getProductType();
	/**
     * Return the Quantity
     * @return the quantity of this MeasuredProductInstance 
     */
	public Quantity getQuantity();
	/**
     * Return the Price of this MeasuredProductInstance
     * @return the price of this MeasuredProductInstance 
     */
	public Money getPrice();
}
