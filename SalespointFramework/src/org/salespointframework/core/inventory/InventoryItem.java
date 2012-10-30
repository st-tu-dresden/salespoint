package org.salespointframework.core.inventory;

import org.salespointframework.core.product.Product;
import org.salespointframework.core.quantity.MetricMismatchException;
import org.salespointframework.core.quantity.Quantity;

/**
 * Represents a {@link Product} and its {@link Quantity}
 * Is used by {@link Inventory}
 * 
 * @author Paul Henke
 * 
 */
public interface InventoryItem
{
	/**
	 * 
	 * @return the unique identifier of this InventoryItem
	 */
	InventoryItemIdentifier getIdentifier();
	
	/**
	 * 
	 * @return the quantity of this InventoryItem
	 */
	Quantity getQuantity();

	/**
	 * 	
	 * @return the Product of this InventoryItem
	 */
	Product getProduct();

	
	/**
	 * Increases the quantity of this InventoryItem
	 * @param quantity 
	 * @throws MetricMismatchException if this.product.getQuantity().getMetric() != quantity.getMetric()
	 */
	void increaseQuantiy(Quantity quantity);

	/**
	 * Decreases the quantity of this InventoryItem
	 * @param quantity 
	 * @throws MetricMismatchException if this.product.getQuantity().getMetric() != quantity.getMetric()
	 */
	void decreaseQuantity(Quantity quantity);

}
