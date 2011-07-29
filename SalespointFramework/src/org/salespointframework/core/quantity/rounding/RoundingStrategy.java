package org.salespointframework.core.quantity.rounding;

import java.math.BigDecimal;

public interface RoundingStrategy {
	public static final RoundingStrategy MONETARY_ROUNDING = new RoundDownStrategy(
			4);
	public static final RoundingStrategy ROUND_ONE = new RoundDownStrategy(0);

	/**
	 * Rounds the given <code>BigDecimal</code> instance <code>amount</code>
	 * with a specific strategy. Because <code>BigDecimal</code> is immutable, a
	 * new instance with a rounded value is returned.
	 * 
	 * @param amount
	 *            The <code>BigDecimal</code> instance which should be rounded.
	 * @return A new <code>BigDecimal</code> instance which has a rounded value.
	 */
	public BigDecimal round(BigDecimal amount);
}
