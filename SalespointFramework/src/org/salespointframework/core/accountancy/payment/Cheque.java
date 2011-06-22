package org.salespointframework.core.accountancy.payment;

import java.io.Serializable;
import javax.persistence.*;

import org.joda.time.DateTime;
import org.salespointframework.core.accountancy.payment.PaymentMethod;

/**
 * 
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

	@Deprecated
	protected Cheque() {
	}

	/**
	 * @param accountName
	 *            Name of the account holder.
	 * @param accountNumber
	 *            Number of this account on which the cheque is drawn.
	 * @param chequeNumber
	 *            Number of this cheque. This is a number uniquely identifying a
	 *            cheque.
	 * @param payee
	 *            Name of the party which receives the cheque.
	 * @param dateWritten
	 *            <code>DateTime</code> object representing the date on which
	 *            the cheque was written,
	 * @param bankName
	 *            Name of the bank that issued the cheque,
	 * @param bankAddress
	 *            Address of the bank that issued the cheque.
	 * @param bankIdentificationNumber
	 *            Unique identifier of the bank that issued the cheque. Also
	 *            known as the bank routing number.
	 */
	public Cheque(String accountName, String accountNumber,
			String chequeNumber, String payee, DateTime dateWritten,
			String bankName, String bankAddress, String bankIdentificationNumber) {
		super();
		this.accountName = accountName;
		this.accountNumber = accountNumber;
		this.chequeNumber = chequeNumber;
		this.payee = payee;
		this.dateWritten = dateWritten;
		this.bankName = bankName;
		this.bankAddress = bankAddress;
		this.bankIdentificationNumber = bankIdentificationNumber;
	}

	/**
	 * @return the accountName
	 */
	public String getAccountName() {
		return accountName;
	}

	/**
	 * @return the accountNumber
	 */
	public String getAccountNumber() {
		return accountNumber;
	}

	/**
	 * @return the chequeNumber
	 */
	public String getChequeNumber() {
		return chequeNumber;
	}

	/**
	 * @return the payee
	 */
	public String getPayee() {
		return payee;
	}

	/**
	 * @return the dateWritten
	 */
	public DateTime getDateWritten() {
		return dateWritten;
	}

	/**
	 * @return the bankName
	 */
	public String getBankName() {
		return bankName;
	}

	/**
	 * @return the bankAddress
	 */
	public String getBankAddress() {
		return bankAddress;
	}

	/**
	 * @return the bankIdentificationNumber
	 */
	public String getBankIdentificationNumber() {
		return bankIdentificationNumber;
	}

}
