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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.javamoney.moneta.Money;
import org.junit.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.catalog.Product;
import org.salespointframework.core.Currencies;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Integration tests for {@link Cart}.
 * 
 * @author Paul Henke
 * @author Oliver Gierke
 */
public class CartIntegrationTests extends AbstractIntegrationTests {

	@Autowired UserAccountManager userAccountManager;

	/**
	 * @see #44
	 */
	@Test
	public void createsOrderFromCartCorrectly() {

		Cart cart = new Cart();
		CartItem cartItem = cart.addOrUpdateItem(//
				new Product("name", Money.of(1, Currencies.EURO)), Quantity.of(10));

		Order order = new Order(userAccountManager.create("foobar", "barfoo"));
		cart.addItemsTo(order);

		assertThat(order.getOrderedLinesPrice(), is(cartItem.getPrice()));
		assertThat(order.getOrderLines(), is(iterableWithSize(1)));
	}
}
