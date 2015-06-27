package org.salespointframework.order;

import org.javamoney.moneta.Money;

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
	Money getPrice();
}
