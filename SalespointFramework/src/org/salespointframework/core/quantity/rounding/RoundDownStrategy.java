package org.salespointframework.core.quantity.rounding;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Rounds a number towards zero. Negative numbers get less negative and positive
 * numbers get less positive. This effectively truncates the number at the
 * specified numberOfDigits.
 */
public class RoundDownStrategy extends AbstractRoundingStrategy implements Serializable, RoundingStrategy {
	private static final long serialVersionUID = 1L;

	/**
	 * Create a new <code>RoundingStrategy</code> which rounds towards zero,
	 * keeping <code>numberOfDigits</code> digits after the decimal delimiter.
	 * 
	 * @param numberOfDigits
	 *            Number of digits, which should be kept after the decimal
	 *            delimiter.
	 */
	public RoundDownStrategy(int numberOfDigits) {
		super(numberOfDigits, 0, BigDecimal.ZERO);
	}

	@Override
	public BigDecimal round(BigDecimal amount) {
		return amount.setScale(numberOfDigits, BigDecimal.ROUND_DOWN);
	}
}
