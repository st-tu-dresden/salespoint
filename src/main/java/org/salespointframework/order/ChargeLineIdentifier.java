package org.salespointframework.order;

import javax.persistence.Embeddable;

import org.salespointframework.core.SalespointIdentifier;

/**
 * {@link ChargeLineIdentifier} serves as an identifier type for {@link ChargeLine} objects. The main reason for its
 * existence is type safety for identifier across the Salespoint Framework.
 * 
 * @author Paul Henke
 * @author Oliver Gierke
 */
@Embeddable
class ChargeLineIdentifier extends SalespointIdentifier {

	private static final long serialVersionUID = 3953538683490057901L;

	/**
	 * Creates a new unique identifier for {@link ChargeLine}s.
	 */
	ChargeLineIdentifier() {
		super();
	}

	/**
	 * Only needed for property editor, shouldn't be used otherwise.
	 * 
	 * @param chargeLineIdentifier The string representation of the identifier.
	 */

	ChargeLineIdentifier(String chargeLineIdentifier) {
		super(chargeLineIdentifier);
	}
}
