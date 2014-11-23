package org.salespointframework.order;

import org.joda.money.Money;

/**
 * Interface for any priced item to ease summing up priced items.
 *
 * @author Oliver Gierke
 */
interface Priced {

	/**
	 * Returns the proce of the item.
	 * 
	 * @return
	 */
	Money getPrice();
}
