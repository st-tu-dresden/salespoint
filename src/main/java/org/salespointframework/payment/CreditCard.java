package org.salespointframework.payment;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.time.LocalDateTime;

import javax.money.MonetaryAmount;

import org.javamoney.moneta.Money;
import org.springframework.util.Assert;

/**
 * A credit card.
 * 
 * @author Hannes Weisbach
 * @author Oliver Gierke
 */
@Value
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CreditCard extends PaymentCard {

	/**
	 * The maximum amount of money, the card holder can dispose of within a day.
	 */
	MonetaryAmount dailyWithdrawalLimit;

	/**
	 * Line of credit extended by the issuing association to the card holder.
	 */
	MonetaryAmount creditLimit;

	/**
	 * Instantiates a specific credit card.
	 * 
	 * @param cardName The name of the card.
	 * @param cardAssociationName the name of the association which issued the card
	 * @param cardNumber the number of this card
	 * @param nameOnCard name of the card owner
	 * @param billingAddress the address to which account statements are sent
	 * @param validFrom date from which the card is valid
	 * @param expiryDate date on which the card expires
	 * @param cardVerificationCode verification code printed on the card or a PIN
	 * @param dailyWithdrawalLimit maximum amount of {@link Money} which can be withdrawn on a day
	 * @param creditLimit {@link Money} representing the line of credit extended by the issuing association to the card
	 *          owner
	 */
	public CreditCard(String cardName, String cardAssociationName, String cardNumber, String nameOnCard,
			String billingAddress, LocalDateTime validFrom, LocalDateTime expiryDate, String cardVerificationCode,
			MonetaryAmount dailyWithdrawalLimit, MonetaryAmount creditLimit) {

		super(cardName, cardAssociationName, cardNumber, nameOnCard, billingAddress, validFrom, expiryDate,
				cardVerificationCode);

		Assert.notNull(dailyWithdrawalLimit, "dailyWithdrawalLimit must not be null");
		Assert.notNull(creditLimit, "creditLimit must not be null");

		this.dailyWithdrawalLimit = dailyWithdrawalLimit;
		this.creditLimit = creditLimit;
	}

	/**
	 * Instantiates a generic credit card.
	 * 
	 * @param cardAssociationName the name of the association which issued the card
	 * @param cardNumber the number of this card
	 * @param nameOnCard name of the card owner
	 * @param billingAddress the address to which account statements are sent
	 * @param validFrom date from which the card is valid
	 * @param expiryDate date on which the card expires
	 * @param cardVerificationCode verification code printed on the card or a PIN
	 * @param dailyWithdrawalLimit maximum amount of {@link Money} which can be withdrawn on a day
	 * @param creditLimit {@link Money} representing the line of credit extended by the issuing association to the card
	 *          owner
	 */
	public CreditCard(String cardAssociationName, String cardNumber, String nameOnCard, String billingAddress,
			LocalDateTime validFrom, LocalDateTime expiryDate, String cardVerificationCode,
			MonetaryAmount dailyWithdrawalLimit, MonetaryAmount creditLimit) {

		super("A credit card", cardAssociationName, cardNumber, nameOnCard, billingAddress, validFrom, expiryDate,
				cardVerificationCode);

		Assert.notNull(dailyWithdrawalLimit, "dailyWithdrawalLimit  must not be null!");
		Assert.notNull(creditLimit, "creditLimit  must not be null!");

		this.dailyWithdrawalLimit = dailyWithdrawalLimit;
		this.creditLimit = creditLimit;
	}
}
