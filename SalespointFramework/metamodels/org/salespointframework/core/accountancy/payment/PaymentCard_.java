package org.salespointframework.core.accountancy.payment;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.joda.time.DateTime;

@Generated(value="EclipseLink-2.3.0.v20110604-r9504", date="2011-07-18T11:45:00")
@StaticMetamodel(PaymentCard.class)
public class PaymentCard_ extends PaymentMethod_ {

    public static volatile SingularAttribute<PaymentCard, DateTime> expiryDate;
    public static volatile SingularAttribute<PaymentCard, String> nameOnCard;
    public static volatile SingularAttribute<PaymentCard, DateTime> validFrom;
    public static volatile SingularAttribute<PaymentCard, String> billingAddress;
    public static volatile SingularAttribute<PaymentCard, String> cardAssociationName;
    public static volatile SingularAttribute<PaymentCard, String> cardVerificationCode;
    public static volatile SingularAttribute<PaymentCard, String> cardNumber;

}