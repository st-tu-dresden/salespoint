package org.salespointframework.core.accountancy.payment;

import java.time.LocalDateTime;
import java.util.Objects;

import org.salespointframework.core.money.Money;

/**
 * 
 * @author Hannes Weisbach
 * @author Oliver Gierke
 * 
 */
@SuppressWarnings("serial")
public final class CreditCard extends PaymentCard {

	/**
	 * The maximum amount of money, the card holder can dispose of within a day.
	 */
	private final Money dailyWithdrawalLimit;

	/**
	 * Line of credit extended by the issuing association to the card holder.
	 */
	private final Money creditLimit;

	/**
	 * Instantiates a specific credit card.
	 * 
	 * @param cardName
	 *            The name of the card.
	 * @param cardAssociationName
	 *            the name of the association which issued the card
	 * @param cardNumber
	 *            the number of this card
	 * @param nameOnCard
	 *            name of the card owner
	 * @param billingAddress
	 *            the address to which account statements are sent
	 * @param validFrom
	 *            date from which the card is valid
	 * @param expiryDate
	 *            date on which the card expires
	 * @param cardVerificationCode
	 *            verification code printed on the card or a PIN
	 * @param dailyWithdrawalLimit
	 *            maximum amount of {@link Money} which can be withdrawn on
	 *            a day
	 * @param creditLimit
	 *            {@link Money} representing the line of credit extended by
	 *            the issuing association to the card owner
	 */
	public CreditCard(String cardName, String cardAssociationName,
			String cardNumber, String nameOnCard, String billingAddress,
			LocalDateTime validFrom, LocalDateTime expiryDate,
			String cardVerificationCode, Money dailyWithdrawalLimit,
			Money creditLimit) {
		super(Objects.requireNonNull(cardName, "cardName must not be null"), 
				Objects.requireNonNull(cardAssociationName, "cardAssociationName must not be null"),
				Objects.requireNonNull(cardNumber, "cardNumber must not be null"), 
				Objects.requireNonNull(nameOnCard, "nameOnCard must not be null"), 
				Objects.requireNonNull(billingAddress, "billingAddress must not be null"),
				Objects.requireNonNull(validFrom, "validFrom must not be null"), 
				Objects.requireNonNull(expiryDate, "expiryDate must not be null"), 
				Objects.requireNonNull(cardVerificationCode, "cardVerficationCode must not be null"));

		this.dailyWithdrawalLimit = Objects.requireNonNull(dailyWithdrawalLimit, "dailyWithdrawalLimit must not be null");
		this.creditLimit = Objects.requireNonNull(creditLimit, "creditLimit must not be null");
	}

	/**
	 * Instantiates a generic credit card.
	 * 
	 * @param cardAssociationName
	 *            the name of the association which issued the card
	 * @param cardNumber
	 *            the number of this card
	 * @param nameOnCard
	 *            name of the card owner
	 * @param billingAddress
	 *            the address to which account statements are sent
	 * @param validFrom
	 *            date from which the card is valid
	 * @param expiryDate
	 *            date on which the card expires
	 * @param cardVerificationCode
	 *            verification code printed on the card or a PIN
	 * @param dailyWithdrawalLimit
	 *            maximum amount of {@link Money} which can be withdrawn on
	 *            a day
	 * @param creditLimit
	 *            {@link Money} representing the line of credit extended by
	 *            the issuing association to the card owner
	 */
	public CreditCard(String cardAssociationName, String cardNumber,
			String nameOnCard, String billingAddress, LocalDateTime validFrom,
			LocalDateTime expiryDate, String cardVerificationCode,
			Money dailyWithdrawalLimit, Money creditLimit) {
		super("A credit card", Objects.requireNonNull(cardAssociationName,
				"cardAssociationName must not be null"), Objects.requireNonNull(cardNumber,
				"cardNumber must not be null"),
				Objects.requireNonNull(nameOnCard, "nameOnCard must not be null"), 
				Objects.requireNonNull(billingAddress, "billingAddress must not be null"),
				Objects.requireNonNull(validFrom, "validFrom must not be null"),
				Objects.requireNonNull(expiryDate, "expiryDate must not be null"), 
				Objects.requireNonNull(cardVerificationCode,"cardVerficationCode must not be null"));
		this.dailyWithdrawalLimit = Objects.requireNonNull(dailyWithdrawalLimit, "dailyWithdrawalLimit  must not be null");
		this.creditLimit = Objects.requireNonNull(creditLimit, "creditLimit  must not be null");
	}

	/**
	 * The maximum amount of money, the card holder can dispose of within a day.
	 * 
	 * @return {@link Money} object representing the the daily withdrawal
	 *         limit
	 */
	public Money getDailyWithdrawalLimit() {
		return dailyWithdrawalLimit;
	}

	/**
	 * Line of credit extended by the issuing association to the card holder.
	 * 
	 * @return {@link Money} object representing the credit limit.
	 */
	public Money getCreditLimit() {
		return creditLimit;
	}
}
