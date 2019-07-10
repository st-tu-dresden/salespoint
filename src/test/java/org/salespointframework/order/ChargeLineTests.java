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
package org.salespointframework.order;

import static org.assertj.core.api.Assertions.*;
import static org.salespointframework.useraccount.UserAccountTestUtils.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.core.Currencies;
import org.salespointframework.payment.Cash;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Integration tests for {@link ChargeLine}. TODO: Improve test cases (assertions, naming).
 */
class ChargeLineTests extends AbstractIntegrationTests {

	@Autowired UserAccountManager userAccountManager;

	UserAccount user;
	Order order;
	ChargeLine chargeLine;

	@BeforeEach
	void before() {
		user = userAccountManager.create("userId", UNENCRYPTED_PASSWORD);
		order = new Order(user, Cash.CASH);
		chargeLine = new ChargeLine(Currencies.ZERO_EURO, "gaaar nix");
	}

	@Test
	void nullTest() {

		assertThatExceptionOfType(IllegalArgumentException.class) //
				.isThrownBy(() -> order.add((ChargeLine) null));
	}

	@Test
	void nullTest2() {

		assertThatExceptionOfType(IllegalArgumentException.class) //
				.isThrownBy(() -> order.remove((ChargeLine) null));
	}

	@Test
	void addsChargeLineCorrectly() {
		order.add(chargeLine);
	}

	@Test
	void addTest2() {
		order.add(chargeLine);
		order.add(chargeLine);
	}

	@Test
	void removeTest() {
		order.add(chargeLine);
		order.remove(chargeLine);
	}

	@Test
	void removeTest2() {
		order.remove(chargeLine);
	}
}
