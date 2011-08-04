package org.salespointframework.core.product.later;

import java.math.BigDecimal;

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
	 * Add this quantity to the available quantity of the MeasuredProductType
	 * @param quantity which add to the {@link AbstractMeasuredProductType#quantityOnHand} of this MeasuredProductType
	 */
	public void addQuantity(Quantity quantity);
	
	/**
	 * Add the quantity of this amount to the available quantity of the MeasuredProductType
	 * @param amount of the quantity which add to the {@link AbstractMeasuredProductType#quantityOnHand} of this MeasuredProductType
	 */
	public void addQuantity(double amount);
	
	/**
	 * Add the quantity of this amount to the available quantity of the MeasuredProductType
	 * @param amount of the quantity which add to the {@link AbstractMeasuredProductType#quantityOnHand} of this MeasuredProductType
	 */
	public void addQuantity(int amount);
	
	/**
	 * Add the quantity of this amount to the available quantity of the MeasuredProductType
	 * @param amount of the quantity which add to the {@link AbstractMeasuredProductType#quantityOnHand} of this MeasuredProductType
	 */
	public void addQuantity(long amount);
	
	/**
	 * Add the quantity of this amount to the available quantity of the MeasuredProductType
	 * @param amount of the quantity which add to the {@link AbstractMeasuredProductType#quantityOnHand} of this MeasuredProductType
	 */
	public void addQuantity(float amount);
	
	/**
	 * Add the quantity of this amount to the available quantity of the MeasuredProductType
	 * @param amount of the quantity which add to the {@link AbstractMeasuredProductType#quantityOnHand} of this MeasuredProductType
	 */
	public void addQuantity(BigDecimal amount);
	
	/**
     * Subtract this quantity from the available quantity of the MeasuredProductType  
     * @param quantity which reduces the {@link AbstractMeasuredProductType#quantityOnHand} of the MeasuredProductType
     */
	public void reduceQuantityOnHand(Quantity quantity);
	
	/**
     * Subtract this amount from the available quantity of the MeasuredProductType  
     * @param amount of the quantity which reduces the {@link AbstractMeasuredProductType#quantityOnHand} of the MeasuredProductType
     */
	public void reduceQuantityOnHand(double amount);
	
	/**
     * Subtract this amount from the available quantity of the MeasuredProductType  
     * @param amount of the quantity which reduces the {@link AbstractMeasuredProductType#quantityOnHand} of the MeasuredProductType
     */
	public void reduceQuantityOnHand(int amount);
	
	/**
     * Subtract this amount from the available quantity of the MeasuredProductType  
     * @param amount of the quantity which reduces the {@link AbstractMeasuredProductType#quantityOnHand} of the MeasuredProductType
     */
	public void reduceQuantityOnHand(long amount);
	
	/**
     * Subtract this amount from the available quantity of the MeasuredProductType  
     * @param amount of the quantity which reduces the {@link AbstractMeasuredProductType#quantityOnHand} of the MeasuredProductType
     */
	public void reduceQuantityOnHand(float amount);
	
	/**
     * Subtract this amount from the available quantity of the MeasuredProductType  
     * @param amount of the quantity which reduces the {@link AbstractMeasuredProductType#quantityOnHand} of the MeasuredProductType
     */
	public void reduceQuantityOnHand(BigDecimal amount);




}
