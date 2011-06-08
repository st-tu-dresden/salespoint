package org.salespointframework.core.money;

import java.math.BigDecimal;

public abstract class AbstractRoundingStrategy implements RoundingStrategy {

	protected int numberOfDigits;
	protected int roundingDigit;
	protected int roundingStep;

	public AbstractRoundingStrategy(int numberOfDigits, int roundingDigit, int roundingStep) {
		this.numberOfDigits = numberOfDigits;
		this.roundingDigit = roundingDigit;
		this.roundingStep = roundingStep;
	}
	
	@Override
	abstract public BigDecimal round(BigDecimal amount);
}
