package org.salespointframework.core.accountancy.payment;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.salespointframework.core.accountancy.payment.PaymentCard;
import org.salespointframework.core.money.Money;
import org.salespointframework.util.Objects;

/**
 * A debit card provides the holder with electronic access to his or her bank
 * account. A payment made with such a card is represented by an instance of the
 * class <code>DebitCard</code>.
 */
public class DebitCard extends PaymentCard implements Serializable {

	private static final long serialVersionUID = 1L;

	private Money dailyWithdrawalLimit;

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
	 *            <code>Money</code> representing the line of credit extended by
	 *            the issuing association to the card owner
	 */
	public DebitCard(String cardName, String cardAssociationName,
			String cardNumber, String nameOnCard, String billingAddress,
			DateTime validFrom, DateTime expiryDate,
			String cardVerificationCode, Money dailyWithdrawalLimit) {
		super(Objects.requireNonNull(cardName, "cardName"), Objects
				.requireNonNull(cardAssociationName, "cardAssociationName"),
				Objects.requireNonNull(cardNumber, "cardNumber"), Objects
						.requireNonNull(nameOnCard, "nameOnCard"), Objects
						.requireNonNull(billingAddress, "billingAddress"),
				Objects.requireNonNull(validFrom, "validFrom"), Objects
						.requireNonNull(expiryDate, "expiryDate"), Objects
						.requireNonNull(cardVerificationCode,
								"cardVerificationCode"));
		this.dailyWithdrawalLimit = Objects.requireNonNull(
				dailyWithdrawalLimit, "dailyWithdrawalLimit");
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
	 *            <code>Money</code> representing the line of credit extended by
	 *            the issuing association to the card owner
	 */
	public DebitCard(String cardAssociationName, String cardNumber,
			String nameOnCard, String billingAddress, DateTime validFrom,
			DateTime expiryDate, String cardVerificationCode,
			Money dailyWithdrawalLimit) {
		this("A debit card", Objects.requireNonNull(cardAssociationName,
				"cardAssociationName"), Objects.requireNonNull(cardNumber,
				"cardNumber"),
				Objects.requireNonNull(nameOnCard, "nameOnCard"), Objects
						.requireNonNull(billingAddress, "billingAddress"),
				Objects.requireNonNull(validFrom, "validFrom"), Objects
						.requireNonNull(expiryDate, "expiryDate"), Objects
						.requireNonNull(cardVerificationCode,
								"cardVerificationCode"), Objects
						.requireNonNull(dailyWithdrawalLimit,
								"dailyWithdrawalLimit"));
	}

	/**
	 * Amount of money, the card holder can dispose of within a day.
	 * 
	 * @return <code>Money</code> object representing the daily withdrawal
	 *         limit.
	 */
	public Money getDailyWithdrawalLimit() {
		return dailyWithdrawalLimit;
	}

}
