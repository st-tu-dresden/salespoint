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
	private UUID id;

	public SalespointIdentifier() {
		id = UUID.randomUUID();
	}

	public String getIdentifier() {
		return id.toString();
	}
}
