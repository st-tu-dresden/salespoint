package org.salespointframework.core.quantity.rounding;

import java.io.Serializable;
import java.math.BigDecimal;
import org.salespointframework.util.Objects;

/**
 * @author Hannes Weisbach
 * 
 */
@SuppressWarnings({ "serial", "javadoc" })
public abstract class AbstractRoundingStrategy implements RoundingStrategy,
		Serializable {
	protected int numberOfDigits;
	protected int roundingDigit;
	protected BigDecimal roundingStep;

	/**
	 * Sole constructor for invocation by sub classes.
	 * 
	 * @param numberOfDigits
	 *            number of digits after the decimal delimiter to which will be
	 *            rounded
	 * @param roundingDigit
	 *            digit, in which the decision is based to round up or down
	 * @param roundingStep
	 *            nearest step to which will be rounded
	 */
	public AbstractRoundingStrategy(int numberOfDigits, int roundingDigit,
			BigDecimal roundingStep) {
		this.numberOfDigits = numberOfDigits;
		this.roundingDigit = roundingDigit;
		this.roundingStep = Objects.requireNonNull(roundingStep, "roundingStep");
	}

	@Override
	abstract public BigDecimal round(BigDecimal amount);

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o == this)
			return true;
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
	 * Returns a hash code for this <code>RoundingStrategy</code> object. The
	 * hash code is the exclusive OR of the <code>numberOfDigits</code>,
	 * <code>roundingDigit</code> and the hash code of <code>roundingStep</code>
	 * .
	 * 
	 * @return hash code for this <code>RoundingStrategy</code> object.
	 */
	public final int hashcode() {
		return numberOfDigits ^ roundingDigit ^ roundingStep.hashCode();
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

	/**
	 * Returns the step, to which is rounded. After rounding, the result is
	 * divisible by <code>roundingStep</code> with an integral result. If no
	 * rounding step is used, <code>BigDecimal.ZERO</code> is returned.
	 * 
	 * @return step to which is rounded, or <code>BigDecimal.ZERO</code>
	 */
	public BigDecimal getRoundingStep() {
		return roundingStep;
	}

}
