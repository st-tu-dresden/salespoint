package org.salespointframework.payment;

import java.time.LocalDateTime;

import org.joda.money.Money;

/**
 * A debit card provides the holder with electronic access to his or her bank account. A payment made with such a card
 * is represented by an instance of the class <code>DebitCard</code>.
 * 
 * @author Hannes Weisbach
 * @author Oliver Gierke
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
	 * @param cardName The name of this card, e.g. Maestro or MasterCard.
	 * @param cardAssociationName the name of the association which issued the card
	 * @param cardNumber the number of this card
	 * @param nameOnCard name of the card owner
	 * @param billingAddress the address to which account statements are sent
	 * @param validFrom date from which the card is valid
	 * @param expiryDate date on which the card expires
	 * @param cardVerificationCode verification code printed on the card or a PIN
	 * @param dailyWithdrawalLimit {@link Money} representing the line of credit extended by the issuing association to
	 *          the card owner
	 */
	public DebitCard(String cardName, String cardAssociationName, String cardNumber, String nameOnCard,
			String billingAddress, LocalDateTime validFrom, LocalDateTime expiryDate, String cardVerificationCode,
			Money dailyWithdrawalLimit) {

		super(cardName, cardAssociationName, cardNumber, nameOnCard, billingAddress, validFrom, expiryDate,
				cardVerificationCode);

		this.dailyWithdrawalLimit = dailyWithdrawalLimit;
	}

	/**
	 * Instantiates a generic debit card.
	 * 
	 * @param cardAssociationName the name of the association which issued the card
	 * @param cardNumber the number of this card
	 * @param nameOnCard name of the card owner
	 * @param billingAddress the address to which account statements are sent
	 * @param validFrom date from which the card is valid
	 * @param expiryDate date on which the card expires
	 * @param cardVerificationCode verification code printed on the card or a PIN
	 * @param dailyWithdrawalLimit {@link Money} representing the line of credit extended by the issuing association to
	 *          the card owner
	 */
	public DebitCard(String cardAssociationName, String cardNumber, String nameOnCard, String billingAddress,
			LocalDateTime validFrom, LocalDateTime expiryDate, String cardVerificationCode, Money dailyWithdrawalLimit) {

		this("A debit card", cardAssociationName, cardNumber, nameOnCard, billingAddress, validFrom, expiryDate,
				cardVerificationCode, dailyWithdrawalLimit);
	}

	/**
	 * Amount of money, the card holder can dispose of within a day.
	 * 
	 * @return {@link Money} object representing the daily withdrawal limit.
	 */
	public Money getDailyWithdrawalLimit() {
		return dailyWithdrawalLimit;
	}

}
