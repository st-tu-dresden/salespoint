package org.salespointframework.order;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.money.MonetaryAmount;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

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
@Getter
@ToString
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE, onConstructor = @__(@Deprecated) )
public class ProductPaymentEntry extends AccountancyEntry {

	private static final long serialVersionUID = 8273712561197143396L;

	/**
	 * The {@link OrderIdentifier} which this {@link ProductPaymentEntry} refers to.
	 */
	@Embedded //
	@AttributeOverride(name = "id", column = @Column(name = "ORDER_ID", nullable = true) ) //
	private OrderIdentifier orderIdentifier;

	/**
	 * The {@link UserAccount} which this {@link ProductPaymentEntry} refers to.
	 */
	@OneToOne //
	@AttributeOverride(name = "id", column = @Column(name = "USER_ID", nullable = true) ) //
	private UserAccount userAccount;

	/**
	 * The {@link PaymentMethod} chosen for the order belonging to this <code>ProductPaymentEntry</code>
	 */
	private @Lob PaymentMethod paymentMethod;

	/**
	 * A {@code ProductPaymentEntry} is constructed for a specific {@link OrderIdentifier} attached to it. This entry
	 * saves also the {@link UserAccountIdentifier} and the specified amount that was paid.
	 * 
	 * @param orderIdentifier the {@link OrderIdentifier} to which this {@link ProductPaymentEntry} will refer to, must
	 *          not be {@literal null}.
	 * @param userAccount the {@link UserAccount} to which this {@link ProductPaymentEntry} will refer to, must not be
	 *          {@literal null}.
	 * @param amount the {@link MonetaryAmount} that was paid, must not be {@literal null}.
	 * @param description textual description of the payment entry, must not be {@literal null}.
	 * @param paymentMethod must not be {@literal null}.
	 */
	public ProductPaymentEntry(OrderIdentifier orderIdentifier, UserAccount userAccount, MonetaryAmount amount,
			String description, PaymentMethod paymentMethod) {

		super(amount, description);

		Assert.notNull(orderIdentifier, "Order identifier must not be null!");
		Assert.notNull(userAccount, "User account must not be null!");
		Assert.notNull(paymentMethod, "Payment method must not be null!");

		this.orderIdentifier = orderIdentifier;
		this.userAccount = userAccount;
		this.paymentMethod = paymentMethod;
	}
}
