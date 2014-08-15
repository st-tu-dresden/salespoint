package org.salespointframework.quantity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * This implementation of a {@link RoundingStrategy} offers to ways to describe
 * a rounding operation: using the decimal places after (or before) the decimal
 * delimiter and using a rounding step.<br/>
 * The two methods are related:
 * <code>roundingStep = 10<sup>(-numberOfDigits)</sup></code> <br/>
 * For example, specifying 4 as {@code numberOfDigits} results in a
 * {@code roundingStep} of 0.0001. To retain 4 digits after the decimal
 * delimiter, the value is rounded to the next multiple of 0.0001.
 * <p>
 * Which multiple is chosen - the higher or lower valued one - is determined by
 * the {@link RoundingMode}.
 * </p>
 * 
 * <p>
 * For either parameter - {@code roundingStep} and
 * {@code numberOfDigits} - positive and negative values are allowed.
 * </p>
 * <p>
 * Positive values of {@code numberOfDigits} designate decimal places after
 * the radix delimiter, negative values designate decimal places before the
 * radix delimiter. A value of -1, for example, will result in rounding to whole
 * 10s, -2 to whole 100s, and so forth. A value of zero designates integer
 * values.
 * </p>
 * 
 * <p>
 * Using negative values of {@code roundingStep} will yield the same result
 * as a positive {@code roundingStep} of the same magnitude would have.
 * Using zero as {@code roundingStep} is illegal and will result in an
 * {@link ArithmeticException} at runtime, when the
 * <code>BasicRoundingStrategy</code> is used.</p>
 * 
 * @author Hannes Weisbach
 * 
 */
@SuppressWarnings({ "serial" })
public class BasicRoundingStrategy implements RoundingStrategy, Serializable {

	private BigDecimal roundingStep;
	private RoundingMode roundingMode;

	/**
	 * Instantiates a new <code>BasicRoundingStrategy</code> using a rounding
	 * step and a rounding mode. Using both parameters a value is rounded in the
	 * direction specified by {@code roundingMode} to the next multiple of
	 * {@code roundingStep}. Usually, a rounding step is a positive value.
	 * However, a negative rounding step can also be specified, which gives the
	 * same results as a positive rounding step of equal value. A
	 * {@code roundingStep} of value zero may not be specified and will
	 * result in an <code>ArithmeticException</code>, if the
	 * <code>RoundingStrategy</code> is used.
	 * 
	 * @param roundingStep
	 *            multiple to which is rounded
	 * @param roundingMode
	 *            mode or direction in which is rounded
	 */
	public BasicRoundingStrategy(BigDecimal roundingStep,
			RoundingMode roundingMode) {
		this.roundingStep = Objects
				.requireNonNull(roundingStep, "roundingStep");
		this.roundingMode = Objects
				.requireNonNull(roundingMode, "roundingMode");
	}

	/**
	 * Instantiates a new <code>BasicRoundingStrategy</code> using the number of
	 * digits after (or before) the decimal delimiter and a rounding mode.
	 * {@code roundingMode} specifies the direction in which is rounded,
	 * whereas {@code numberOfDigits} specifies the precision to which is
	 * rounded. Positive values for {@code numberOfDigits} describe decimal
	 * places after the decimal delimiter, negative values before the decimal
	 * delimiter. A value of zero rounds to integer values.
	 * 
	 * @param numberOfDigits
	 *            rounding precision in decimal places before (negative) or
	 *            after (positive) the decimal delimiter
	 * @param roundingMode
	 *            mode or direction in which is rounded
	 */
	public BasicRoundingStrategy(int numberOfDigits, RoundingMode roundingMode) {
		this(BigDecimal.ONE.scaleByPowerOfTen(-numberOfDigits), roundingMode);
	}

	@Override
	public BigDecimal round(BigDecimal amount) {
		BigDecimal div;
		/* (int)(amount * (1/roundingStep)) * roundingStep */
		// System.out.print("Rounding " + amount + " with step " + roundingStep
		// + " " + roundingMode + " resulting in ");
		try {
			div = amount.divide(roundingStep);
		} catch (ArithmeticException e) {
			div = amount.divide(roundingStep, 10, roundingMode);
		}
		// System.out.println(div.setScale(0, roundingMode) + "(" + div + ") * "
		// + roundingStep + " = "
		// + div.setScale(0, roundingMode).multiply(roundingStep));
		return div.setScale(0, roundingMode).multiply(roundingStep);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Two instances of BasicRoundingStrategy are equal, if and only if their
	 * {@code roundingStep} attributes are numerically equal and if their
	 * {@code roundingMode} attributes are equal.
	 * </p>
	 */
	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o == this)
			return true;
		if (!(o instanceof BasicRoundingStrategy))
			return false;
		else {
			BasicRoundingStrategy s = (BasicRoundingStrategy) o;
			return this.roundingStep.compareTo(s.roundingStep) == 0
					&& this.roundingMode.equals(s.roundingMode);
		}
	}
	
	@Override 
	public int hashCode() {
		return this.roundingStep.hashCode() ^ this.roundingMode.hashCode();
	}

	/**
	 * Returns a hash code for this <code>RoundingStrategy</code> object. The
	 * hash code is the exclusive OR of the {@code roundingStep}, and the
	 * hash code of {@code roundingMode} objects.
	 * 
	 * @return hash code for this <code>RoundingStrategy</code> object.
	 */
	public final int hashcode() {
		return roundingStep.hashCode() ^ roundingMode.hashCode();
	}

	/**
	 * Returns the step, to which is rounded. After rounding, the result is
	 * divisible by {@code roundingStep} with an integral result.
	 * 
	 * @return step to which is rounded
	 */
	public BigDecimal getRoundingStep() {
		return roundingStep;
	}

	/**
	 * Returns the <code>RoundingMode</code> used by this instance of
	 * <code>BasicRoundingStrategy</code>.
	 * 
	 * @return <code>RoundingMode</code> used for rounding.
	 */
	public RoundingMode getRoundingMode() {
		return roundingMode;
	}

}
