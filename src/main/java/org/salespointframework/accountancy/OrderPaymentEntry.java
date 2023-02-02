/*
 * Copyright 2017-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.salespointframework.accountancy;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.money.MonetaryAmount;

import org.salespointframework.core.Currencies;
import org.salespointframework.order.Order;
import org.salespointframework.order.Order.OrderIdentifier;
import org.salespointframework.payment.PaymentMethod;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccount.UserAccountIdentifier;
import org.springframework.util.Assert;

/**
 * A {@link OrderPaymentEntry} is used to store information of payments of orders.
 *
 * @author Hannes Weisbach
 * @author Thomas Dedek
 * @author Oliver Gierke
 */
@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class OrderPaymentEntry extends AccountancyEntry {

	/**
	 * The {@link OrderIdentifier} which this {@link OrderPaymentEntry} refers to.
	 */
	@Embedded //
	@AttributeOverride(name = "id", column = @Column(name = "ORDER_ID", nullable = true)) //
	private OrderIdentifier orderIdentifier;

	/**
	 * The identifier of the {@link UserAccount} which this {@link OrderPaymentEntry} refers to.
	 */
	private UserAccountIdentifier userAccountIdentifier;

	/**
	 * The {@link PaymentMethod} chosen for the order belonging to this {@link OrderPaymentEntry}.
	 */
	private @Lob PaymentMethod paymentMethod;

	/**
	 * Creates a new {@link OrderPaymentEntry} for the given {@link Order} and description.
	 *
	 * @param order must not be {@literal null}.
	 * @param description must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	public static OrderPaymentEntry of(Order order, String description) {

		Assert.notNull(order, "Order must not be null!");
		Assert.notNull(description, "Description must not be null!");

		return new OrderPaymentEntry(order.getId(), order.getUserAccountIdentifier(), order.getTotal(), description,
				order.getPaymentMethod());
	}

	/**
	 * Creates a new {@link OrderPaymentEntry} that rolls back the payment for the given {@link Order}.
	 *
	 * @param order must not be {@literal null}.
	 * @param description must not be {@literal null}.
	 * @return will never be {@literal null}.
	 * @since 7.1
	 */
	public static OrderPaymentEntry rollback(Order order, String description) {

		Assert.notNull(order, "Order must not be null!");
		Assert.notNull(description, "Description must not be null!");

		MonetaryAmount amount = Currencies.ZERO_EURO.subtract(order.getTotal());

		return new OrderPaymentEntry(order.getId(), order.getUserAccountIdentifier(), amount, description,
				order.getPaymentMethod());
	}

	/**
	 * A {@code ProductPaymentEntry} is constructed for a specific {@link OrderIdentifier} attached to it. This entry
	 * saves also the {@link UserAccountIdentifier} and the specified amount that was paid.
	 *
	 * @param orderIdentifier the {@link OrderIdentifier} to which this {@link OrderPaymentEntry} will refer to, must not
	 *          be {@literal null}.
	 * @param userAccountIdentifier the identifier of the {@link UserAccount} this {@link OrderPaymentEntry} will refer
	 *          to, must not be {@literal null}.
	 * @param amount the {@link MonetaryAmount} that was paid, must not be {@literal null}.
	 * @param description textual description of the payment entry, must not be {@literal null}.
	 * @param paymentMethod must not be {@literal null}.
	 */
	public OrderPaymentEntry(OrderIdentifier orderIdentifier, UserAccountIdentifier userAccountIdentifier,
			MonetaryAmount amount, String description, PaymentMethod paymentMethod) {

		super(amount, description);

		Assert.notNull(orderIdentifier, "Order identifier must not be null!");
		Assert.notNull(userAccountIdentifier, "User account identifier must not be null!");
		Assert.notNull(paymentMethod, "Payment method must not be null!");

		this.orderIdentifier = orderIdentifier;
		this.userAccountIdentifier = userAccountIdentifier;
		this.paymentMethod = paymentMethod;
	}

	/**
	 * Returns whether the {@link OrderPaymentEntry} belongs to the given {@link Order}.
	 *
	 * @param order must not be {@literal null}.
	 * @return whether the {@link OrderPaymentEntry} belongs to the given {@link Order}.
	 */
	public boolean belongsTo(Order order) {

		Assert.notNull(order, "Order must not be null!");

		return this.orderIdentifier.equals(order.getId());
	}
}
