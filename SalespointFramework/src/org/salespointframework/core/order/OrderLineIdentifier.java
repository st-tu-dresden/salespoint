package org.salespointframework.core.order;

import javax.persistence.Embeddable;

import org.salespointframework.util.SalespointIdentifier;

@SuppressWarnings("serial")
@Embeddable
public final class OrderLineIdentifier extends SalespointIdentifier
{
	public OrderLineIdentifier()
	{
		super();
	}

	public OrderLineIdentifier(String orderLineIdentifier)
	{
		super(orderLineIdentifier);
	}
}
