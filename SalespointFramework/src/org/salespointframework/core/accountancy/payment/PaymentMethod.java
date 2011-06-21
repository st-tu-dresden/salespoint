package org.salespointframework.core.accountancy.payment;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: PaymentMethod
 *
 */
@Entity

public abstract class PaymentMethod implements Serializable {
	@Id @GeneratedValue(strategy=GenerationType.AUTO) long id;
	
	private static final long serialVersionUID = 1L;

	protected PaymentMethod() {
		super();
	}
   
}
