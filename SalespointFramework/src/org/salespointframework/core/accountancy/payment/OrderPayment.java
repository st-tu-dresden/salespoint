package org.salespointframework.core.accountancy.payment;

import java.io.Serializable;
import javax.persistence.*;

import org.joda.time.DateTime;
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

	@Deprecated
	protected OrderPayment() {}

	/**
	 * @param fromAccount
	 * @param toAccount
	 */
	public OrderPayment(PaymentMethod paymentMethod, DateTime dateDue,
			String fromAccount, String toAccount) {
		super(paymentMethod, dateDue);
		this.fromAccount = fromAccount;
		this.toAccount = toAccount;
	}

	/**
	 * @return the fromAccount
	 */
	public String getFromAccount() {
		return fromAccount;
	}

	/**
	 * @return the toAccount
	 */
	public String getToAccount() {
		return toAccount;
	}

}
