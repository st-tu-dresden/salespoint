package org.salespointframework.order;

import javax.money.MonetaryAmount;

import org.salespointframework.core.Currencies;
import org.salespointframework.core.Streamable;
import org.springframework.util.Assert;

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

	/**
	 * Sums up the prices of all given {@link Priced} instances.
	 * 
	 * @param priced must not be {@literal null}.
	 * @return
	 */
	static MonetaryAmount sumUp(Iterable<? extends Priced> priced) {

		Assert.notNull(priced, "Iterable must not be null!");

		return Streamable.of(priced).stream().//
				map(Priced::getPrice).//
				reduce((left, right) -> left.add(right)).orElse(Currencies.ZERO_EURO);
	}
}
