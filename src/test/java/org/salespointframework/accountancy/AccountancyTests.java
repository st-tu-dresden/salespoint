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

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.core.Currencies;
import org.salespointframework.order.Order;
import org.salespointframework.payment.Cash;
import org.salespointframework.time.Interval;
import org.salespointframework.useraccount.UserAccount.UserAccountIdentifier;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.ApplicationModuleTest.BootstrapMode;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for {@link Accountancy}.
 *
 * @author Paul Henke
 * @author Oliver Drotbohm
 * @author Rebecca Uecker
 */
@Transactional
@ApplicationModuleTest(BootstrapMode.DIRECT_DEPENDENCIES)
class AccountancyTests {

	@Autowired Accountancy accountancy;
	@Autowired UserAccountManagement users;

	LocalDateTime from;
	LocalDateTime to;

	@BeforeEach
	void testSetup() throws Exception {

		for (int year = 2000; year < 2010; year++) {

			if (year % 2 == 0) {

				var userAccountIdentifier = UserAccountIdentifier.of("userId" + year);
				var orderIdentifier = new Order(userAccountIdentifier).getId();

				accountancy.add(new OrderPaymentEntry(orderIdentifier, userAccountIdentifier, Money.of(1, Currencies.EURO),
						"Rechnung nr " + year, Cash.CASH));
			} else {
				accountancy.add(new AccountancyEntry(Money.of(2.22, Currencies.EURO)));
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

		var interval = Interval.from(from).to(to);

		System.out.println("Entries from " + from + " to " + to + ":");

		accountancy.find(interval).forEach(System.out::println);
	}

	@Test
	void selectType() {

		System.out.println("All entries:");
		accountancy.find(Interval.from(from).to(to)).forEach(System.out::println);
	}

	@Test // #412
	void doesNotAddExistingEntry() {

		var entry = accountancy.add(new AccountancyEntry(Money.of(1.00, Currencies.EURO)));

		assertThatIllegalArgumentException()
				.isThrownBy(() -> accountancy.add(entry))
				.as("Adding an exitsing AccountancyEntry should result in IllegalArgumentException!");
	}
}
