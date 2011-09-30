package org.salespointframework.core.quantity.rounding;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * <code>RoundingStrategy</code> which rounds away from zero, using the supplied
 * <code>roundingDigit</code> for rounding instead of 5.
 * 
 * @author Hannes Weisbach
 * 
 */
@SuppressWarnings("serial")
public class RoundStrategy extends AbstractRoundingStrategy {

	private int numberOfDigits;
	private int roundingDigit;

	/**
	 * Creates a new <code>RoundingStrategy</code> keeping
	 * <code>numberOfDigits</code> digits after the decimal delimiter. Also,
	 * <code>roundingDigit</code> specifies the digit on which is rounded on,
	 * instead of 5.
	 * 
	 * @param numberOfDigits
	 *            Number of digits after the decimal delimiter, which are kept.
	 * @param roundingDigit
	 *            Digit, above which the number is rounded up and below which
	 *            the number is rounded down.
	 */
	public RoundStrategy(int numberOfDigits, int roundingDigit) {
		super(numberOfDigits, roundingDigit, BigDecimal.ZERO);
	}

	@Override
	public BigDecimal round(BigDecimal amount) {
		BigInteger lastDigit = amount.scaleByPowerOfTen(numberOfDigits + 1)
				.toBigInteger().mod(BigInteger.TEN);
		// if the last digit is equal or greater than the rounding digit ...
		if (lastDigit.compareTo(BigInteger.valueOf(roundingDigit)) >= 0) {
			// ... we need to add or subtract (depending on sign of amount) 1
			if (amount.compareTo(BigDecimal.ZERO) > 0) {
				return new BigDecimal(amount.scaleByPowerOfTen(numberOfDigits)
						.add(BigDecimal.ONE).toBigInteger())
						.scaleByPowerOfTen(-numberOfDigits);
			} else {
				return new BigDecimal(amount.scaleByPowerOfTen(numberOfDigits)
						.subtract(BigDecimal.ONE).toBigInteger())
						.scaleByPowerOfTen(-numberOfDigits);
			}
		} else {
			// if the last digit is smaller than the rounding digit, we can just
			// truncate
			return new BigDecimal(amount.scaleByPowerOfTen(numberOfDigits)
					.toBigInteger()).scaleByPowerOfTen(-numberOfDigits);
		}

	}

}
