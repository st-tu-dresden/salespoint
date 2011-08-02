package org.salespointframework.core.product.later;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.ProductType;
import org.salespointframework.core.quantity.Metric;
import org.salespointframework.core.quantity.Quantity;

public interface MeasuredProductType extends ProductType {

	/**
     * Return the quantity of the MeasuredProductType, which is available
     * @return the quantity of the MeasuredProductType
     */
	public Quantity getQuantityOnHand();
	
	/**
     * Return the metric of the MeasuredProductType, which is preferred 
     * @return the metric of the MeasuredProductType
     */
	public Metric getPreferredMetric();
	
	/**
     * Return the price for a unit of the MeasuredProductType 
     * @return the price for a unit of the MeasuredProductType
     */
	public Money getUnitPrice();
	
	/**
     * Subtract this quantity from the available quantity of the MeasuredProductType  
     * @param quantity which reduces the {@link AbstractMeasuredProductType#quantityOnHand} of the MeasuredProductType
     */
	public void reduceQuantity(Quantity quantity);
}
