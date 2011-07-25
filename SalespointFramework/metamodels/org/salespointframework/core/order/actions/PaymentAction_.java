package org.salespointframework.core.order.actions;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.salespointframework.core.accountancy.ProductPaymentEntry;
import org.salespointframework.core.accountancy.payment.OrderPayment;
import org.salespointframework.core.order.InvoiceIdentifier;
import org.salespointframework.core.order.OrderLineIdentifier;

@Generated(value="EclipseLink-2.3.0.v20110604-r9504", date="2011-07-25T11:37:45")
@StaticMetamodel(PaymentAction.class)
public class PaymentAction_ extends OrderAction_ {

    public static volatile SingularAttribute<PaymentAction, OrderPayment> orderPayment;
    public static volatile SingularAttribute<PaymentAction, InvoiceIdentifier> invoiceIdentifier;
    public static volatile SingularAttribute<PaymentAction, ProductPaymentEntry> productPaymentEntry;
    public static volatile SingularAttribute<PaymentAction, OrderLineIdentifier> oderLineIdentifier;

}