package org.salespointframework.util;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.*;

/**
 * 
 * @author hannesweisbach
 * @author Thomas Dedek
 * 
 */
@Embeddable
@MappedSuperclass
public /*abstract*/ class SalespointIdentifier implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5689127925646322527L;
	//@Id
	private String id;

	public SalespointIdentifier() {
		id = UUID.randomUUID().toString();
	}
	
	public SalespointIdentifier(String id) {
		this.id = id;
	}
	
	public String getIdentifier() {
		return id;
	}
	
	@Override
	public String toString() {
		return id;
	}
	
	@Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (!(other instanceof SalespointIdentifier)) return false;
        return this.equals((SalespointIdentifier) other);
    }
	
	public boolean equals(SalespointIdentifier other) {
        if (other == this) return true;
        if (other == null) return false;
		return this.getIdentifier().equals(other.getIdentifier());
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
}
