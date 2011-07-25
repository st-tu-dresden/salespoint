package org.salespointframework.core.quantity.rounding;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: RoundStrategy
 *
 */
//@Entity

public class RoundStrategy extends AbstractRoundingStrategy implements Serializable, RoundingStrategy {
	//@Id @GeneratedValue(strategy=GenerationType.AUTO) long id;	
	private static final long serialVersionUID = 1L;
	/*
	@Deprecated
	protected RoundStrategy() {};
	*/
	public RoundStrategy(int numberOfDigits, int roundingDigit) {
		super(numberOfDigits, roundingDigit, 0);
	}
	
	@Override
	public BigDecimal round(BigDecimal amount) {
		BigInteger lastDigit = amount.scaleByPowerOfTen(numberOfDigits + 1).toBigInteger().mod(BigInteger.TEN);
		//if the last digit is equal or greater than the rounding digit ...
		if(lastDigit.compareTo(BigInteger.valueOf(roundingDigit)) >= 0) {
			// ... we need to add or subtract (depending on sign of amount) 1
			if(amount.compareTo(BigDecimal.ZERO) > 0) {
				return new BigDecimal(amount.scaleByPowerOfTen(numberOfDigits).add(BigDecimal.ONE).toBigInteger()).scaleByPowerOfTen(-numberOfDigits);
			} else {
				return new BigDecimal(amount.scaleByPowerOfTen(numberOfDigits).subtract(BigDecimal.ONE).toBigInteger()).scaleByPowerOfTen(-numberOfDigits);	
			}
		} else {
			//if the last digit is smaller than the rounding digit, we can just truncate	
			return new BigDecimal(amount.scaleByPowerOfTen(numberOfDigits).toBigInteger()).scaleByPowerOfTen(-numberOfDigits);	
		}
	
	}
   
}
