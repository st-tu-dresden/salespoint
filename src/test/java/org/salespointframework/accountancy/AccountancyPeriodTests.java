/*
 * Copyright 2017-2020 the original author or authors.
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

import java.time.Duration;
import java.time.LocalDateTime;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.core.Currencies;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderIdentifier;
import org.salespointframework.payment.Cash;
import org.salespointframework.time.Interval;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
import org.salespointframework.useraccount.UserAccountTestUtils;
import org.springframework.beans.factory.annotation.Autowired;

class AccountancyPeriodTests extends AbstractIntegrationTests {

	@Autowired Accountancy a;
	@Autowired UserAccountManagement users;

	LocalDateTime from;
	LocalDateTime to;

	@BeforeEach
	void testSetup() throws Exception {

		UserAccount account = users.create("username", UserAccountTestUtils.UNENCRYPTED_PASSWORD);
		OrderIdentifier orderIdentifier = new Order(account).getId();

		Money oneEuro = Money.of(1, Currencies.EURO);

		for (int i = 0; i < 20; i++) {

			ProductPaymentEntry p = new ProductPaymentEntry(orderIdentifier, account, oneEuro, "Rechnung nr. 3", Cash.CASH);
			a.add(p);

			if (i == 5) {
				from = p.getDate().get();
			}
			if (i == 15) {
				to = p.getDate().get();
			}
		}
	}

	@Test
	void periodSetTest() {

		var interval = Interval.from(from).to(to);
		var m = a.find(interval, Duration.ofMillis(200));

		for (var e : m.entrySet()) {
			for (var p : e.getValue()) {}
		}
	}

	@Test
	void singlePeriodTest() {

		var interval = Interval.from(from).to(to);
		var m = a.find(interval, interval.getDuration());

		for (var e : m.entrySet()) {
			for (var p : e.getValue()) {}
		}
	}

	@Test
	void periodMoneyTest() {

		var total = Currencies.ZERO_EURO;
		var interval = Interval.from(from).to(to);
		var m = a.find(interval, Duration.ofMillis(200));

		for (var e : m.entrySet()) {

			total = Currencies.ZERO_EURO;

			for (var p : e.getValue()) {
				total = total.add(p.getValue());
			}
		}

		var sales = a.salesVolume(interval, Duration.ofMillis(200));

		for (var e : sales.entrySet()) {
			System.out.println("MonetaryAmount for interval " + e.getKey() + ": " + e.getValue());
		}
	}
}
