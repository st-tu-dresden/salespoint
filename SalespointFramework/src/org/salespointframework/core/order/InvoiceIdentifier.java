package org.salespointframework.core.order;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Entity;

import org.salespointframework.util.SalespointIdentifier;

/**
 * 
 * @author hannesweisbach
 * 
 */
@Embeddable
public class InvoiceIdentifier extends SalespointIdentifier implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3826370059021652841L;

	public InvoiceIdentifier() {
		super();
	}
}
