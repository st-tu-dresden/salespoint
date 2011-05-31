package org.salespointframework.core.money;

public abstract class AbstractRoundingStrategy implements RoundingStrategy {

	private int numberOfDigits;
	private int roundingDigit;
	private int roundingStep;

	public AbstractRoundingStrategy(int numberOfDigits, int roundingDigit, int roundingStep) {
		this.numberOfDigits = numberOfDigits;
		this.roundingDigit = roundingDigit;
		this.roundingStep = roundingStep;
	}
	
	@Override
	abstract public Quantity round(Quantity quantity);
}
