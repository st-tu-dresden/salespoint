package org.salespointframework.core.product;

import javax.persistence.Embeddable;

import org.salespointframework.util.SalespointIdentifier;

/**
 * <code>SerialNumber</code> serves as an identifier type for
 * {@link ProductInstance} objects. The main reason for its existence is
 * type safety for identifier across the Salespoint Framework. <br>
 * <code>SerialNumber</code> instances serve as primary key
 * attribute in {@link PersistentProductInstance}, but can also be used as
 * a key for non-persistent, <code>Map</code>-based implementations.
 * 
 * @author Paul Henke
 *
 */
@SuppressWarnings("serial")
@Embeddable
public final class SerialNumber extends SalespointIdentifier
{
	/**
	 * Creates a new unique identifier for {@link ProductInstance}s.
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
