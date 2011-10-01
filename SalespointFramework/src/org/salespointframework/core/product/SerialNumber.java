package org.salespointframework.core.product;

import javax.persistence.Embeddable;

import org.salespointframework.util.SalespointIdentifier;

/**
 * TODO
 * @author Paul Henke
 *
 */
@SuppressWarnings("serial")
@Embeddable
public final class SerialNumber extends SalespointIdentifier
{
	/**
	 * Creates a new unique identifier for {@link Product}s.
	 */
	public SerialNumber()
	{
		super();
	}

	/**
	 * Only needed for property editor, shouldn't be used otherwise.
	 * 
	 * @param serialNumber
	 *            The string representation of the identifier.
	 */
	@Deprecated
	public SerialNumber(String serialNumber)
	{
		super(serialNumber);
	}
}
