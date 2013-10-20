package org.salespointframework.core.accountancy;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Lob;

import org.salespointframework.core.accountancy.payment.PaymentMethod;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.order.OrderIdentifier;
import org.salespointframework.core.useraccount.UserAccountIdentifier;

import java.util.Objects;

/**
 * A <code>ProductPaymentEntry</code> is used to store information of payments
 * of orders.
 * 
 * @author Hannes Weisbach
 * @author Thomas Dedek
 * @author Oliver Gierke
 */
@Entity
// @Customizer(PersistentAccountancyEntryDescriptorCustomizer.class)
public class ProductPaymentEntry extends AccountancyEntry {
	/**
	 * The {@link OrderIdentifier} to which this
	 * <code>ProductPaymentEntry</code> refers to.
	 */
	@Embedded
	@AttributeOverride(name = "id", column = @Column(name = "ORDER_ID", nullable = true))
	private OrderIdentifier orderIdentifier;

	/**
	 * The {@link UserAccountIdentifier} to which this
	 * <code>ProductPaymentEntry</code> refers to.
	 */
	@Embedded
	@AttributeOverride(name = "id", column = @Column(name = "USER_ID", nullable = true))
	private UserAccountIdentifier userIdentifier;

	@Lob
	private PaymentMethod paymentMethod;

	/**
	 * Parameterless constructor required for JPA. Do not use.
	 */
	@Deprecated
	protected ProductPaymentEntry() {
	}

	// TODO ergänzen * @param paymentMethod 
	
	/**
	 * A <code>ProductPaymentEntry</code> is constructed for a specific
	 * {@link OrderIdentifier} attached to it. This Entry saves also the
	 * {@link UserAccountIdentifier} and the specified amount that was payed.
	 * 
	 * @param orderIdentifier
	 *            the {@link OrderIdentifier} to which this
	 *            <code>ProductPaymentEntry</code> will refer to.
	 * @param userIdentifier
	 *            the {@link UserAccountIdentifier} to which this
	 *            <code>ProductPaymentEntry</code> will refer to.
	 * @param amount
	 *            the {@link Money} that was payed.
	 * @param description
	 *            textual description of the payment entry
	 * @param paymentMethod 
	 */
	
	public ProductPaymentEntry(OrderIdentifier orderIdentifier,
			UserAccountIdentifier userIdentifier, Money amount, String description,
			PaymentMethod paymentMethod) {
		super(amount, description);
		this.orderIdentifier = Objects.requireNonNull(orderIdentifier,
				"orderIdentifier must not be null");
		this.userIdentifier = Objects.requireNonNull(userIdentifier,
				"userIdentifier must not be null");
		this.paymentMethod = Objects.requireNonNull(paymentMethod,
				"paymentMethod must not be null");
	}

	/**
	 * @return the {@link UserAccountIdentifier}, to which this payment refers to
	 */
	public final UserAccountIdentifier getUserIdentifier() {
		return userIdentifier;
	}

	/**
	 * @return the {@link OrderIdentifier}, to which this payment refers to
	 */
	public final OrderIdentifier getOrderIdentifier() {
		return orderIdentifier;
	}

	/**
	 * 
	 * @return the {@link PaymentMethod} chosen for the order belonging to
	 *         this <code>ProductPaymentEntry</code>
	 */
	public final PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}
}
