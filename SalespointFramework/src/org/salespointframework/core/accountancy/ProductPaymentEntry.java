package org.salespointframework.core.accountancy;

import java.io.Serializable;
import javax.persistence.*;
import org.salespointframework.core.accountancy.AccountancyEntry;
import org.salespointframework.core.order.actions.OrderAction;
import org.salespointframework.core.order.actions.PaymentAction;
import org.salespointframework.util.Objects;

/**
 * Entity implementation class for Entity: ProductPaymentEntry
 * 
 * @author hannesweisbach
 */
@Entity
public class ProductPaymentEntry extends AccountancyEntry implements
		Serializable {

	private static final long serialVersionUID = 1L;

	@OneToOne(cascade=CascadeType.ALL, targetEntity=OrderAction.class)
	private PaymentAction paymentAction;
	
	@Deprecated
	protected ProductPaymentEntry() {}
	
	public ProductPaymentEntry(PaymentAction paymentAction) {
		this.paymentAction = Objects.requireNonNull(paymentAction,
				"paymentAction");
	}

	public PaymentAction getPaymentAction() {
		return paymentAction;
	}

}
