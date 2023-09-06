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

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.salespointframework.catalog.Product;
import org.salespointframework.core.Currencies;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.Password.UnencryptedPassword;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.ApplicationModuleTest.BootstrapMode;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for {@link Cart}.
 *
 * @author Paul Henke
 * @author Oliver Gierke
 */
@Transactional
@ApplicationModuleTest(mode = BootstrapMode.DIRECT_DEPENDENCIES)
class CartIntegrationTests {

	@Autowired UserAccountManagement users;

	@Test // #44
	void createsOrderFromCartCorrectly() {

		var cart = new Cart();
		var cartItem = cart.addOrUpdateItem(new Product("name", Money.of(1, Currencies.EURO)), Quantity.of(10));

		var order = cart.createOrderFor(users.create("foobar", UnencryptedPassword.of("barfoo")));

		var orderLines = order.getOrderLines();

		assertThat(cartItem.map(CartItem::getPrice)).hasValue(orderLines.getTotal());
		assertThat(orderLines).hasSize(1);
	}
}
