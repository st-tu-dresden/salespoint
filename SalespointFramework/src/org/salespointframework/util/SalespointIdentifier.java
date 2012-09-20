package org.salespointframework.util;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.MappedSuperclass;

/**
 * 
 * @author Hannes Weisbach
 * @author Thomas Dedek
 * 
 */
@SuppressWarnings("serial")
//@Embeddable
@MappedSuperclass
public class SalespointIdentifier implements Serializable, Comparable<SalespointIdentifier>
{
	private final String id;

	public SalespointIdentifier()
	{
		id = UUID.randomUUID().toString();
	}

	public SalespointIdentifier(String id)
	{
		this.id = id;
	}

	public String getIdentifier()
	{
		return id;
	}

	@Override
	public String toString()
	{
		return id;
	}

	@Override
	public boolean equals(Object other)
	{
		if (other == null)
		{
			return false;
		}
		if (other == this)
		{
			return true;
		}
		if (other instanceof SalespointIdentifier)
		{
			return this.id.equals(((SalespointIdentifier) other).id);
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		return id.hashCode();
	}

	@Override
	public int compareTo(SalespointIdentifier other)
	{
		return this.id.compareTo(other.id);
	}
}
