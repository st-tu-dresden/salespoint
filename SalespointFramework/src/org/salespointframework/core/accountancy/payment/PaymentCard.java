package org.salespointframework.core.accountancy.payment;

import java.io.Serializable;
import javax.persistence.*;

import org.joda.time.DateTime;
import org.salespointframework.core.accountancy.payment.PaymentMethod;

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
 */
@Entity
public class PaymentCard extends PaymentMethod implements Serializable {

	private static final long serialVersionUID = 1L;

	private String cardAssociationName;
	private String cardNumber;
	private String nameOnCard;
	private String billingAddress;
	private DateTime validFrom;
	private DateTime expiryDate;
	private String cardVerificationCode;

	public PaymentCard() {
		super();
	}

	/**
	 * Instantiates a <code>PaymentCard</code>.
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
	 */
	public PaymentCard(String cardAssociationName, String cardNumber,
			String nameOnCard, String billingAddress, DateTime validFrom,
			DateTime expiryDate, String cardVerificationCode) {
		super();
		this.cardAssociationName = cardAssociationName;
		this.cardNumber = cardNumber;
		this.nameOnCard = nameOnCard;
		this.billingAddress = billingAddress;
		this.validFrom = validFrom;
		this.expiryDate = expiryDate;
		this.cardVerificationCode = cardVerificationCode;
	}

	/**
	 * @return name of the association which issued the card or
	 *         <code>null</code>
	 */
	public String getCardAssociationName() {
		return cardAssociationName;
	}

	/**
	 * @return card number or <code>null</code>
	 */
	public String getCardNumber() {
		return cardNumber;
	}

	/**
	 * @return name of party to which the card was issued to or
	 *         <code>null</code>
	 */
	public String getNameOnCard() {
		return nameOnCard;
	}

	/**
	 * @return billing address registered with this card or <code>null</code>
	 */
	public String getBillingAddress() {
		return billingAddress;
	}

	/**
	 * @return <code>DateTime</code> representing the date from which the card
	 *         is valid or <code>null</code>
	 */
	public DateTime getValidFrom() {
		return validFrom;
	}

	/**
	 * @return <code>DateTime</code> representing the date on which the card
	 *         expires or <code>null</code>
	 */
	public DateTime getExpiryDate() {
		return expiryDate;
	}

	/**
	 * @return verification code of this card or <code>null</code>
	 */
	public String getCardVerificationCode() {
		return cardVerificationCode;
	}
}
