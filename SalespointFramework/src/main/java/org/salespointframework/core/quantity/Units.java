package org.salespointframework.core.quantity;

import org.salespointframework.core.quantity.rounding.RoundingStrategy;

/**
 * The class <code>Unit</code> represents everything which can be counted. It
 * has a default <code>Metric</code> of "Unit" and default
 * <code>RoundDownStrategy</code> which rounds to the nearest integer value.
 * 
 * @author hannesweisbach
 * 
 */
@SuppressWarnings("serial")
public class Units extends Quantity {
	
	/**
	 * Convenience instance, which can be used for countable objects. The name
	 * is "Units", the symbol is "units" and the definition is empty.
	 */
	public static final Metric METRIC = new Metric("Units", "units", "");

	
	/**
	 * Convenience instance of 0 units.
	 */
	public static final Units ZERO = new Units(0);
	/**
	 * Convenience instance of 1 unit.
	 */
	public static final Units ONE = new Units(1);
	/**
	 * Convenience instance of 10 units.
	 */
	public static final Units TEN = new Units(10);

	/**
	 * Creates a new <code>Unit</code> instance, representing
	 * {@code amount} units of something.
	 * 
	 * @param amount
	 *            number of objects, this <code>Unit</code> object represents
	 */
	private Units(long amount) {
		super(amount, METRIC , RoundingStrategy.ROUND_ONE);
	}
	
	public static Units of(long amount) {
		return new Units(amount);
	}
}
