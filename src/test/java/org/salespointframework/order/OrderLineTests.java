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
package org.salespointframework.order;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.catalog.Cookie;
import org.salespointframework.catalog.Product;
import org.salespointframework.core.Currencies;
import org.salespointframework.payment.Cash;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccount.UserAccountIdentifier;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Integration tests for {@link OrderLine}. TODO: Improve test cases (assertions, naming).
 */
class OrderLineTests extends AbstractIntegrationTests {

	@Autowired UserAccountManagement users;
	@Autowired Catalog<Product> catalog;

	private static int keksCounter = 0;

	private Order order;
	private OrderLine orderLine;

	@BeforeEach
	void before() {

		Product cookie = new Cookie("OrderLine Cookie " + keksCounter++, Currencies.ZERO_EURO);

		catalog.save(cookie);

		order = new Order(UserAccountIdentifier.of("userId"), Cash.CASH);
		orderLine = new OrderLine(cookie, Quantity.of(10));
	}

	@Test
	void nullTest() {
		assertThatExceptionOfType(IllegalArgumentException.class) //
				.isThrownBy(() -> order.add((OrderLine) null));
	}

	@Test
	void nullTest2() {

		assertThatExceptionOfType(IllegalArgumentException.class) //
				.isThrownBy(() -> order.remove((OrderLine) null));
	}

	@Test
	void addTest() {
		order.add(orderLine);
	}

	@Test
	void addTest2() {
		order.add(orderLine);
		order.add(orderLine);
	}

	@Test
	void removeTest() {
		order.add(orderLine);
		order.remove(orderLine);
	}

	@Test
	void removeTest2() {
		order.remove(orderLine);
	}
}
