/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.salespointframework.payment;

import lombok.EqualsAndHashCode;
import lombok.Value;

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
@Value
@EqualsAndHashCode(callSuper = true)
public final class Cheque extends PaymentMethod {

	/**
	 * Name of the account holder.
	 */
	String accountName;

	/**
	 * Number of the account on which the cheque is drawn.
	 */
	String accountNumber;

	/**
	 * Number uniquely identifying this cheque.
	 */
	String chequeNumber;

	/**
	 * Name of the payee.
	 */
	String payee;

	/**
	 * Date on which the cheque was written.
	 */
	LocalDateTime dateWritten;

	/**
	 * Name of the bank, that issued the cheque.
	 */
	String bankName;

	/**
	 * Address of the bank, that issued the cheque.
	 */
	String bankAddress;

	/**
	 * Unique identification number of this bank. Also known as routing number.
	 */
	String bankIdentificationNumber;

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

	/* 
	 * (non-Javadoc)
	 * @see org.salespointframework.payment.PaymentMethod#toString()
	 */
	@Override
	public String toString() {
		return String.format("%s - %s - %s - %s", super.toString(), accountName, accountNumber, dateWritten);
	}
}
