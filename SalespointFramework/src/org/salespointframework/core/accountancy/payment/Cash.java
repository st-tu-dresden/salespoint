package org.salespointframework.core.accountancy.payment;

import java.io.Serializable;
import org.salespointframework.core.accountancy.payment.PaymentMethod;

/**
 * The <code>Cash</code> <code>PaymentMethod</code> is used to designate all
 * payments made in cash.
 */
// @Entity
public class Cash extends PaymentMethod implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * A convenience instance <code>CASH</code> is defined in this class, which
	 * can be reused, instead of instantiating a new instance every time one is
	 * needed.
	 */
	public static final Cash CASH = new Cash();

	public Cash() {
		super("Cash");
	}

}
