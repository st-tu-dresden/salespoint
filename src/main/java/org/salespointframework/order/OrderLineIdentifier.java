package org.salespointframework.order;

import javax.persistence.Embeddable;

import org.salespointframework.core.SalespointIdentifier;

/**
 * <code>OrderLineIdentifier</code> serves as an identifier type for
 * {@link OrderLine} objects. The main reason for its existence is
 * type safety for identifier across the Salespoint Framework. <br />
 * <code>OrderLineIdentifier</code> instances serve as primary key
 * attribute in {@link OrderLine}, but can also be used as
 * a key for non-persistent, <code>Map</code>-based implementations.
 * 
 * @author Thomas Dedek
 *
 */
@SuppressWarnings("serial")
@Embeddable
public final class OrderLineIdentifier extends SalespointIdentifier
{
	/**
	 * Creates a new unique identifier for {@link OrderLine}s
	 */
	public OrderLineIdentifier()
	{
		super();
	}

	/**
	 * Only needed for property editor, shouldn't be used otherwise.
	 * 
	 * @param orderLineIdentifier
	 *            The string representation of the identifier.
	 */
	@Deprecated
	public OrderLineIdentifier(String orderLineIdentifier)
	{
		super(orderLineIdentifier);
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
