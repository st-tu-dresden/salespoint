package org.salespointframework.core.accountancy.payment;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.joda.time.DateTime;
import org.salespointframework.core.accountancy.payment.PaymentMethod;

@Generated(value="EclipseLink-2.3.0.v20110604-r9504", date="2011-07-18T11:45:00")
@StaticMetamodel(Payment.class)
public class Payment_ { 

    public static volatile SingularAttribute<Payment, Long> id;
    public static volatile SingularAttribute<Payment, DateTime> dateMade;
    public static volatile SingularAttribute<Payment, DateTime> dateCleared;
    public static volatile SingularAttribute<Payment, DateTime> dateDue;
    public static volatile SingularAttribute<Payment, DateTime> dateReceived;
    public static volatile SingularAttribute<Payment, PaymentMethod> paymentMethod;

}