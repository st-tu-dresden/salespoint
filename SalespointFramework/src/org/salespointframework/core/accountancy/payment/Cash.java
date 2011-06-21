package org.salespointframework.core.accountancy.payment;

import java.io.Serializable;
import javax.persistence.*;
import org.salespointframework.core.accountancy.payment.PaymentMethod;

/**
 * Entity implementation class for Entity: Cash
 *
 */
@Entity

public class Cash extends PaymentMethod implements Serializable {

	
	private static final long serialVersionUID = 1L;

	public Cash() {
		super();
	}
   
}
