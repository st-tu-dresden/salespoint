package org.salespointframework.core.product;

import javax.persistence.Embeddable;

import org.salespointframework.util.SalespointIdentifier;

@SuppressWarnings("serial")
@Embeddable
public final class SerialNumber extends SalespointIdentifier
{
	public SerialNumber()
	{
		super();
	}

	public SerialNumber(String serialNumber)
	{
		super(serialNumber);
	}
}
