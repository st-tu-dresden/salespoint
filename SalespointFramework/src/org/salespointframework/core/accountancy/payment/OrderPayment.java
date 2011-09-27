package org.salespointframework.core.accountancy.payment;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.salespointframework.core.accountancy.payment.Payment;

/**
 * Payment to specifically pay for an Order.
 * 
 * @author Hannes Weisbach
 */
public class OrderPayment extends Payment implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Account from which the payment was made.
	 */
	private String fromAccount;
	
	/**
	 * Account to which the payment was made.
	 */
	private String toAccount;

	/**
	 * Instantiates a payment for an Order
	 * 
	 * @param paymentMethod
	 *            Method with which the payment was made.
	 * @param dateDue
	 *            Date on which the payment is due
	 * @param fromAccount
	 *            Account from which the payment was made
	 * @param toAccount
	 *            Account to which the payment was made
	 */
	public OrderPayment(PaymentMethod paymentMethod, DateTime dateDue,
			String fromAccount, String toAccount) {
		super(paymentMethod, dateDue);
		this.fromAccount = fromAccount;
		this.toAccount = toAccount;
	}

	/**
	 * Account from which the payment was made.
	 * 
	 * @return String containing the account from which the payment was made.
	 */
	public String getFromAccount() {
		return fromAccount;
	}

	/**
	 * Account to which the payment was made.
	 * 
	 * @return String containing the account to which the payment was made.
	 */
	public String getToAccount() {
		return toAccount;
	}

}
