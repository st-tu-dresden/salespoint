/*
 * Copyright 2017-2018 the original author or authors.
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
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.util.Assert;

/**
 * A <code>PaymentCard</code> is used to charge the cost of goods or services to an account, belonging to the party
 * identified on the card. A <code>PaymentCard</code> which has a line of credit is a {@link CreditCard}. A
 * {@link DebitCard} is a {@link PaymentCard} where the associated account is debited immediately. An example for a
 * {@link DebitCard} is an EC-card or MaestroCard. Other forms of {@link PaymentCard}s such as prepaid cards or charge
 * cards are not implemented at the moment.
 * 
 * @author Hannes Weisbach
 * @author Oliver Gierke
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public abstract class PaymentCard extends PaymentMethod {

	private static final DateTimeFormatter MONTH_YEAR = DateTimeFormatter.ofPattern("MM/YY");

	/**
	 * The name of the card association.
	 */
	private final String cardAssociationName;

	/**
	 * The number uniquely identifying this payment card.
	 */
	private final String cardNumber;

	/**
	 * The name of the party to which the card was issued to.
	 */
	private final String nameOnCard;

	/**
	 * The billing address registered with this card.
	 */
	private final String billingAddress;

	/**
	 * The date from which on the card is valid.
	 */
	private final LocalDateTime validFrom;

	/**
	 * The date on which the card expires.
	 */
	private final LocalDateTime expiryDate;

	/**
	 * The verification code of this card.
	 */
	private final String cardVerificationCode;

	/**
	 * Instantiates a <code>PaymentCard</code>.
	 * 
	 * @param cardName specific name of this card, e.g. VISA, or MasterCard
	 * @param cardAssociationName the name of the association which issued the card
	 * @param cardNumber the number of this card
	 * @param nameOnCard name of the card owner
	 * @param billingAddress the address to which account statements are sent
	 * @param validFrom date from which the card is valid
	 * @param expiryDate date on which the card expires
	 * @param cardVerificationCode verification code printed on the card or a PIN
	 */
	public PaymentCard(String cardName, String cardAssociationName, String cardNumber, String nameOnCard,
			String billingAddress, LocalDateTime validFrom, LocalDateTime expiryDate, String cardVerificationCode) {

		super(cardName);

		Assert.notNull(cardAssociationName, "cardAssociationName  must not be null");
		Assert.notNull(cardNumber, "cardNumber  must not be null");
		Assert.notNull(nameOnCard, "nameOnCard  must not be null");
		Assert.notNull(billingAddress, "billingAddress  must not be null");
		Assert.notNull(validFrom, "validFrom  must not be null");
		Assert.notNull(expiryDate, "expiryDate  must not be null");
		Assert.notNull(cardVerificationCode, "cardVerificationCode  must not be null");

		this.cardAssociationName = cardAssociationName;
		this.cardNumber = cardNumber;
		this.nameOnCard = nameOnCard;
		this.billingAddress = billingAddress;
		this.validFrom = validFrom;
		this.expiryDate = expiryDate;
		this.cardVerificationCode = cardVerificationCode;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.salespointframework.payment.PaymentMethod#toString()
	 */
	@Override
	public String toString() {
		return String.format("%s - %s - %s - valid from %s to %s", super.toString(), cardNumber, nameOnCard,
				validFrom.format(MONTH_YEAR), expiryDate.format(MONTH_YEAR));
	}
}
