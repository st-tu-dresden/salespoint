package org.salespointframework.core.order;

import javax.persistence.Embeddable;

import org.salespointframework.util.SalespointIdentifier;

/**
 * TODO
 * @author Paul Henke
 * 
 */
@SuppressWarnings("serial")
@Embeddable
public class ChargeLineIdentifier extends SalespointIdentifier
{
	/**
	 * Creates a new unique identifier for {@link ChargeLine}s.
	 */
	public ChargeLineIdentifier()
	{
		super();
	}

	/**
	 * Only needed for property editor, shouldn't be used otherwise.
	 * 
	 * @param chargeLineIdentifier
	 *            The string representation of the identifier.
	 */
	@Deprecated
	public ChargeLineIdentifier(String chargeLineIdentifier)
	{
		super(chargeLineIdentifier);
	}
}
