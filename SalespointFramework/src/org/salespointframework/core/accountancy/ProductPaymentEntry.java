package org.salespointframework.core.accountancy;

import java.io.Serializable;
import javax.persistence.*;
import org.salespointframework.core.accountancy.AbstractAccountancyEntry;
import org.salespointframework.core.order.actions.OrderAction;
import org.salespointframework.core.order.actions.PaymentAction;
import org.salespointframework.util.Objects;

/**
 * A <code>ProductPaymentEntry</code> is an
 * <code>AbstractAccountancyEntry</code> used to store information of payments
 * of orders. For this purpose, a <code>ProductPaymentEntry</code> holds a
 * reference to a <code>OrderAction</code>, specifically a
 * <code>PaymentAction</code>.
 * 
 * @author hannesweisbach
 */
@Entity
public class ProductPaymentEntry extends AbstractAccountancyEntry implements
		Serializable {

	private static final long serialVersionUID = 1L;

	@OneToOne(cascade = CascadeType.ALL, targetEntity = OrderAction.class)
	private PaymentAction paymentAction;

	/**
	 * Parameterless constructor required for JPA. Do not use.
	 */
	@Deprecated
	protected ProductPaymentEntry() {
	}

	/**
	 * A <code>ProductPaymentEntry</code> is constructed with a specific
	 * <code>PaymentAction</code> attached to it. The <code>paymentAction</code>
	 * must be non-null.
	 * 
	 * @param paymentAction
	 *            the <code>PaymentAction</code> to which this
	 *            <code>ProductPaymentEntry</code> will refer to.
	 */
	public ProductPaymentEntry(PaymentAction paymentAction) {
		this.paymentAction = Objects.requireNonNull(paymentAction,
				"paymentAction");
	}

	/**
	 * Return the <code>PaymentAction</code> to which this
	 * <code>ProductPaymentEntry</code> refers to.
	 * 
	 * @return the <code>PaymentAction</code>, to which this
	 *         <code>ProductPaymentEntry</code> refers to
	 */
	public PaymentAction getPaymentAction() {
		return paymentAction;
	}

}
