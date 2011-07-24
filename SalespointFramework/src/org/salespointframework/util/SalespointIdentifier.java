package org.salespointframework.util;

import java.util.UUID;

import javax.persistence.*;

/**
 * 
 * @author hannesweisbach
 * @author Thomas Dedek
 * 
 */
@Entity
public class SalespointIdentifier {
	@Id
	private String id;

	public SalespointIdentifier() {
		id = UUID.randomUUID().toString();
	}

	public String getIdentifier() {
		return id;
	}
	
	public String toString() {
		return id;
	}
	
	@Override
    public boolean equals(Object obj) {
		
        if (obj == this) return true;
        if (obj == null) return false;
        if (!(obj instanceof SalespointIdentifier)) return false;
        
        SalespointIdentifier si = (SalespointIdentifier) obj;
        return this.getIdentifier().equals(si.getIdentifier());
    }
}
