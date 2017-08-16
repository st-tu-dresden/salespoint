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
package org.salespointframework.order;

import org.junit.Before;
import org.junit.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.core.Currencies;
import org.salespointframework.payment.Cash;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Integration tests for {@link ChargeLine}. TODO: Improve test cases (assertions, naming).
 */
public class ChargeLineTests extends AbstractIntegrationTests {

	@Autowired UserAccountManager userAccountManager;

	UserAccount user;
	Order order;
	ChargeLine chargeLine;

	@Before
	public void before() {
		user = userAccountManager.create("userId", "password");
		order = new Order(user, Cash.CASH);
		chargeLine = new ChargeLine(Currencies.ZERO_EURO, "gaaar nix");
	}

	@Test(expected = IllegalArgumentException.class)
	public void nullTest() {
		order.add((ChargeLine) null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void nullTest2() {
		order.remove((ChargeLine) null);
	}

	@Test
	public void addsChargeLineCorrectly() {
		order.add(chargeLine);
	}

	@Test
	public void addTest2() {
		order.add(chargeLine);
		order.add(chargeLine);
	}

	@Test
	public void removeTest() {
		order.add(chargeLine);
		order.remove(chargeLine);
	}

	@Test
	public void removeTest2() {
		order.remove(chargeLine);
	}
}
