/*
 * Copyright 2017-2018 the original author or authors.
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
package org.salespointframework.inventory;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.catalog.Product;
import org.salespointframework.core.Currencies;
import org.salespointframework.order.Cart;
import org.salespointframework.order.Order;
import org.salespointframework.order.Order.OrderCancelled;
import org.salespointframework.order.Order.OrderCompleted;
import org.salespointframework.order.OrderCompletionFailure;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;

/**
 * Integration tests for {@link InventoryOrderEventListener}.
 * 
 * @author Oliver Gierke
 */
@ContextConfiguration(classes = InventoryOrderEventListenerTests.Config.class)
class InventoryOrderEventListenerTests extends AbstractIntegrationTests {

	@Autowired InventoryOrderEventListener listener;

	@Autowired UserAccountManager userAccounts;
	@Autowired Catalog<Product> products;
	@Autowired Inventory<InventoryItem> inventory;

	Product iPad, iPadToFilter, macBook;

	// tag::custom-line-item-filter[]
	static class Config {

		@Bean
		LineItemFilter filter() {
			return item -> !item.getProductName().startsWith("to filter:");
		}
	}
	// end::custom-line-item-filter[]

	@BeforeEach
	void setUp() {

		this.iPad = products.save(new Product("iPad", Money.of(499, Currencies.EURO)));
		this.macBook = products.save(new Product("MacBook", Money.of(999, Currencies.EURO)));
		this.iPadToFilter = products.save(new Product("to filter:iPad", Money.of(499, Currencies.EURO)));

		inventory.save(new InventoryItem(iPad, Quantity.of(10)));
		inventory.save(new InventoryItem(macBook, Quantity.of(1)));
	}

	@Test // #144
	void triggersExceptionForInsufficientStock() {

		UserAccount user = userAccounts.create("username", "password");

		Cart cart = new Cart();
		cart.addOrUpdateItem(iPad, 1);
		cart.addOrUpdateItem(iPadToFilter, 1);
		cart.addOrUpdateItem(macBook, 2);

		Order order = cart.createOrderFor(user);

		assertThatExceptionOfType(OrderCompletionFailure.class) //
				.isThrownBy(() -> listener.on(OrderCompleted.of(order)));
	}

	@Test // #230
	void restocksForCompletedOrderOnCancellation() {

		UserAccount user = userAccounts.create("username", "password");

		Cart cart = new Cart();
		cart.addOrUpdateItem(iPad, 1);

		Order order = spy(cart.createOrderFor(user));
		when(order.isCompleted()).thenReturn(true);

		listener.on(OrderCancelled.of(order, "No reason!"));

		assertThat(inventory.findByProduct(iPad) //
				.map(InventoryItem::getQuantity) //
		).hasValue(Quantity.of(11));
	}
}
