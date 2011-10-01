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
}
