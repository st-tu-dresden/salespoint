package org.salespointframework.core.quantity.rounding;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Round a value up, i.e. away from zero.
 * 
 */
@SuppressWarnings("serial")
public class RoundUpStrategy extends AbstractRoundingStrategy {

	private int numberOfDigits;

	/**
	 * Creates a new <code>RoundingsStrategy</code> which rounds away from zero,
	 * keeping <code>numberOfDigits</code> digits after the decimal delimiter.
	 * 
	 * @param numberOfDigits
	 *            Number of digits after the decimal delimiter, which should be
	 *            kept.
	 */
	public RoundUpStrategy(int numberOfDigits) {
		super(numberOfDigits, 0, BigDecimal.ZERO);
	}

	@Override
	public BigDecimal round(BigDecimal amount) {
		return amount.setScale(numberOfDigits, RoundingMode.UP);
	}
}
