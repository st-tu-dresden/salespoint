package org.salespointframework.core.accountancy.payment;

import java.io.Serializable;
import javax.persistence.*;

import org.joda.time.DateTime;

/**
 * A <code>Payment</code> represents a payment from one party to another in
 * exchange for goods or services.
 * 
 */
@Entity
public class Payment implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	long id;

	private static final long serialVersionUID = 1L;

	/**
	 * A method of payment associated with a particular payment
	 */
	@OneToOne(cascade=CascadeType.ALL)
	protected PaymentMethod paymentMethod;

	/**
	 * A <code>DateTime</code> object representing the time, the payment was
	 * made by the payer.
	 */
	private DateTime dateMade;

	/**
	 * A <code>DateTime</code> object representing the time, the payment was
	 * received by the payee.
	 */
	private DateTime dateReceived;

	/**
	 * A <code>DateTime</code> object representing the time, the payee expects
	 * the payment to be complete.
	 */
	protected DateTime dateDue;

	/**
	 * A <code>DateTime</code> object representing the time, the payment has
	 * been cleared, for example by a banking system.
	 */
	private DateTime dateCleared;

	/**
	 * This constructor may not be used. It is required by the JPA.
	 * 
	 */
	@Deprecated
	protected Payment() {}

	/**
	 * Instantiates a new <code>Payment</code>-object, specifying a
	 * <code>PaymentMethod</code> and a <code>DateTime</code> when the Payment
	 * is due.
	 * 
	 * @param paymentMethod
	 *            the method used for this <code>Payment</code>
	 * @param dateDue
	 *            the date this <code>Payment</code> is due
	 */
	public Payment(PaymentMethod paymentMethod, DateTime dateDue) {
		this.paymentMethod = paymentMethod;
		this.dateDue = dateDue;
	}

	/**
	 * This method initializes the <code>dateReceived</code> attribute of this
	 * <code>Payment</code> instance to the current system time of the method
	 * call. The <code>dateReceived</code> can only be initialized once,
	 * subsequent calls to this method have no effect.
	 * 
	 */
	public void paymentReceived() {
		if (dateReceived == null)
			dateReceived = new DateTime();
	}

	/**
	 * This method initializes the <code>dateCleared</code> attribute of this
	 * <code>Payment</code> instance to the current system time of the method
	 * call. The <code>dateReceived</code> attribute can only be initialized
	 * once, subsequent calls to this method have no effect.
	 */
	public void paymentCleared() {
		if (dateCleared == null)
			dateCleared = new DateTime();
	}

	/**
	 * This method initializes the <code>dateMade</code> attribute of this
	 * <code>Payment</code> instance to the current system time of the method
	 * call. The <code>dateMade</code> attribute can only be initialized once,
	 * subsequent calls to this method have no effect.
	 */
	public void paymentMade() {
		if (dateMade == null)
			dateMade = new DateTime();
	}

	/**
	 * Returns the <code>PaymentMethod</code> associated with this
	 * <code>Payment</code>
	 * 
	 * @return the paymentMethod
	 */
	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	/**
	 * Returns a <code>DateTime</code> object representing the time the payment
	 * was made by the payer or <code>null</code>, if the payment was not made
	 * yet.
	 * 
	 * @return the dateMade or <code>null</code>
	 */
	public DateTime getDateMade() {
		return dateMade;
	}

	/**
	 * Returns a <code>DateTime</code> object representing the time the payment
	 * was received by the payee or <code>null</code>, if the payment has not
	 * been received yet.
	 * 
	 * @return the dateReceived or <code>null</code>
	 */
	public DateTime getDateReceived() {
		return dateReceived;
	}

	/**
	 * Returns a <code>DateTime</code> object, representing the time, when the
	 * payee expects the payment.
	 * 
	 * @return the dateDue
	 */
	public DateTime getDateDue() {
		return dateDue;
	}

	/**
	 * Returns a <code>DateTime</code> object, representing the time, the
	 * payment was cleared by a banking system, or <code>null</code>, if the
	 * payment was not cleared yet.
	 * 
	 * @return the dateCleared or <code>null</code>
	 */
	public DateTime getDateCleared() {
		return dateCleared;
	}
}
