package org.salespointframework.core.quantity.rounding;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Rounds away from zero, to the nearest value, divisible by
 * <code>roundingStep</code>
 * 
 * @author hannesweisbach
 * 
 */
@SuppressWarnings("serial")
public class RoundUpStepStrategy extends AbstractRoundingStrategy {

	/**
	 * Creates a new <code>RoundingStrategy</code> which away from zero to the
	 * next value divisible by <code>roudingStep</code>.
	 * 
	 * @param roundingStep
	 *            step size to which should be rounded to.
	 * 
	 */
	public RoundUpStepStrategy(BigDecimal roundingStep) {
		super(0, 0, roundingStep);
	}

	@Override
	public BigDecimal round(BigDecimal amount) {
		/* 1/roundingStep */
		BigDecimal div = BigDecimal.ONE.divide(roundingStep);

		/* (int)(amount * (1/roundingStep)) * roundingStep */
		return amount.multiply(div).setScale(0, RoundingMode.UP).divide(div);
	}

}
