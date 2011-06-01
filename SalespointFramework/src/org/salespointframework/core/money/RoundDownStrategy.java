package org.salespointframework.core.money;

import java.io.Serializable;
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
	public Quantity round(Quantity quantity) {
		int precision = 1;
		
		while(numberOfDigits-- > 0)
			precision *= 10;
		
		quantity.setAmount((float) Math.floor(quantity.getAmount() * precision)/precision);
		
		return quantity;
	}
   
}
