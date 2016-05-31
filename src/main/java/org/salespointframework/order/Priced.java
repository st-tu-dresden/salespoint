package org.salespointframework.order;

import javax.money.MonetaryAmount;

/**
 * Interface for any priced item to ease summing up priced items.
 *
 * @author Oliver Gierke
 */
interface Priced {

	/**
	 * Returns the price of the item.
	 * 
	 * @return
	 */
	MonetaryAmount getPrice();
}
