package org.salespointframework.core.money;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

import org.salespointframework.core.money.AbstractRoundingStrategy;

/**
 * Entity implementation class for Entity: RoundDownStrategy
 *
 */
@Entity
public class RoundDownStrategy extends AbstractRoundingStrategy implements Serializable {
	@Id @GeneratedValue(strategy=GenerationType.AUTO) long id;
	private static final long serialVersionUID = 1L;

	public RoundDownStrategy(int numberOfDigits, int roundingDigit, int roundingStep) {
		super(numberOfDigits, roundingDigit, roundingStep);
	}

	@Override
	public BigDecimal round(BigDecimal amount) {
		return amount.setScale(numberOfDigits, BigDecimal.ROUND_DOWN);
	}
   
}
