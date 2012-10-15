package org.salespointframework.core.inventory;

import org.salespointframework.core.product.Product;
import org.salespointframework.core.quantity.Quantity;

/**
 * 
 * @author Paul Henke
 * 
 */
public interface InventoryItem
{
	InventoryItemIdentifier getIdentifier();
	
	Quantity getQuantity();

	Product getProduct();

	void increaseQuantiy(Quantity quantity);

	void decreaseQuantity(Quantity quantity);

}
