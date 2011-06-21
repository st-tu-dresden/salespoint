package org.salespointframework.core.accountancy.payment;

import java.io.Serializable;
import javax.persistence.*;

import org.joda.time.DateTime;

/**
 * Entity implementation class for Entity: Payment
 *
 */
@Entity

public class Payment implements Serializable {
	@Id @GeneratedValue(strategy=GenerationType.AUTO) long id;
	
	private static final long serialVersionUID = 1L;

	private PaymentMethod paymentMethod;
	private DateTime dateMade;
	private DateTime dateReceived;
	private DateTime dateDue;
	private DateTime dateCleared;
	
	protected Payment() {
		super();
	}
   
}
