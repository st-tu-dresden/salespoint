package org.salespointframework.core.accountancy.payment;

import java.util.Objects;

import org.joda.time.DateTime;
import org.salespointframework.core.accountancy.payment.PaymentCard;
import org.salespointframework.core.money.Money;

/**
 * A debit card provides the holder with electronic access to his or her bank
 * account. A payment made with such a card is represented by an instance of the
 * class <code>DebitCard</code>.
 * 
 * @author Hannes Weisbach
 */
@SuppressWarnings("serial")
public final class DebitCard extends PaymentCard {

	/**
	 * Amount of money, the card holder can dispose of within a day.
	 */
	private final Money dailyWithdrawalLimit;

	/**
	 * Instantiates a specific type of debit card.
	 * 
	 * @param cardName
	 *            The name of this card, e.g. Maestro or MasterCard.
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
	 *            {@link Money} representing the line of credit extended by
	 *            the issuing association to the card owner
	 */
	public DebitCard(String cardName, String cardAssociationName,
			String cardNumber, String nameOnCard, String billingAddress,
			DateTime validFrom, DateTime expiryDate,
			String cardVerificationCode, Money dailyWithdrawalLimit) {
		super(Objects.requireNonNull(cardName, "cardName must not be null"), 
				Objects.requireNonNull(cardAssociationName, "cardAssociationName must not be null"),
				Objects.requireNonNull(cardNumber, "cardNumber must not be null"), 
				Objects.requireNonNull(nameOnCard, "nameOnCard must not be null"), 
				Objects.requireNonNull(billingAddress, "billingAddress must not be null"),
				Objects.requireNonNull(validFrom, "validFrom must not be null"), 
				Objects.requireNonNull(expiryDate, "expiryDate must not be null"), 
				Objects.requireNonNull(cardVerificationCode,"cardVerificationCode must not be null"));
		this.dailyWithdrawalLimit = Objects.requireNonNull(dailyWithdrawalLimit, "dailyWithdrawalLimit must not be null");
	}

	/**
	 * Instantiates a generic debit card.
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
	 *            {@link Money} representing the line of credit extended by
	 *            the issuing association to the card owner
	 */
	public DebitCard(String cardAssociationName, String cardNumber,
			String nameOnCard, String billingAddress, DateTime validFrom,
			DateTime expiryDate, String cardVerificationCode,
			Money dailyWithdrawalLimit) {
		this("A debit card", 
				Objects.requireNonNull(cardAssociationName,"cardAssociationName must not be null"), 
				Objects.requireNonNull(cardNumber,"cardNumber must not be null"),
				Objects.requireNonNull(nameOnCard, "nameOnCard must not be null"), 
				Objects.requireNonNull(billingAddress, "billingAddress must not be null"),
				Objects.requireNonNull(validFrom, "validFrom must not be null"), 
				Objects	.requireNonNull(expiryDate, "expiryDate must not be null"), 
				Objects.requireNonNull(cardVerificationCode,"cardVerificationCode must not be null"), 
				Objects.requireNonNull(dailyWithdrawalLimit,"dailyWithdrawalLimit must not be null"));
	}

	/**
	 * Amount of money, the card holder can dispose of within a day.
	 * 
	 * @return {@link Money} object representing the daily withdrawal
	 *         limit.
	 */
	public Money getDailyWithdrawalLimit() {
		return dailyWithdrawalLimit;
	}

}
