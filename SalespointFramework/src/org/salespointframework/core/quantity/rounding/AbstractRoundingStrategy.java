package org.salespointframework.core.quantity.rounding;

import java.math.BigDecimal;

public abstract class AbstractRoundingStrategy implements RoundingStrategy {

	protected int numberOfDigits;
	protected int roundingDigit;
	protected int roundingStep;

	@Deprecated
	protected AbstractRoundingStrategy() {};
	
	public AbstractRoundingStrategy(int numberOfDigits, int roundingDigit, int roundingStep) {
		this.numberOfDigits = numberOfDigits;
		this.roundingDigit = roundingDigit;
		this.roundingStep = roundingStep;
	}
	
	@Override
	abstract public BigDecimal round(BigDecimal amount);
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof AbstractRoundingStrategy))
			return false;
		else {
			AbstractRoundingStrategy s = (AbstractRoundingStrategy)o;
			return numberOfDigits == s.numberOfDigits &&
						roundingDigit == s.roundingDigit &&
						roundingStep == s.roundingStep &&
						this.getClass().equals(o.getClass());
		}
	}
}
