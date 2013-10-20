package org.salespointframework.core.useraccount;

import java.io.Serializable;

import javax.persistence.Embeddable;

import java.util.Objects;

/**
 * A Role is only identified by a name.
 * This class is immutable.
 * 
 * @author Christopher Bellmann
 * @author Paul Henke
 * 
 */
@SuppressWarnings("serial")
@Embeddable
public final class Role implements Serializable, Comparable<Role>
{
	private final String name;

	/**
	 * Protected, parameterless Constructor required by the persistence layer.
	 * Do not use it.
	 */
	@Deprecated
	protected Role() {
		name = "";
	}
	
	/**
	 * Creates a new Role
	 * 
	 * @param name the name of the Role  
	 *             
	 * @throws NullPointerException if name is null
	 */
	public Role(String name)
	{
		this.name = Objects.requireNonNull(name, "name must not be null");
	}

	/**
	 * @return the name of the Role
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
		if (other instanceof Role)
		{
			return this.name.equals(((Role) other).name);
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
	public final int compareTo(Role other)
	{
		return this.name.compareTo(other.name);
	}
}
