package org.salespointframework.core.order;

import java.io.Serializable;

import javax.persistence.*;
import org.salespointframework.util.SalespointIdentifier;

/**
 * Entity implementation class for Entity: OrderLineIdentifier
 * 
 * @author hannesweisbach
 */
@Embeddable
public class OrderLineIdentifier extends SalespointIdentifier implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3266292510417825915L;

	public OrderLineIdentifier() {
		super();
	}

}
