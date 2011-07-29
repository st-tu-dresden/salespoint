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
public class Unit extends Quantity {
	public static final Unit ZERO = new Unit(0);
	public static final Unit ONE = new Unit(1);
	public static final Unit TEN = new Unit(10);
	/**
	 * 
	 */
	private static final long serialVersionUID = 8974672849577920667L;

	/**
	 * Creates a new <code>Unit</code> instance, representing
	 * <code>amount</code> units of something.
	 * 
	 * @param amount
	 */
	public Unit(int amount) {
		super(amount, Metric.PIECES, RoundingStrategy.ROUND_ONE);
	}

}
