package org.salespointframework.core.accountancy.payment;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A <code>PaymentMethod</code> specifies a medium by which a payment has or
 * will be made.
 * 
 */
@Entity
public /*abstract */ class PaymentMethod implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	long id;

	private static final long serialVersionUID = 1L;

	protected PaymentMethod() {}

}
