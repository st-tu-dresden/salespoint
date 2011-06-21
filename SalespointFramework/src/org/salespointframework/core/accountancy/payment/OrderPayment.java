package org.salespointframework.core.accountancy.payment;

import java.io.Serializable;
import javax.persistence.*;
import org.salespointframework.core.accountancy.payment.Payment;

/**
 * Entity implementation class for Entity: OrderPayment
 *
 */
@Entity

public class OrderPayment extends Payment implements Serializable {

	private static final long serialVersionUID = 1L;

	private String fromAccount;
	private String toAccount;
	
	public OrderPayment() {
		super();
	}
   
}
