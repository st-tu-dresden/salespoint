package org.salespointframework.core.accountancy.payment;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.salespointframework.core.accountancy.AccountancyEntryIdentifier;
import org.salespointframework.core.shop.Shop;
import org.salespointframework.util.Objects;

/**
 * A <code>Payment</code> represents a payment from one party to another in
 * exchange for goods or services.
 * 
 * @author Hannes Weisbach
 * 
 */
//TODO dateMade und dateCleared entfernen
@SuppressWarnings("serial")
public abstract class Payment implements Serializable {

    /**
     * The <code>PaymentMethod</code> associated with this <code>Payment</code>
     */
    private final PaymentMethod     paymentMethod;
    
    /**
     * Represents the time the payment
     * was made by the payer or <code>null</code>, if the payment was not made
     * yet.
     */
    private DateTime          dateMade;
    
    /**
     * Represents the time the payment
     * was received by the payee or <code>null</code>, if the payment has not
     * been received yet.
     */
    private DateTime          dateReceived;
    
    /**
     * Represents the time, when the
     * payee expects the payment.
     */
    private final DateTime          dateDue;

    /**
     * Represents the time, the payment was cleared by a banking system, or
     * <code>null</code>, if the payment was not cleared yet.
     */
    private DateTime          dateCleared;

    //TODO make this work
    private AccountancyEntryIdentifier accountancyEntryIdentifier;
    
    /**
     * 
     * @param paymentMethod
     *            A method of payment associated with a particular payment
     * @param dateDue
     *            A <code>DateTime</code> object representing the time, the
     *            payee expects the payment to be complete.
     */
    public Payment(PaymentMethod paymentMethod, DateTime dateDue) {
        this.paymentMethod = Objects.requireNonNull(paymentMethod, "paymentMethod");
        this.dateDue = Objects.requireNonNull(dateDue, "dateDue");
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
            dateReceived = Shop.INSTANCE.getTime().getDateTime();
    }

    /**
     * This method initializes the <code>dateCleared</code> attribute of this
     * <code>Payment</code> instance to the current system time of the method
     * call. The <code>dateReceived</code> attribute can only be initialized
     * once, subsequent calls to this method have no effect.
     */
    public void paymentCleared() {
        if (dateCleared == null)
            dateCleared = Shop.INSTANCE.getTime().getDateTime();
    }

    /**
     * This method initializes the <code>dateMade</code> attribute of this
     * <code>Payment</code> instance to the current system time of the method
     * call. The <code>dateMade</code> attribute can only be initialized once,
     * subsequent calls to this method have no effect.
     */
    public void paymentMade() {
        if (dateMade == null)
            dateMade = Shop.INSTANCE.getTime().getDateTime();
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
     * was made by the payer or <code>null</code> representing the time the payment
     * was received by the payee or <code>null</code>, if the payment has not
     * been received yet.>, if the payment was not made
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
