package org.salespointframework.core.product.later;

import java.math.BigDecimal;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.ProductType;
import org.salespointframework.core.quantity.Metric;
import org.salespointframework.core.quantity.Quantity;

/**
 * This is an interface which provides basic methods to handle MeasuredProductTypes.
 * 
 */

// TODO, die vielen add und reduce methoden sind ziemlich redundant 
public interface MeasuredProductType extends ProductType {

	/**
     * Returns the quantity of the MeasuredProductType, which is available
     * @return the quantity of the MeasuredProductType
     */
	public Quantity getQuantityOnHand();
	
	/**
     * Returns the metric of the MeasuredProductType, which is preferred 
     * @return the metric of the MeasuredProductType
     */
	public Metric getPreferredMetric();
	
	/**
     * Returns the price for a unit of the MeasuredProductType 
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
	 * @throws IllegalArgumentException
	 * The {@link IllegalArgumentException} will be thrown, if the amount 
	 * of the quantity is negative. 
	 */
	public void addQuantity(double amount);
	
	/**
	 * Add the quantity of this amount to the available quantity of the MeasuredProductType
	 * @param amount of the quantity which add to the {@link AbstractMeasuredProductType#quantityOnHand} of this MeasuredProductType
	 * @throws IllegalArgumentException
	 * The {@link IllegalArgumentException} will be thrown, if the amount 
	 * of the quantity is negative. 
	 */
	public void addQuantity(int amount);
	
	/**
	 * Add the quantity of this amount to the available quantity of the MeasuredProductType
	 * @param amount of the quantity which add to the {@link AbstractMeasuredProductType#quantityOnHand} of this MeasuredProductType
	 * @throws IllegalArgumentException
	 * The {@link IllegalArgumentException} will be thrown, if the amount 
	 * of the quantity is negative. 
	 */
	public void addQuantity(long amount);
	
	/**
	 * Add the quantity of this amount to the available quantity of the MeasuredProductType
	 * @param amount of the quantity which add to the {@link AbstractMeasuredProductType#quantityOnHand} of this MeasuredProductType
	 * @throws IllegalArgumentException
	 * The {@link IllegalArgumentException} will be thrown, if the amount 
	 * of the quantity is negative. 
	 */
	public void addQuantity(float amount);
	
	/**
	 * Add the quantity of this amount to the available quantity of the MeasuredProductType
	 * @param amount of the quantity which add to the {@link AbstractMeasuredProductType#quantityOnHand} of this MeasuredProductType
	 * @throws IllegalArgumentException
	 * The {@link IllegalArgumentException} will be thrown, if the amount 
	 * of the quantity is negative. 
	 */
	public void addQuantity(BigDecimal amount);
	
	/**
     * Subtract this quantity from the available quantity of the MeasuredProductType  
     * @param quantity which reduces the {@link AbstractMeasuredProductType#quantityOnHand} of the MeasuredProductType
     * @throws IllegalArgumentException
	 * The {@link IllegalArgumentException} will be thrown, if the amount 
	 * of the quantity is negative. 
     */
	public void reduceQuantityOnHand(Quantity quantity);
	
	/**
     * Subtract this amount from the available quantity of the MeasuredProductType  
     * @param amount of the quantity which reduces the {@link AbstractMeasuredProductType#quantityOnHand} of the MeasuredProductType
     * @throws IllegalArgumentException
     * The {@link IllegalArgumentException} will be thrown, if the amount 
     * of the quantity is negative and if the quantity, which will be subtract, is greater than
     * the quantityOnHand of this MeasuredProductType. 
     */
	public void reduceQuantityOnHand(double amount);
	
	/**
     * Subtract this amount from the available quantity of the MeasuredProductType  
     * @param amount of the quantity which reduces the {@link AbstractMeasuredProductType#quantityOnHand} of the MeasuredProductType
     * @throws IllegalArgumentException
     * The {@link IllegalArgumentException} will be thrown, if the amount 
     * of the quantity is negative and if the quantity, which will be subtract, is greater than
     * the quantityOnHand of this MeasuredProductType. 
     */
	
	public void reduceQuantityOnHand(int amount);
	
	/**
     * Subtract this amount from the available quantity of the MeasuredProductType  
     * @param amount of the quantity which reduces the {@link AbstractMeasuredProductType#quantityOnHand} of the MeasuredProductType
     * @throws IllegalArgumentException
     * The {@link IllegalArgumentException} will be thrown, if the amount 
     * of the quantity is negative and if the quantity, which will be subtract, is greater than
     * the quantityOnHand of this MeasuredProductType. 
     */
	
	public void reduceQuantityOnHand(long amount);
	
	/**
     * Subtract this amount from the available quantity of the MeasuredProductType  
     * @param amount of the quantity which reduces the {@link AbstractMeasuredProductType#quantityOnHand} of the MeasuredProductType
     * @throws IllegalArgumentException
     * The {@link IllegalArgumentException} will be thrown, if the amount 
     * of the quantity is negative and if the quantity, which will be subtract, is greater than
     * the quantityOnHand of this MeasuredProductType. 
     */
	
	public void reduceQuantityOnHand(float amount);
	
	/**
     * Subtract this amount from the available quantity of the MeasuredProductType  
     * @param amount of the quantity which reduces the {@link AbstractMeasuredProductType#quantityOnHand} of the MeasuredProductType
     * @throws IllegalArgumentException
     * The {@link IllegalArgumentException} will be thrown, if the amount 
     * of the quantity is negative and if the quantity, which will be subtract, is greater than
     * the quantityOnHand of this MeasuredProductType. 
     */
	
	public void reduceQuantityOnHand(BigDecimal amount);




}
