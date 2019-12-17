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

import static org.hamcrest.Matchers.*;
import static org.hamcrest.junit.MatcherAssert.*;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.moduliths.test.ModuleTest;
import org.moduliths.test.ModuleTest.BootstrapMode;
import org.salespointframework.catalog.Product;
import org.salespointframework.core.Currencies;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.Password.UnencryptedPassword;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for {@link Cart}.
 *
 * @author Paul Henke
 * @author Oliver Gierke
 */
@Transactional
@ModuleTest(mode = BootstrapMode.DIRECT_DEPENDENCIES)
class CartIntegrationTests {

	@Autowired UserAccountManager userAccountManager;

	@Test // #44
	void createsOrderFromCartCorrectly() {

		Cart cart = new Cart();
		CartItem cartItem = cart.addOrUpdateItem(//
				new Product("name", Money.of(1, Currencies.EURO)), Quantity.of(10));

		Order order = cart.createOrderFor(userAccountManager.create("foobar", UnencryptedPassword.of("barfoo")));

		Totalable<OrderLine> orderLines = order.getOrderLines();

		assertThat(orderLines.getTotal(), is(cartItem.getPrice()));
		assertThat(orderLines, is(iterableWithSize(1)));
	}
}
