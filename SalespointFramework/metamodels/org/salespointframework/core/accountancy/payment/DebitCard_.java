package org.salespointframework.core.accountancy.payment;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.salespointframework.core.money.Money;

@Generated(value="EclipseLink-2.3.0.v20110604-r9504", date="2011-07-18T11:45:00")
@StaticMetamodel(DebitCard.class)
public class DebitCard_ extends PaymentCard_ {

    public static volatile SingularAttribute<DebitCard, Money> dailyWithdrawalLimit;

}