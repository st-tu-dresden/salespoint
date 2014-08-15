package org.salespointframework.order;

import javax.persistence.Embeddable;

import org.salespointframework.core.SalespointIdentifier;

/**
 * <code>OrderIdentifier</code> serves as an identifier type for
 * {@link Order} objects. The main reason for its existence is
 * type safety for identifier across the Salespoint Framework. <br />
 * <code>OrderIdentifier</code> instances serve as primary key
 * attribute in {@link Order}, but can also be used as
 * a key for non-persistent, <code>Map</code>-based implementations.
 * 
 * @author Thomas Dedek
 *
 */
@SuppressWarnings("serial")
@Embeddable
public final class OrderIdentifier extends SalespointIdentifier
{
	/**
	 * Creates a new unique identifier for {@link Order}.
	 */
	public OrderIdentifier()
	{
		super();
	}

	
	/**
	 * Only needed for property editor, shouldn't be used otherwise.
	 * 
	 * @param orderIdentifier
	 *            The string representation of the identifier.
	 */
	@Deprecated
	public OrderIdentifier(String orderIdentifier)
	{
		super(orderIdentifier);
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	@Override
	public boolean equals(Object other) { 
		return super.equals(other);
	}
}
