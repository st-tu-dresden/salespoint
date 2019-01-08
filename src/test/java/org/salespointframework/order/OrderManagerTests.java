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
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.junit.MatcherAssert.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import javax.persistence.EntityManager;

import org.hamcrest.collection.IsIterableWithSize;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.catalog.Cookie;
import org.salespointframework.catalog.Product;
import org.salespointframework.core.Currencies;
import org.salespointframework.inventory.Inventory;
import org.salespointframework.inventory.InventoryItem;
import org.salespointframework.payment.Cash;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.time.Interval;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.StreamUtils;

/**
 * Integration tests for {@link OrderManager}.
 *
 * @author Hannes Weissbach
 * @author Paul Henke
 * @author Oliver Gierke
 */
class OrderManagerTests extends AbstractIntegrationTests {

	@Autowired UserAccountManager userAccountManager;
	@Autowired OrderManager<Order> orderManager;
	@Autowired EntityManager em;

	@Autowired Catalog<Product> catalog;
	@Autowired Inventory<InventoryItem> inventory;

	UserAccount user;
	Order order;

	@BeforeEach
	void before() {
		user = userAccountManager.create("userId", "password");
		userAccountManager.save(user);
		order = new Order(user, Cash.CASH);
	}

	@Test
	void nullAddtest() {

		assertThatExceptionOfType(IllegalArgumentException.class) //
				.isThrownBy(() -> orderManager.save(null));
	}

	@Test
	void addTest() {
		orderManager.save(order);
	}

	@Test
	void testContains() {
		orderManager.save(order);
		assertThat(orderManager.contains(order.getId()), is(true));
	}

	@Test
	void testGet() {

		order = orderManager.save(order);

		Optional<Order> result = orderManager.get(order.getId());

		assertThat(result.isPresent(), is(true));
		assertThat(result.get(), is(order));
	}

	@Test // #38
	void completesOrderIfAllLineItemsAreAvailableInSufficientQuantity() {

		Cookie cookie = catalog.save(new Cookie("Double choc", Money.of(1.2, Currencies.EURO)));
		inventory.save(new InventoryItem(cookie, Quantity.of(100)));
		order.addOrderLine(cookie, Quantity.of(10));

		orderManager.payOrder(order);
		orderManager.completeOrder(order);
	}

	@Test // #38
	void failsOrderCompletionIfLineItemsAreNotAvailableInSufficientQuantity() {

		Cookie cookie = catalog.save(new Cookie("Double choc", Money.of(1.2, Currencies.EURO)));
		inventory.save(new InventoryItem(cookie, Quantity.of(1)));
		order.addOrderLine(cookie, Quantity.of(10));

		orderManager.payOrder(order);

		assertThatExceptionOfType(OrderCompletionFailure.class) //
				.isThrownBy(() -> orderManager.completeOrder(order));
	}

	@Test // #61
	void findOrdersBetween() {

		order = orderManager.save(order);
		LocalDateTime dateCreated = order.getDateCreated();

		Iterable<Order> result = orderManager.findBy(Interval.from(dateCreated).to(dateCreated.plusHours(1L)));

		assertThat(result, IsIterableWithSize.<Order> iterableWithSize(1));
		assertThat(result.iterator().next(), is(order));
	}

	@Test // #61
	void findOrdersBetweenWhenFromToEqual() {

		order = orderManager.save(order);
		LocalDateTime dateCreated = order.getDateCreated();

		Iterable<Order> result = orderManager.findBy(Interval.from(dateCreated).to(dateCreated));

		assertThat(result, IsIterableWithSize.<Order> iterableWithSize(1));
		assertThat(result.iterator().next(), is(order));
	}

	@Test
	void findOrdersBetweenWhenToLowerThenFrom() {

		order = orderManager.save(order);
		LocalDateTime dateCreated = order.getDateCreated();

		assertThatExceptionOfType(IllegalArgumentException.class) //
				.isThrownBy(() -> orderManager.findBy(Interval.from(dateCreated).to(dateCreated.minusHours(1L))));
	}

	@Test // #61
	void findOrdersByOrderStatus_OPEN() {

		Order openOrder = new Order(user, Cash.CASH);
		openOrder = orderManager.save(openOrder);
		orderManager.save(order);

		orderManager.payOrder(order);

		Iterable<Order> openOrders = orderManager.findBy(OrderStatus.OPEN);

		assertThat(openOrders, IsIterableWithSize.<Order> iterableWithSize(1));
		assertThat(openOrders.iterator().next(), is(openOrder));
	}

	@Test // #219
	void ordersCanBeDeleted() {

		Order reference = orderManager.save(new Order(user, Cash.CASH));
		assertThat(orderManager.get(reference.getId())).hasValue(reference);

		orderManager.delete(reference);
		assertThat(orderManager.get(reference.getId())).isEmpty();
	}

	@Test // #240
	void returnsAPageOfOrder() throws Exception {

		List<Order> orders = IntStream.range(0, 19).mapToObj(__ -> new Order(user, Cash.CASH)) //
				.peek(orderManager::save) //
				.collect(StreamUtils.toUnmodifiableList());

		Pageable pageable = PageRequest.of(0, 10);
		Page<Order> result = orderManager.findAll(pageable);

		assertThat(result).containsExactlyInAnyOrderElementsOf(orders.subList(0, 10));
		assertThat(orderManager.findAll(result.nextPageable())).containsExactlyInAnyOrderElementsOf(orders.subList(10, 19));
	}

	@Test // #246
	void persistsChargeLines() {

		Order order = orderManager.save(new Order(user, Cash.CASH));
		order.addChargeLine(Money.of(-1.5, Currencies.EURO), "Some discount");

		orderManager.save(order);
		em.flush();
	}
}
