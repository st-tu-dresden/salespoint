package org.salespointframework.core.order;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.Product;
import org.salespointframework.core.product.ProductIdentifier;
import org.salespointframework.core.quantity.Quantity;

/**
 * 
 * @author Thomas Dedek
 * @author Paul Henke
 * 
 */
public interface OrderLine
{
	/**
	 * 
	 * @return the {@link OrderLineIdentifier} to uniquely identify this orderline
	 */
	OrderLineIdentifier getIdentifier();
	
	/**
	 * 
	 * @return the {@link ProductIdentifier} of this orderline 
	 */
	ProductIdentifier getProductIdentifier();

	/**
	 * 
	 * @return the quantity of ordered {@link Product}s
	 */
	Quantity getQuantity();
	/**
	 * 
	 * @return the value of the orderline
	 */
	Money getPrice();
	
	/**
	 * 
	 * @return the name of the product in this orderline
	 */
	String getProductName();
}
