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

import jakarta.persistence.EntityManager;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.catalog.Cookie;
import org.salespointframework.catalog.Product;
import org.salespointframework.core.Currencies;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.payment.Cash;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.time.Interval;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
import org.salespointframework.useraccount.UserAccountTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

/**
 * Integration tests for {@link OrderManagement}.
 *
 * @author Hannes Weissbach
 * @author Paul Henke
 * @author Oliver Gierke
 */
class OrderManagementTests extends AbstractIntegrationTests {

	@Autowired UserAccountManagement users;
	@Autowired OrderManagement<Order> orders;
	@Autowired EntityManager em;

	@Autowired Catalog<Product> catalog;
	@Autowired UniqueInventory<UniqueInventoryItem> inventory;

	UserAccount user;
	Order order;

	@BeforeEach
	void before() {
		user = users.create("userId", UserAccountTestUtils.UNENCRYPTED_PASSWORD);
		users.save(user);
		order = new Order(user, Cash.CASH);
	}

	@Test
	void nullAddtest() {

		assertThatExceptionOfType(IllegalArgumentException.class) //
				.isThrownBy(() -> orders.save(null));
	}

	@Test
	void addTest() {
		orders.save(order);
	}

	@Test
	void testContains() {

		orders.save(order);

		assertThat(orders.contains(order.getId())).isTrue();
	}

	@Test
	void testGet() {

		order = orders.save(order);

		var result = orders.get(order.getId());

		assertThat(result).hasValue(order);
	}

	@Test // #38
	void completesOrderIfAllLineItemsAreAvailableInSufficientQuantity() {

		var cookie = catalog.save(new Cookie("Double choc", Money.of(1.2, Currencies.EURO)));

		inventory.save(new UniqueInventoryItem(cookie, Quantity.of(100)));
		order.addOrderLine(cookie, Quantity.of(10));
		orders.payOrder(order);
		orders.completeOrder(order);
	}

	@Test // #38, #409
	void failsOrderCompletionIfLineItemsAreNotAvailableInSufficientQuantity() {

		var cookie = catalog.save(new Cookie("Double choc", Money.of(1.2, Currencies.EURO)));

		inventory.save(new UniqueInventoryItem(cookie, Quantity.of(1)));
		order.addOrderLine(cookie, Quantity.of(10));
		orders.payOrder(order);

		assertThatExceptionOfType(OrderCompletionFailure.class) //
				.isThrownBy(() -> orders.completeOrder(order));
		assertThat(order.isCompleted()).isFalse();
	}

	@Test // #61
	void findOrdersBetween() {

		order = orders.save(order);
		var dateCreated = order.getDateCreated();

		var interval = Interval.from(dateCreated.minusSeconds(1)).to(dateCreated.plusSeconds(1));
		var result = orders.findBy(interval);

		assertThat(result).containsExactly(order);
	}

	@Test // #61
	void findOrdersBetweenWhenFromToEqual() {

		order = orders.save(order);
		var dateCreated = order.getDateCreated();

		var interval = Interval
				.from(dateCreated.minusSeconds(1))
				.to(dateCreated.plusSeconds(1));

		var result = orders.findBy(interval);

		assertThat(result).containsExactly(order);
	}

	@Test
	void findOrdersBetweenWhenToLowerThenFrom() {

		order = orders.save(order);
		var dateCreated = order.getDateCreated();

		assertThatExceptionOfType(IllegalArgumentException.class) //
				.isThrownBy(() -> orders.findBy(Interval.from(dateCreated).to(dateCreated.minusHours(1L))));
	}

	@Test // #61
	void findOrdersByOrderStatus_OPEN() {

		var openOrder = orders.save(new Order(user, Cash.CASH));

		orders.save(order);
		orders.payOrder(order);

		var openOrders = orders.findBy(OrderStatus.OPEN);

		assertThat(openOrders).containsExactly(openOrder);
	}

	@Test // #219
	void ordersCanBeDeleted() {

		var reference = orders.save(new Order(user, Cash.CASH));
		assertThat(orders.get(reference.getId())).hasValue(reference);

		orders.delete(reference);
		assertThat(orders.get(reference.getId())).isEmpty();
	}

	@Test // #240
	void returnsAPageOfOrder() throws Exception {

		var source = IntStream.range(0, 19).mapToObj(__ -> new Order(user, Cash.CASH)) //
				.peek(orders::save) //
				.collect(Collectors.toUnmodifiableList());

		var pageable = PageRequest.of(0, 10);
		var result = orders.findAll(pageable);

		assertThat(result).containsExactlyInAnyOrderElementsOf(source.subList(0, 10));
		assertThat(orders.findAll(result.nextPageable())).containsExactlyInAnyOrderElementsOf(source.subList(10, 19));
	}

	@Test // #246
	void persistsChargeLines() {

		var order = orders.save(new Order(user, Cash.CASH));
		order.addChargeLine(Money.of(-1.5, Currencies.EURO), "Some discount");

		orders.save(order);
		em.flush();
	}
}
