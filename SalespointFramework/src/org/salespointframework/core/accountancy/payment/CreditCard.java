package org.salespointframework.core.accountancy.payment;

import java.io.Serializable;
import javax.persistence.*;
import org.salespointframework.core.accountancy.payment.PaymentCard;
import org.salespointframework.core.money.Money;

/**
 * Entity implementation class for Entity: CreditCard
 *
 */
@Entity

public class CreditCard extends PaymentCard implements Serializable {

	private static final long serialVersionUID = 1L;

	private Money dailyWithdrawalLimit;
	private Money creditLimit;
	
	public CreditCard() {
		super();
	}
   
}
