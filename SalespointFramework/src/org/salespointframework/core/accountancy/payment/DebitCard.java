package org.salespointframework.core.accountancy.payment;

import java.io.Serializable;
import javax.persistence.*;
import org.salespointframework.core.accountancy.payment.PaymentCard;
import org.salespointframework.core.money.Money;

/**
 * Entity implementation class for Entity: DebitCard
 *
 */
@Entity

public class DebitCard extends PaymentCard implements Serializable {

	private static final long serialVersionUID = 1L;

	private Money dailyWithdrawalLimit;
	
	public DebitCard() {
		super();
	}
   
}
