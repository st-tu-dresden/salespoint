package org.salespointframework.core;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * @author Hannes Weisbach
 * @author Thomas Dedek
 * @author Oliver Gierke
 */
@MappedSuperclass
@Access(AccessType.FIELD)
public class SalespointIdentifier implements Serializable {

	private static final long serialVersionUID = -859038278950680970L;

	private final @Column(unique = true) String id;

	public SalespointIdentifier() {
		this.id = UUID.randomUUID().toString();
	}

	public SalespointIdentifier(String id) {
		this.id = id;
	}

	public String getIdentifier() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {

		if (other == null) {
			return false;
		}
		if (other == this) {
			return true;
		}
		if (other instanceof SalespointIdentifier) {
			return this.id.equals(((SalespointIdentifier) other).id);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return id.hashCode();
	}
}
