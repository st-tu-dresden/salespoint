package org.salespointframework.core.quantity.rounding;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

//@Entity
public/* abstract */class AbstractRoundingStrategy implements RoundingStrategy, Serializable {
/**
	 * 
	 */
	private static final long serialVersionUID = 7276214459340424642L;
	/*	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	long id;
*/
	protected int numberOfDigits;
	protected int roundingDigit;
	protected int roundingStep;

	/*
	@Deprecated
	protected AbstractRoundingStrategy() {
	};
*/
	
	public AbstractRoundingStrategy(int numberOfDigits, int roundingDigit,
			int roundingStep) {
		this.numberOfDigits = numberOfDigits;
		this.roundingDigit = roundingDigit;
		this.roundingStep = roundingStep;
	}

	@Override
	/* abstract */public BigDecimal round(BigDecimal amount) {
		throw new RuntimeException(
				"Do not use AbstractRoundingStrategy. Extend it and implement public BigDecimal round(BigDecimal amount)");
	};

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
}
