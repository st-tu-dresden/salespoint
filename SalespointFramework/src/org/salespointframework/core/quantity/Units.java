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
	
	public static final Metric METRIC = Metric.PIECES;
	
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
	 * <code>amount</code> units of something.
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
