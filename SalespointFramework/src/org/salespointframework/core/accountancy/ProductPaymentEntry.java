package org.salespointframework.core.accountancy;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.order.OrderIdentifier;
import org.salespointframework.core.users.UserIdentifier;
import org.salespointframework.util.Objects;

/**
 * A <code>ProductPaymentEntry</code> is an
 * <code>AbstractAccountancyEntry</code> used to store information of payments
 * of orders.
 * 
 * @author Hannes Weisbach
 * @author Thomas Dedek
 */
@Entity
public class ProductPaymentEntry extends PersistentAccountancyEntry
{
	/**
	 * The <code>OrderIdentifier</code> to which this
	 * <code>ProductPaymentEntry</code> refers to.
	 */
	@Embedded
	@AttributeOverride(name = "id", column = @Column(name = "ORDER_ID"))
	private OrderIdentifier orderIdentifier;

	/**
	 * The <code>UserIdentifier</code> to which this
	 * <code>ProductPaymentEntry</code> refers to.
	 */
	@Embedded
	@AttributeOverride(name = "id", column = @Column(name = "USER_ID"))
	private UserIdentifier userIdentifier;

	/**
	 * Parameterless constructor required for JPA. Do not use.
	 */
	@Deprecated
	protected ProductPaymentEntry()
	{
	}

	/**
	 * A <code>ProductPaymentEntry</code> is constructed for a specific
	 * <code>OrderIdentifier</code> attached to it. This Entry saves also the
	 * <code>UserIdentifier</code> and the specified amount that was payed.
	 * 
	 * @param orderIdentifier
	 *            the <code>OrderIdentifier</code> to which this
	 *            <code>ProductPaymentEntry</code> will refer to.
	 * @param userIdentifier
	 *            the <code>UserIdentifier</code> to which this
	 *            <code>ProductPaymentEntry</code> will refer to.
	 * @param amount
	 *            the <code>Money</code> that was payed.
	 */
	public ProductPaymentEntry(OrderIdentifier orderIdentifier, UserIdentifier userIdentifier, Money amount)
	{
		super(amount);
		this.orderIdentifier = Objects.requireNonNull(orderIdentifier, "orderIdentifier");
		this.userIdentifier = Objects.requireNonNull(userIdentifier, "userIdentifier");
	}

	/**
	 * Return the amount of this Payment.
	 * 
	 * @return the amount of this Payment
	 */
	public Money getAmount()
	{
		return getValue();
	}

	/**
	 * Return the <code>UserIdentifier</code> to which this payment refers to.
	 * 
	 * @return the <code>UserIdentifier</code>, to which this payment refers to
	 */
	public UserIdentifier getUserIdentifier()
	{
		return userIdentifier;
	}

	/**
	 * Return the <code>OrderIdentifier</code> to which this payment refers to.
	 * 
	 * @return the <code>OrderIdentifier</code>, to which this payment refers to
	 */
	public OrderIdentifier getOrderIdentifier()
	{
		return orderIdentifier;
	}
}
