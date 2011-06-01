package org.salespointframework.core.money;

import java.io.Serializable;

import javax.persistence.*;
import org.salespointframework.core.money.AbstractRoundingStrategy;

/**
 * Entity implementation class for Entity: RoundStrategy
 *
 */
@Entity

public class RoundStrategy extends AbstractRoundingStrategy implements Serializable {
	@Id @GeneratedValue(strategy=GenerationType.AUTO) long id;	
	private static final long serialVersionUID = 1L;

	public RoundStrategy(int numberOfDigits, int roundingDigit, int roundingStep) {
		super(numberOfDigits, roundingDigit, roundingStep);
	}

	@Override
	public Quantity round(Quantity quantity) {
		// TODO Auto-generated method stub
		return null;
	}
   
}
