package org.salespointframework.useraccount;

import java.io.Serializable;

import javax.persistence.Embeddable;

import org.springframework.util.Assert;

/**
 * A Role is only identified by a name. This class is immutable.
 * 
 * @author Christopher Bellmann
 * @author Paul Henke
 */
@Embeddable
@SuppressWarnings("serial")
public final class Role implements Serializable, Comparable<Role> {

	private final String name;

	/**
	 * Protected, parameterless Constructor required by the persistence layer. Do not use it.
	 */
	@Deprecated
	protected Role() {
		name = "";
	}

	/**
	 * Creates a new Role
	 * 
	 * @param name the name of the Role
	 * @throws NullPointerException if name is null
	 */
	public Role(String name) {

		Assert.hasText(name, "Name must not be null!");
		this.name = name;
	}

	/**
	 * @return the name of the Role
	 */
	public final String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(Object other) {

		if (other == this) {
			return true;
		}

		if (!(other instanceof Role)) {
			return false;
		}

		return this.name.equals(((Role) other).name);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		return name.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public final int compareTo(Role other) {
		return this.name.compareTo(other.name);
	}
}
