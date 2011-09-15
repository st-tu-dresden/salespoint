package org.salespointframework.core.order;

import javax.persistence.Embeddable;

import org.salespointframework.util.SalespointIdentifier;

@SuppressWarnings("serial")
@Embeddable
public final class OrderIdentifier extends SalespointIdentifier
{
	public OrderIdentifier()
	{
		super();
	}

	public OrderIdentifier(String orderIdentifier)
	{
		super(orderIdentifier);
	}
}
