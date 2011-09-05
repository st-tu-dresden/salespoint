package org.salespointframework.core.quantity.rounding;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Rounds towards positive infinity.
 * 
 */
public class RoundCeilStrategy extends AbstractRoundingStrategy implements Serializable, RoundingStrategy {

	private static final long serialVersionUID = 1L;

	/**
	 * Create a new <code>RoundingStrategy</code> which rounds towards positive
	 * infinity. <code>numberOfDigits</code> digits after the decimal delimiter
	 * are kept.
	 * 
	 * @param numberOfDigits
	 *            Number of digits after the decimal delimiter which are kept.
	 */
	public RoundCeilStrategy(int numberOfDigits) {
		super(numberOfDigits, 0, BigDecimal.ZERO);
	}

	@Override
	public BigDecimal round(BigDecimal amount) {
		return amount.setScale(numberOfDigits, RoundingMode.CEILING);
	}

}
