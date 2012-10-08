package org.salespointframework.core.accountancy.payment;

import java.util.Objects;

import org.joda.time.DateTime;
import org.salespointframework.core.accountancy.payment.PaymentMethod;

/**
 * A is a written bill of exchange (or draft), a written order by the drawer
 * (the writer of the cheque) to the drawee (usually the bank), to pay a
 * specified amount of money to a payee. The class <code>Cheque</code>
 * represents such a written order of payment in a digital form.
 * 
 * @author Hannes Weisbach
 * 
 */
@SuppressWarnings("serial")
public final class Cheque extends PaymentMethod {

	private final String accountName;

	private final String accountNumber;

	private final String chequeNumber;

	private final String payee;

	private final DateTime dateWritten;

	private final String bankName;
	// TODO Address class

	private final String bankAddress;

	private final String bankIdentificationNumber;

	/**
	 * Instantiate a new <code>Cheque</code> instance.
	 * 
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
		super("Cheque");
		this.accountName = Objects.requireNonNull(accountName, "accountName must be not null");
		this.accountNumber = Objects.requireNonNull(accountNumber, "accountNumber must be not null");
		this.chequeNumber = Objects.requireNonNull(chequeNumber, "chequeNumber must be not null");
		this.payee = Objects.requireNonNull(payee, "payee must be not null");
		this.dateWritten = Objects.requireNonNull(dateWritten, "dateWritten must be not null");
		this.bankName = Objects.requireNonNull(bankName, "bankName must be not null");
		this.bankAddress = Objects.requireNonNull(bankAddress, "bankAddress must be not null");
		this.bankIdentificationNumber = Objects.requireNonNull(bankIdentificationNumber, "bankIdentificationNumber must be not null");
	}

	/**
	 * Name of the account holder.
	 * 
	 * @return String containing the name of the account holder.
	 */
	public String getAccountName() {
		return accountName;
	}

	/**
	 * Number of the account on which the cheque is drawn.
	 * 
	 * @return String containing the account number on which the cheque is
	 *         drawn.
	 */
	public String getAccountNumber() {
		return accountNumber;
	}

	/**
	 * Number uniquely identifying this cheque.
	 * 
	 * @return String containing the cheque number.
	 */
	public String getChequeNumber() {
		return chequeNumber;
	}

	/**
	 * Name of the payee.
	 * 
	 * @return String containing the name of the payee.
	 */
	public String getPayee() {
		return payee;
	}

	/**
	 * Date on which the cheque was written.
	 * 
	 * @return DateTime containing the date on which the cheque was written.
	 */
	public DateTime getDateWritten() {
		return dateWritten;
	}

	/**
	 * Name of the bank, that issued the cheque.
	 * 
	 * @return String containing the name of the bank.
	 */
	public String getBankName() {
		return bankName;
	}

	/**
	 * Address of the bank, that issued the cheque.
	 * 
	 * @return String containing the address of the bank.
	 */
	public String getBankAddress() {
		return bankAddress;
	}

	/**
	 * Unique identification number of this bank. Also known as routing number.
	 * 
	 * @return String containing the identification number of this bank.
	 */
	public String getBankIdentificationNumber() {
		return bankIdentificationNumber;
	}

}
