package org.salespointframework.core.quantity.rounding;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: RoundDownStrategy
 *
 */
@Entity
/** Rounds a number towards zero.
 * Negative numbers get less negative and positive numbers get less positive.
 * This effectively truncates the number at the specified numberOfDigits.
 */
public class RoundDownStrategy extends AbstractRoundingStrategy implements Serializable, RoundingStrategy {
	@Id @GeneratedValue(strategy=GenerationType.AUTO) long id;
	private static final long serialVersionUID = 1L;
	
	@Deprecated
	protected RoundDownStrategy() {};
	
	public RoundDownStrategy(int numberOfDigits) {
		super(numberOfDigits, 0, 0);
	}
	
	@Override
	public BigDecimal round(BigDecimal amount) {
		return amount.setScale(numberOfDigits, BigDecimal.ROUND_DOWN);
	}
   
}
