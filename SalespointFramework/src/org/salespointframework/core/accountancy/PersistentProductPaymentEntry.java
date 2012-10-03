package org.salespointframework.core.accountancy;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;

import org.salespointframework.core.accountancy.payment.PaymentMethod;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.order.OrderIdentifier;
import org.salespointframework.core.user.UserIdentifier;
import java.util.Objects;

/**
 * A <code>PersistentProductPaymentEntry</code> is used to store information of payments
 * of orders.
 * 
 * @author Hannes Weisbach
 * @author Thomas Dedek
 */
@Entity
// @Customizer(PersistentAccountancyEntryDescriptorCustomizer.class)
public class PersistentProductPaymentEntry extends PersistentAccountancyEntry {
	/**
	 * The {@link OrderIdentifier} to which this
	 * <code>ProductPaymentEntry</code> refers to.
	 */
	@Embedded
	@AttributeOverride(name = "id", column = @Column(name = "ORDER_ID", nullable = true))
	private OrderIdentifier orderIdentifier;

	/**
	 * The {@link UserIdentifier} to which this
	 * <code>ProductPaymentEntry</code> refers to.
	 */
	@Embedded
	@AttributeOverride(name = "id", column = @Column(name = "USER_ID", nullable = true))
	private UserIdentifier userIdentifier;

	private PaymentMethod paymentMethod;

	/**
	 * Parameterless constructor required for JPA. Do not use.
	 */
	@Deprecated
	protected PersistentProductPaymentEntry() {
	}

	// TODO ergänzen * @param paymentMethod 
	
	/**
	 * A <code>ProductPaymentEntry</code> is constructed for a specific
	 * {@link OrderIdentifier} attached to it. This Entry saves also the
	 * {@link UserIdentifier} and the specified amount that was payed.
	 * 
	 * @param orderIdentifier
	 *            the {@link OrderIdentifier} to which this
	 *            <code>ProductPaymentEntry</code> will refer to.
	 * @param userIdentifier
	 *            the {@link UserIdentifier} to which this
	 *            <code>ProductPaymentEntry</code> will refer to.
	 * @param amount
	 *            the {@link Money} that was payed.
	 * @param description
	 *            textual description of the payment entry
	 * @param paymentMethod 
	 */
	
	public PersistentProductPaymentEntry(OrderIdentifier orderIdentifier,
			UserIdentifier userIdentifier, Money amount, String description,
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
	 * @return the {@link UserIdentifier}, to which this payment refers to
	 */
	public UserIdentifier getUserIdentifier() {
		return userIdentifier;
	}

	/**
	 * @return the {@link OrderIdentifier}, to which this payment refers to
	 */
	public OrderIdentifier getOrderIdentifier() {
		return orderIdentifier;
	}

	/**
	 * 
	 * @return the {@link PaymentMethod} chosen for the order belonging to
	 *         this <code>ProductPaymentEntry</code>
	 */
	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}
}
