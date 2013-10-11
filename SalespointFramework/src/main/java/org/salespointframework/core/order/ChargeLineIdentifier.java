package org.salespointframework.core.order;

import javax.persistence.Embeddable;

import org.salespointframework.core.SalespointIdentifier;

/**
 * <code>ChargeLineIdentifier</code> serves as an identifier type for
 * {@link ChargeLine} objects. The main reason for its existence is
 * type safety for identifier across the Salespoint Framework.
 * <code>ChargeLineIdentifier</code> 
 * 
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
