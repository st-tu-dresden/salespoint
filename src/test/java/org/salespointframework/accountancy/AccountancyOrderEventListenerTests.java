/*
 * Copyright 2018-2022 the original author or authors.
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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.salespointframework.core.Currencies.*;

import javax.money.MonetaryAmount;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderEvents.OrderCanceled;
import org.salespointframework.order.OrderEvents.OrderPaid;
import org.salespointframework.payment.Cash;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
import org.salespointframework.useraccount.UserAccountTestUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Integration tests for {@link AccountancyOrderEventListener}.
 *
 * @author Oliver Gierke
 * @since 7.1
 */
class AccountancyOrderEventListenerTests extends AbstractIntegrationTests {

	@Autowired AccountancyOrderEventListener listener;
	@Autowired UserAccountManagement users;
	@Autowired Accountancy accountancy;

	Order order;

	@BeforeEach
	void init() {

		UserAccount account = users.create("username", UserAccountTestUtils.UNENCRYPTED_PASSWORD);

		this.order = spy(new Order(account, Cash.CASH));

		when(order.getTotal()).thenReturn(Money.of(42, EURO));
	}

	@Test // #230
	void createsPaymentEntryOnOrderPaidEvent() {

		listener.on(OrderPaid.of(order));

		assertThat(accountancy.findAll()).hasSize(1);
	}

	@Test // #230
	void createsRollingBackEntryOnOrderCancellation() {

		listener.on(OrderPaid.of(order));
		listener.on(OrderCanceled.of(order, "Testing"));

		var entries = accountancy.findAll();

		assertThat(entries).hasSize(2);
		assertThat(entries.stream() //
				.map(AccountancyEntry::getValue) //
				.reduce(ZERO_EURO, MonetaryAmount::add)//
		).isEqualTo(ZERO_EURO);
	}
}
