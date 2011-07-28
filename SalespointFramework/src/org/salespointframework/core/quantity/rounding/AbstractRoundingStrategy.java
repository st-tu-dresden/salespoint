package org.salespointframework.core.quantity.rounding;

import java.io.Serializable;
import java.math.BigDecimal;

public abstract class AbstractRoundingStrategy implements RoundingStrategy,
		Serializable {
	private static final long serialVersionUID = 7276214459340424642L;

	protected int numberOfDigits;
	protected int roundingDigit;
	protected int roundingStep;

	public AbstractRoundingStrategy(int numberOfDigits, int roundingDigit,
			int roundingStep) {
		this.numberOfDigits = numberOfDigits;
		this.roundingDigit = roundingDigit;
		this.roundingStep = roundingStep;
	}

	@Override
	abstract public BigDecimal round(BigDecimal amount);

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof AbstractRoundingStrategy))
			return false;
		else {
			AbstractRoundingStrategy s = (AbstractRoundingStrategy) o;
			return numberOfDigits == s.numberOfDigits
					&& roundingDigit == s.roundingDigit
					&& roundingStep == s.roundingStep
					&& this.getClass().equals(o.getClass());
		}
	}

	/**
	 * Returns the number of digits after the decimal delimiter which are kept,
	 * when using this <code>RoundingStrategy</code>.
	 * 
	 * @return Number of digits after the decimal delimiter.
	 */
	public int getNumberOfDigits() {
		return numberOfDigits;
	}

	/**
	 * Returns the digit on which is rounded on. If a
	 * <code>RoundingStrategy</code> does not use this option, a value of
	 * <code>0</code> is returned.
	 * 
	 * @return Digit, below which a number is rounded down an above which a
	 *         number is rounded up.
	 */
	public int getRoundingDigit() {
		return roundingDigit;
	}

}
