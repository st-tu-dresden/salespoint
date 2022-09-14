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

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.salespointframework.order.Order;
import org.salespointframework.order.OrderEvents.OrderCanceled;
import org.salespointframework.order.OrderEvents.OrderCompleted;
import org.salespointframework.order.OrderEvents.OrderPaid;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * An {@link ApplicationListener} for {@link OrderCompleted} events to create {@link ProductPaymentEntry} for the
 * {@link Order}.
 *
 * @author Oliver Gierke
 */
@Component
@RequiredArgsConstructor
public class AccountancyOrderEventListener {

	private final @NonNull Accountancy accountancy;

	/**
	 * Creates a new revenue {@link ProductPaymentEntry} for the order that has been paid.
	 *
	 * @param event must not be {@literal null}.
	 */
	@EventListener
	public void on(OrderPaid event) {

		Order order = event.getOrder();

		accountancy.add(ProductPaymentEntry.of(order, String.format("Rechnung Nr. %s", order.getId())));
	}

	/**
	 * Creates a counter {@link ProductPaymentEntry} for the order that is cancelled if there's a revenue entry for the
	 * given order already, i.e. the order has been paid before.
	 *
	 * @param event must not be {@literal null}.
	 * @since 7.1
	 */
	@EventListener
	public void on(OrderCanceled event) {

		Order order = event.getOrder();

		if (accountancy.findAll().stream() //
				.map(ProductPaymentEntry.class::cast) //
				.anyMatch(it -> it.belongsTo(order) && it.isRevenue())) {

			accountancy.add(ProductPaymentEntry.rollback(order,
					String.format("Order %s cancelled! Reason: %s.", order.getId(), event.getReason())));
		}
	}
}
