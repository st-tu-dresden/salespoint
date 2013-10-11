package org.salespointframework.core.user;

import java.io.Serializable;

import javax.persistence.Embeddable;

import java.util.Objects;

/**
 * A Capability is only identified by a name.
 * This class is immutable.
 * 
 * @author Christopher Bellmann
 * 
 */
@SuppressWarnings("serial")
@Embeddable
public final class Capability implements Serializable, Comparable<Capability>
{
	private final String name;

	/**
	 * Protected, parameterless Constructor required by the persistence layer.
	 * Do not use it.
	 */
	@Deprecated
	protected Capability() {
		name = "";
	}
	
	/**
	 * Creates a new Capability
	 * 
	 * @param name the name of the capability  
	 *             
	 * @throws NullPointerException if name is null
	 */
	public Capability(String name)
	{
		this.name = Objects.requireNonNull(name, "name must not be null");
	}

	/**
	 * @return the name of the Capability
	 */
	public final String getName()
	{
		return name;
	}

	@Override
	public final boolean equals(Object other)
	{
		if (other == null)
		{
			return false;
		}
		if (other == this)
		{
			return true;
		}
		if (other instanceof Capability)
		{
			return this.name.equals(((Capability) other).name);
		}
		return false;
	}

	@Override
	public final int hashCode()
	{
		return name.hashCode();
	}

	@Override
	public final String toString()
	{
		return name;
	}

	@Override
	public final int compareTo(Capability other)
	{
		return this.name.compareTo(other.name);
	}
}
