package org.salespointframework.useraccount;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Value;

import java.io.Serializable;

import javax.persistence.Embeddable;

/**
 * A Role is only identified by a name. This class is immutable.
 * 
 * @author Christopher Bellmann
 * @author Paul Henke
 */
@Value
@Embeddable
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Role implements Serializable, Comparable<Role> {

	private static final long serialVersionUID = 5440415491197908839L;

	/**
	 * The name of the role.
	 */
	@NonNull String name;

	/**
	 * Creates a new {@link Role} instance with the given name.
	 * 
	 * @param name the name of the Role, must not be {@literal null} or empty.
	 */
	public static Role of(String name) {
		return new Role(name);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
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
