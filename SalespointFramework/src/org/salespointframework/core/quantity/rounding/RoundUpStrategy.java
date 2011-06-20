package org.salespointframework.core.quantity.rounding;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: RoundUpStrategy
 *
 */
@Entity
/**Round a value up, i.e. away from zero.
 * 
 */
public class RoundUpStrategy extends AbstractRoundingStrategy implements Serializable, RoundingStrategy {
	@Id @GeneratedValue(strategy=GenerationType.AUTO) long id;
	
	private static final long serialVersionUID = 1L;
	
	@Deprecated
	protected RoundUpStrategy() {};
	
	public RoundUpStrategy(int numberOfDigits) {
		super(numberOfDigits, 0, 0);
	}

	@Override
	public BigDecimal round(BigDecimal amount) {
		return amount.setScale(numberOfDigits, RoundingMode.UP);
	}
   
}
