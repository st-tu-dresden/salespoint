package org.salespointframework.core.order.actions;

import java.io.Serializable;
import javax.persistence.*;

import org.salespointframework.core.accountancy.ProductPaymentEntry;
import org.salespointframework.core.accountancy.payment.OrderPayment;
import org.salespointframework.core.order.OrderIdentifier;
import org.salespointframework.core.order.OrderLineIdentifier;
import org.salespointframework.core.order.InvoiceIdentifier;
import org.salespointframework.core.order.actions.OrderAction;
import org.salespointframework.util.Objects;

/**
 * Entity implementation class for Entity: PaymentAction
 * 
 * @author hannesweisbach
 */
@Entity
public class PaymentAction extends OrderAction {

	// FIXME: an OrderLineIdentifier makes no sense whatsoever here. WTF?!
	@Embedded
	@AttributeOverride(name="id", column=@Column(name="ORDERLINE_ID"))
	private OrderLineIdentifier oderLineIdentifier;
	
	@Embedded
	@AttributeOverride(name="id", column=@Column(name="INVOICE_ID"))
	private InvoiceIdentifier invoiceIdentifier;
	
	@OneToOne(cascade=CascadeType.ALL)
	private OrderPayment orderPayment;
	
	//TODO: does a PaymentAction really need a reference to a ProductPaymentEntry?
	@OneToOne(cascade=CascadeType.ALL, mappedBy="paymentAction", targetEntity=ProductPaymentEntry.class)
	private ProductPaymentEntry productPaymentEntry;

	@Deprecated
	protected PaymentAction() {}
	
	public PaymentAction(OrderPayment orderPayment) {
		//TODO fix orderLineIdentifier, etc
		super(new OrderIdentifier());
		this.orderPayment = Objects.requireNonNull(orderPayment, "orderPayment");
		this.productPaymentEntry = new ProductPaymentEntry(this);
	}

	/**
	 * @return the productPaymentEntry
	 */
	public ProductPaymentEntry getProductPaymentEntry() {
		return productPaymentEntry;
	}
	

}
