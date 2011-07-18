package org.salespointframework.core.accountancy.payment;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.salespointframework.core.money.Money;

@Generated(value="EclipseLink-2.3.0.v20110604-r9504", date="2011-07-18T11:45:00")
@StaticMetamodel(CreditCard.class)
public class CreditCard_ extends PaymentCard_ {

    public static volatile SingularAttribute<CreditCard, Money> dailyWithdrawalLimit;
    public static volatile SingularAttribute<CreditCard, Money> creditLimit;

}