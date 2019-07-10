/*
 * Copyright 2017-2019 the original author or authors.
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

import de.olivergierke.moduliths.test.ModuleTest;
import de.olivergierke.moduliths.test.ModuleTest.BootstrapMode;

import java.time.LocalDateTime;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.core.Currencies;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderIdentifier;
import org.salespointframework.payment.Cash;
import org.salespointframework.time.Interval;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.salespointframework.useraccount.UserAccountTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ModuleTest(BootstrapMode.DIRECT_DEPENDENCIES)
class AccountancyTests {

	@Autowired Accountancy accountancyEntries;
	@Autowired UserAccountManager userAccountManager;

	LocalDateTime from;
	LocalDateTime to;

	@BeforeEach
	void testSetup() throws Exception {

		for (int year = 2000; year < 2010; year++) {

			if (year % 2 == 0) {
				UserAccount user = userAccountManager.create("userId" + year, UserAccountTestUtils.UNENCRYPTED_PASSWORD);
				OrderIdentifier orderIdentifier = new Order(user).getId();

				accountancyEntries.add(new ProductPaymentEntry(orderIdentifier, user, Money.of(1, Currencies.EURO),
						"Rechnung nr " + year, Cash.CASH));
			} else {
				accountancyEntries.add(new AccountancyEntry(Money.of(2.22, Currencies.EURO)));
			}

			if (year == 2002) {
				from = LocalDateTime.now();
			}

			if (year == 2008) {
				to = LocalDateTime.now();
			}
		}
	}

	@Test
	void select() {

		Interval interval = Interval.from(from).to(to);

		System.out.println("Entries from " + from + " to " + to + ":");

		accountancyEntries.find(interval).forEach(System.out::println);
	}

	@Test
	void selectType() {

		System.out.println("All entries:");
		accountancyEntries.find(Interval.from(from).to(to)).forEach(System.out::println);
	}
}
