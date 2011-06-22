package org.salespointframework.core.accountancy.payment;

import java.io.Serializable;
import javax.persistence.*;

import org.joda.time.DateTime;
import org.salespointframework.core.accountancy.payment.PaymentCard;
import org.salespointframework.core.money.Money;

/**
 * Entity implementation class for Entity: DebitCard
 * 
 */
@Entity
public class DebitCard extends PaymentCard implements Serializable {

	private static final long serialVersionUID = 1L;

	private Money dailyWithdrawalLimit;

	@Deprecated
	protected DebitCard() {
	}

	/**
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
		super(cardAssociationName, cardNumber, nameOnCard, billingAddress,
				validFrom, expiryDate, cardVerificationCode);
		this.dailyWithdrawalLimit = dailyWithdrawalLimit;
	}

	/**
	 * <code>Money</code> object, representing the maximum amount of money, the
	 * card holder can dispose of within a day or <code>null</code>.
	 * 
	 * @return the dailyWithdrawalLimit
	 */
	public Money getDailyWithdrawalLimit() {
		return dailyWithdrawalLimit;
	}

}
