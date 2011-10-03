package org.salespointframework.core.accountancy.payment;

import org.joda.time.DateTime;
import org.salespointframework.core.accountancy.payment.PaymentMethod;
import org.salespointframework.util.Objects;

/**
 * A <code>PaymentCard</code> is used to charge the cost of goods or services to
 * an account, belonging to the party identified on the card. A
 * <code>PaymentCard</code> which has a line of credit is a
 * <code>CreditCard</code>. A <code>DebitCard</code> is a
 * <code>PaymentCard</code> where the associated account is debited immediately.
 * An example for a <code>DebitCard</code> is an EC-card or MaestroCard. Other
 * forms of <code>PaymentCard</code>s such as prepaid cards or charge cards are
 * not implemented at the moment.
 * 
 * @author Hannes Weisbach
 * 
 */
@SuppressWarnings("serial")
public abstract class PaymentCard extends PaymentMethod {

	private final String cardAssociationName;

	private final String cardNumber;

	private final String nameOnCard;

	private final String billingAddress;

	private final DateTime validFrom;

	private final DateTime expiryDate;

	private final String cardVerificationCode;

	/**
	 * Instantiates a <code>PaymentCard</code>.
	 * 
	 * @param cardName
	 *            specific name of this card, e.g. VISA, or MasterCard
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
	 */
	public PaymentCard(String cardName, String cardAssociationName,
			String cardNumber, String nameOnCard, String billingAddress,
			DateTime validFrom, DateTime expiryDate, String cardVerificationCode) {
		super(Objects.requireNonNull(cardName, "cardName"));
		this.cardAssociationName = Objects.requireNonNull(cardAssociationName,
				"cardAssociationName");
		this.cardNumber = Objects.requireNonNull(cardNumber, "cardNumber");
		this.nameOnCard = Objects.requireNonNull(nameOnCard, "nameOnCard");
		this.billingAddress = Objects.requireNonNull(billingAddress,
				"billingAddress");
		this.validFrom = Objects.requireNonNull(validFrom, "validFrom");
		this.expiryDate = Objects.requireNonNull(expiryDate, "expiryDate");
		this.cardVerificationCode = Objects.requireNonNull(
				cardVerificationCode, "cardVerificationCode");
	}

	/**
	 * @return String containing the name of the card association.
	 */
	public String getCardAssociationName() {
		return cardAssociationName;
	}

	/**
	 * The number uniquely identifying this payment card.
	 * 
	 * @return String containing the card number.
	 */
	public String getCardNumber() {
		return cardNumber;
	}

	/**
	 * The name of the party to which the card was issued to.
	 * 
	 * @return The name of the card holder.
	 */
	public String getNameOnCard() {
		return nameOnCard;
	}

	/**
	 * The billing address registered with this card.
	 * 
	 * @return String containing the billing address.
	 */
	public String getBillingAddress() {
		return billingAddress;
	}

	/**
	 * The date from which on the card is valid.
	 * 
	 * @return <code>DateTime</code> representing the date from which the card
	 *         is valid.
	 */
	public DateTime getValidFrom() {
		return validFrom;
	}

	/**
	 * The date on which the card expires.
	 * 
	 * @return <code>DateTime</code> representing the date on which the card
	 *         expires.
	 */
	public DateTime getExpiryDate() {
		return expiryDate;
	}

	/**
	 * The verification code or PIN of this card.
	 * 
	 * @return String containing the verification code of this card.
	 */
	public String getCardVerificationCode() {
		return cardVerificationCode;
	}
}
