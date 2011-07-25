package org.salespointframework.core.order;

import java.io.Serializable;

import javax.persistence.*;
import org.salespointframework.util.SalespointIdentifier;

/**
 * Entity implementation class for Entity: OrderIdentifier
 *
 */
@Embeddable

public class OrderIdentifier extends SalespointIdentifier implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 475313836782250338L;

	public OrderIdentifier() {
		super();
	}
	
}
