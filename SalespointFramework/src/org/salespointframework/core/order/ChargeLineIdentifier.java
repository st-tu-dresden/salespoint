package org.salespointframework.core.order;

import javax.persistence.Embeddable;

import org.salespointframework.util.SalespointIdentifier;

/**
 * 
 * @author Paul Henke
 *
 */
@SuppressWarnings("serial")
@Embeddable
public class ChargeLineIdentifier extends SalespointIdentifier {
	public ChargeLineIdentifier() {
		super();
	}
	
	public ChargeLineIdentifier(String chargeLineIdentifier) {
		super(chargeLineIdentifier);
	}
}
