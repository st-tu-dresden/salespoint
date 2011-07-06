package org.salespointframework.util;

import java.util.UUID;

import javax.persistence.*;

/**
 * 
 * @author hannesweisbach
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
}
