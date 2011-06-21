package org.salespointframework.core.accountancy.payment;

import java.io.Serializable;
import javax.persistence.*;

import org.joda.time.DateTime;
import org.salespointframework.core.accountancy.payment.PaymentMethod;

/**
 * Entity implementation class for Entity: PaymentCard
 *
 */
@Entity

public class PaymentCard extends PaymentMethod implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String cardAssociationName;
	private String cardNumber;
	private String nameOnCard;
	private String billingAddress;
	private DateTime validFrom;
	private DateTime expiryDate;
	private String cardVerificationCode;
	private String issueNumber;
	
	public PaymentCard() {
		super();
	}
   
}
