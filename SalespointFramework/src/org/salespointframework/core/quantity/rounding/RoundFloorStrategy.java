package org.salespointframework.core.quantity.rounding;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Rounds towards negative infinity.
 * 
 */
public class RoundFloorStrategy extends AbstractRoundingStrategy implements Serializable, RoundingStrategy {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new <code>RoundingStrategy</code> which rounds towards negative
	 * infinity, keeping <code>numberOfDigits</code> digits after the decimal
	 * delimiter.
	 * 
	 * @param numberOfDigits
	 *            Number of digits which should be kept, when using this
	 *            <code>RoundingStrategy</code>.
	 */
	public RoundFloorStrategy(int numberOfDigits) {
		super(numberOfDigits, 0, BigDecimal.ZERO);
	}

	@Override
	public BigDecimal round(BigDecimal amount) {
		return amount.setScale(numberOfDigits, RoundingMode.FLOOR);
	}
}
