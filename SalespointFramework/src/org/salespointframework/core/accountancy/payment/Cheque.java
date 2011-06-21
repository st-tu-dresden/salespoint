package org.salespointframework.core.accountancy.payment;

import java.io.Serializable;
import javax.persistence.*;

import org.joda.time.DateTime;
import org.salespointframework.core.accountancy.payment.PaymentMethod;

/**
 * Entity implementation class for Entity: Cheque
 *
 */
@Entity

public class Cheque extends PaymentMethod implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String accountName;
	private String accountNumber;
	private String chequeNumber;
	private String payee;
	private DateTime dateWritten;
	private String bankName;
	private String bankAddress;
	private String bankIdentificationNumber;
	
	public Cheque() {
		super();
	}
   
}
