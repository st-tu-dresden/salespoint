package org.salespointframework.core.order.actions;

import java.io.Serializable;
import javax.persistence.*;

import org.salespointframework.core.accountancy.ProductPaymentEntry;
import org.salespointframework.core.accountancy.payment.OrderPayment;
import org.salespointframework.core.order.actions.OrderAction;
import org.salespointframework.util.Objects;

/**
 * Entity implementation class for Entity: PaymentAction
 * 
 * @author hannesweisbach
 */
@Entity
public class PaymentAction extends OrderAction implements Serializable {
	private static final long serialVersionUID = 1L;

	// FIXME implement classes
	// FIXME: an OrderLineIdentifier makes no sense whatsoever here. WTF?!
	// private OrderLineIndentifier oderLineIdentifier;
	// private InvoiceIdentifier InvoiceIdentifier;
	@OneToOne(cascade=CascadeType.ALL)
	private OrderPayment orderPayment;
	@OneToOne(cascade=CascadeType.ALL, mappedBy="paymentAction", targetEntity=ProductPaymentEntry.class)
	private ProductPaymentEntry productPaymentEntry;

	@Deprecated
	protected PaymentAction() {}
	
	public PaymentAction(OrderPayment orderPayment) {
		super();
		this.orderPayment = Objects.requireNonNull(orderPayment, "orderPAyment");
		this.productPaymentEntry = new ProductPaymentEntry(this);
	}

}
