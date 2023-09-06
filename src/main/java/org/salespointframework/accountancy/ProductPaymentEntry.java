/*
 * Copyright 2017-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
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
import jakarta.persistence.ManyToOne;
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
 * A {@link ProductPaymentEntry} is used to store information of payments of orders.
 *
 * @author Hannes Weisbach
 * @author Thomas Dedek
 * @author Oliver Gierke
 */
@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class ProductPaymentEntry extends AccountancyEntry {

	/**
	 * The {@link OrderIdentifier} which this {@link ProductPaymentEntry} refers to.
	 */
	@Embedded //
	@AttributeOverride(name = "id", column = @Column(name = "ORDER_ID", nullable = true)) //
	private OrderIdentifier orderIdentifier;

	/**
	 * The {@link UserAccount} which this {@link ProductPaymentEntry} refers to.
	 */
	@ManyToOne //
	@AttributeOverride(name = "id", column = @Column(name = "USER_ID", nullable = true)) //
	private UserAccount userAccount;

	/**
	 * The {@link PaymentMethod} chosen for the order belonging to this <code>ProductPaymentEntry</code>
	 */
	private @Lob PaymentMethod paymentMethod;

	public static ProductPaymentEntry of(Order order, String description) {
		return new ProductPaymentEntry(order.getId(), order.getUserAccount(), order.getTotal(), description,
				order.getPaymentMethod());
	}

	/**
	 * Creates a new {@link ProductPaymentEntry} that rolls back the payment for the given {@link Order}.
	 *
	 * @param order must not be {@literal null}.
	 * @param description must not be {@literal null}.
	 * @return
	 * @since 7.1
	 */
	public static ProductPaymentEntry rollback(Order order, String description) {

		Assert.notNull(order, "Order must not be null!");
		Assert.notNull(description, "Description must not be null!");

		MonetaryAmount amount = Currencies.ZERO_EURO.subtract(order.getTotal());

		return new ProductPaymentEntry(order.getId(), order.getUserAccount(), amount, description,
				order.getPaymentMethod());
	}

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

	/**
	 * Returns whether the {@link ProductPaymentEntry} belongs to the given {@link Order}.
	 *
	 * @param order must not be {@literal null}.
	 * @return
	 */
	public boolean belongsTo(Order order) {

		Assert.notNull(order, "Order must not be null!");

		return this.orderIdentifier.equals(order.getId());
	}
}
