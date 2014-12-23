package org.salespointframework.payment;

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
@SuppressWarnings("serial")
public abstract class PaymentCard extends PaymentMethod {

	private static final DateTimeFormatter MONTH_YEAR = DateTimeFormatter.ofPattern("MM/YY");

	private final String cardAssociationName;
	private final String cardNumber;
	private final String nameOnCard;
	private final String billingAddress;
	private final LocalDateTime validFrom;
	private final LocalDateTime expiryDate;
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
	 * @return {@link DateTime} representing the date from which the card is valid.
	 */
	public LocalDateTime getValidFrom() {
		return validFrom;
	}

	/**
	 * The date on which the card expires.
	 * 
	 * @return {@link DateTime} representing the date on which the card expires.
	 */
	public LocalDateTime getExpiryDate() {
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

	/* 
	 * (non-Javadoc)
	 * @see org.salespointframework.payment.PaymentMethod#toString()
	 */
	@Override
	public String toString() {
		return String.format("%s - %s - %s - valid from %s to %s", super.toString(), cardNumber, nameOnCard,
				validFrom.format(MONTH_YEAR), expiryDate.format(MONTH_YEAR));
	}

	/* 
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}

		if (obj == null || !this.getClass().equals(obj.getClass())) {
			return false;
		}

		PaymentCard that = (PaymentCard) obj;

		return super.equals(obj) && this.billingAddress.equals(that.billingAddress) && //
				this.cardAssociationName.equals(that.cardAssociationName) && //
				this.cardNumber.equals(that.cardNumber) && //
				this.cardVerificationCode.equals(that.cardVerificationCode) && //
				this.expiryDate.equals(that.expiryDate) && //
				this.nameOnCard.equals(that.nameOnCard) && //
				this.validFrom.equals(that.validFrom);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.salespointframework.payment.PaymentMethod#hashCode()
	 */
	@Override
	public int hashCode() {

		int result = super.hashCode();

		result += 31 * billingAddress.hashCode();
		result += 31 * cardAssociationName.hashCode();
		result += 31 * cardNumber.hashCode();
		result += 31 * cardVerificationCode.hashCode();
		result += 31 * expiryDate.hashCode();
		result += 31 * nameOnCard.hashCode();
		result += 31 * validFrom.hashCode();

		return result;
	}
}
