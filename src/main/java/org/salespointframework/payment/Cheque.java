package org.salespointframework.payment;

import java.time.LocalDateTime;

import org.springframework.util.Assert;

/**
 * A is a written bill of exchange (or draft), a written order by the drawer (the writer of the cheque) to the drawee
 * (usually the bank), to pay a specified amount of money to a payee. The class <code>Cheque</code> represents such a
 * written order of payment in a digital form.
 * 
 * @author Hannes Weisbach
 * @author Oliver Gierke
 */
@SuppressWarnings("serial")
public final class Cheque extends PaymentMethod {

	private final String accountName;
	private final String accountNumber;
	private final String chequeNumber;
	private final String payee;
	private final LocalDateTime dateWritten;
	private final String bankName;
	private final String bankAddress;
	private final String bankIdentificationNumber;

	/**
	 * Instantiate a new <code>Cheque</code> instance.
	 * 
	 * @param accountName Name of the account holder.
	 * @param accountNumber Number of this account on which the cheque is drawn.
	 * @param chequeNumber Number of this cheque. This is a number uniquely identifying a cheque.
	 * @param payee Name of the party which receives the cheque.
	 * @param dateWritten {@link DateTime} object representing the date on which the cheque was written,
	 * @param bankName Name of the bank that issued the cheque,
	 * @param bankAddress Address of the bank that issued the cheque.
	 * @param bankIdentificationNumber Unique identifier of the bank that issued the cheque. Also known as the bank
	 *          routing number.
	 */
	public Cheque(String accountName, String accountNumber, String chequeNumber, String payee, LocalDateTime dateWritten,
			String bankName, String bankAddress, String bankIdentificationNumber) {

		super("Cheque");

		Assert.notNull(accountName, "accountName must be not null");
		Assert.notNull(accountNumber, "accountNumber must be not null");
		Assert.notNull(chequeNumber, "chequeNumber must be not null");
		Assert.notNull(payee, "payee must be not null");
		Assert.notNull(dateWritten, "dateWritten must be not null");
		Assert.notNull(bankName, "bankName must be not null");
		Assert.notNull(bankAddress, "bankAddress must be not null");
		Assert.notNull(bankIdentificationNumber, "bankIdentificationNumber must be not null");

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
	 * @return String containing the account number on which the cheque is drawn.
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
	public LocalDateTime getDateWritten() {
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
