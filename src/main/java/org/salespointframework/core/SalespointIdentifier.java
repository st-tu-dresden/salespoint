package org.salespointframework.core;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

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
@RequiredArgsConstructor
@EqualsAndHashCode
public class SalespointIdentifier implements Serializable {

	private static final long serialVersionUID = -859038278950680970L;

	private final @Column(unique = true) String id;

	public SalespointIdentifier() {
		this.id = UUID.randomUUID().toString();
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
}
