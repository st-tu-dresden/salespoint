package org.salespointframework.core.order;

import javax.persistence.Embeddable;

import org.salespointframework.util.SalespointIdentifier;

/**
 * TODO
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
}
