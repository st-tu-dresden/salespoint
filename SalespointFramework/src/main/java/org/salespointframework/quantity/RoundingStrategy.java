package org.salespointframework.quantity;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * This is interface is implemented by classes, which are used to round numbers.
 * Specifically, {@link Quantity} uses classes implementing
 * <code>RoundingStrategy</code> to facilitate arithmetic operations.
 * 
 * @author Hannes Weisbach
 * 
 */
public interface RoundingStrategy {
	/**
	 * Convenience instance, which can be used for monetary rounding. Rounds
	 * towards zero with 4 digits after the decimal delimiter.
	 */
	public static final RoundingStrategy MONETARY_ROUNDING = new BasicRoundingStrategy(
			4, RoundingMode.HALF_UP);
	/**
	 * Convenience instance, which can be used for rounding of integral types.
	 * Rounds towards zero, with 0 digits after the decimal delimiter.
	 */
	public static final RoundingStrategy ROUND_ONE = new BasicRoundingStrategy(0, RoundingMode.HALF_DOWN);

	/**
	 * Rounds the given {@link BigDecimal} instance {@code amount}
	 * with a specific strategy. Because {@link BigDecimal} is immutable, a
	 * new instance with a rounded value is returned.
	 * 
	 * @param amount
	 *            The {@link BigDecimal} instance which should be rounded.
	 * @return A new {@link BigDecimal} instance which has a rounded value.
	 */
	public BigDecimal round(BigDecimal amount);
}
