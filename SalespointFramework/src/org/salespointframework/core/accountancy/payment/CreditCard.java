package org.salespointframework.core.accountancy.payment;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.salespointframework.core.accountancy.payment.PaymentCard;
import org.salespointframework.core.money.Money;
import org.salespointframework.util.Objects;

/**
 * Entity implementation class for Entity: CreditCard
 * 
 */
// @Entity
public class CreditCard extends PaymentCard implements Serializable {

	private static final long serialVersionUID = 1L;

	private Money dailyWithdrawalLimit;
	private Money creditLimit;

	/*
	 * @Deprecated protected CreditCard() { }
	 */
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
	 *            maximum amount of <code>Money</code> which can be withdrawn on
	 *            a day
	 * @param creditLimit
	 *            <code>Money</code> representing the line of credit extended by
	 *            the issuing association to the card owner
	 */
	public CreditCard(String cardName, String cardAssociationName,
			String cardNumber, String nameOnCard, String billingAddress,
			DateTime validFrom, DateTime expiryDate,
			String cardVerificationCode, Money dailyWithdrawalLimit,
			Money creditLimit) {
		super(Objects.requireNonNull(cardName, "cardName"), Objects
				.requireNonNull(cardAssociationName, "cardAssociationName"),
				Objects.requireNonNull(cardNumber, "cardNumber"), Objects
						.requireNonNull(nameOnCard, "nameOnCard"), Objects
						.requireNonNull(billingAddress, "billingAddress"),
				Objects.requireNonNull(validFrom, "validFrom"), Objects
						.requireNonNull(expiryDate, "expiryDate"), Objects
						.requireNonNull(cardVerificationCode,
								"cardVerficationCode"));
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
	 *            maximum amount of <code>Money</code> which can be withdrawn on
	 *            a day
	 * @param creditLimit
	 *            <code>Money</code> representing the line of credit extended by
	 *            the issuing association to the card owner
	 */
	public CreditCard(String cardAssociationName, String cardNumber,
			String nameOnCard, String billingAddress, DateTime validFrom,
			DateTime expiryDate, String cardVerificationCode,
			Money dailyWithdrawalLimit, Money creditLimit) {
		super("A credit card", Objects.requireNonNull(cardAssociationName,
				"cardAssociationName"), Objects.requireNonNull(cardNumber,
				"cardNumber"),
				Objects.requireNonNull(nameOnCard, "nameOnCard"), Objects
						.requireNonNull(billingAddress, "billingAddress"),
				Objects.requireNonNull(validFrom, "validFrom"), Objects
						.requireNonNull(expiryDate, "expiryDate"), Objects
						.requireNonNull(cardVerificationCode,
								"cardVerficationCode"));
	}

	/**
	 * The maximum amount of money, the card holder can dispose of within a day.
	 * 
	 * @return <code>Money</code> object representing the the daily withdrawal
	 *         limit
	 */
	public Money getDailyWithdrawalLimit() {
		return dailyWithdrawalLimit;
	}

	/**
	 * Line of credit extended by the issuing association to the card holder.
	 * 
	 * @return <code>Money</code> object representing the credit limit.
	 */
	public Money getCreditLimit() {
		return creditLimit;
	}
}
