package org.salespointframework.order;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import org.joda.money.Money;
import org.salespointframework.accountancy.AccountancyEntry;
import org.salespointframework.payment.PaymentMethod;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountIdentifier;
import org.springframework.util.Assert;

/**
 * A {@link ProductPaymentEntry} is used to store information of payments of orders.
 * 
 * @author Hannes Weisbach
 * @author Thomas Dedek
 * @author Oliver Gierke
 */
@Entity
public class ProductPaymentEntry extends AccountancyEntry {

	private static final long serialVersionUID = 8273712561197143396L;

	/**
	 * The {@link OrderIdentifier} to which this {@link ProductPaymentEntry} refers to.
	 */
	@Embedded//
	@AttributeOverride(name = "id", column = @Column(name = "ORDER_ID", nullable = true))//
	private OrderIdentifier orderIdentifier;

	/**
	 * The {@link UserAccountIdentifier} to which this {@link ProductPaymentEntry} refers to.
	 */
	@OneToOne//
	@AttributeOverride(name = "id", column = @Column(name = "USER_ID", nullable = true))//
	private UserAccount userAccount;

	private @Lob PaymentMethod paymentMethod;

	/**
	 * Parameterless constructor required for JPA. Do not use.
	 */
	@Deprecated
	protected ProductPaymentEntry() {}

	/**
	 * A <code>ProductPaymentEntry</code> is constructed for a specific {@link OrderIdentifier} attached to it. This Entry
	 * saves also the {@link UserAccountIdentifier} and the specified amount that was payed.
	 * 
	 * @param orderIdentifier the {@link OrderIdentifier} to which this {@link ProductPaymentEntry} will refer to, must
	 *          not be {@literal null}.
	 * @param userAccount the {@link UserAccount} to which this {@link ProductPaymentEntry} will refer to, must not be
	 *          {@literal null}.
	 * @param amount the {@link Money} that was paid, must not be {@literal null}.
	 * @param description textual description of the payment entry, must not be {@literal null}.
	 * @param paymentMethod must not be {@literal null}.
	 */

	public ProductPaymentEntry(OrderIdentifier orderIdentifier, UserAccount userAccount, Money amount,
			String description, PaymentMethod paymentMethod) {

		super(amount, description);

		Assert.notNull(orderIdentifier, "orderIdentifier must not be null");
		Assert.notNull(userAccount, "userAccount must not be null");
		Assert.notNull(paymentMethod, "paymentMethod must not be null");

		this.orderIdentifier = orderIdentifier;
		this.userAccount = userAccount;
		this.paymentMethod = paymentMethod;
	}

	/**
	 * @return the {@link UserAccountIdentifier}, to which this payment refers to
	 */
	public final UserAccount getUserAccount() {
		return userAccount;
	}

	/**
	 * @return the {@link OrderIdentifier}, to which this payment refers to
	 */
	public final OrderIdentifier getOrderIdentifier() {
		return orderIdentifier;
	}

	/**
	 * @return the {@link PaymentMethod} chosen for the order belonging to this <code>ProductPaymentEntry</code>
	 */
	public final PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}
}
