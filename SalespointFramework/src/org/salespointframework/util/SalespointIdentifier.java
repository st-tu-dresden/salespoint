package org.salespointframework.util;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Identifier
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
