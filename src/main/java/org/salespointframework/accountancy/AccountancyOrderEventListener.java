/*
 * Copyright 2017 the original author or authors.
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
import org.salespointframework.order.Order.OrderCompleted;
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

	@EventListener
	public void on(OrderCompleted event) {

		Order order = event.getOrder();

		accountancy.add(ProductPaymentEntry.of(order, "Rechnung Nr. ".concat(order.getId().toString())));
	}
}
