package org.salespointframework.core.users;

import java.io.Serializable;

import javax.persistence.Embeddable;

/**
 * A UserCapability is identified by a name and nothing else. You connect your
 * Capabilities to the User in your {@link PersistentUserManager}.
 * 
 * @author Christopher Bellmann
 * 
 */
@SuppressWarnings("serial")
@Embeddable
public final class UserCapability implements Serializable {

	private final String name;

	@Deprecated
	public UserCapability() {
		this.name = null;
	};

	/**
	 * Creates a new UserCapability
	 * 
	 * @param name
	 *            name you want the give the Capability
	 */
	public UserCapability(String name) {
		this.name = name;
	}

	/**
	 * @return the name of the Capability
	 */
	public final String getName() {
		return name;
	}

	@Override
	public final int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object other) {
		if(other == null) return false;
		if(other == this) return true;
		if(!(other instanceof UserCapability)) return false;
		return equals((UserCapability) other);
	}

	public final boolean equals(UserCapability other) {
		if(other == null) return false;
		if(other == this) return true;
		return this.name.equals(other.name);
	}

	@Override
	public String toString() {
		return name;
	}
}
