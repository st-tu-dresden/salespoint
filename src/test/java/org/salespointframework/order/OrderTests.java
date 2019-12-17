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
import static org.junit.jupiter.api.Assertions.*;
import static org.salespointframework.core.Currencies.*;
import static org.salespointframework.order.OrderStatus.*;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.moduliths.test.ModuleTest;
import org.moduliths.test.ModuleTest.BootstrapMode;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.catalog.Product;
import org.salespointframework.order.ChargeLine.AttachedChargeLine;
import org.salespointframework.payment.Cash;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.salespointframework.useraccount.UserAccountTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Hannes Weissbach
 * @author Paul Henke
 * @author Oliver Gierke
 */
@Transactional
@ModuleTest(mode = BootstrapMode.DIRECT_DEPENDENCIES, //
		extraIncludes = { "org.salespointframework.catalog" })
class OrderTests {

	@Autowired UserAccountManager userAccountManager;
	@Autowired OrderManager<Order> orderManager;
	@Autowired Catalog<Product> catalog;

	UserAccount user;
	Order order;

	private static int foobar = 0;

	@BeforeEach
	void before() {

		user = userAccountManager.create("OrderTests " + foobar, UserAccountTestUtils.UNENCRYPTED_PASSWORD);
		order = new Order(user, Cash.CASH);
		foobar++;
	}

	@Test
	void orderStatusOpentest() {
		assertEquals(OrderStatus.OPEN, order.getOrderStatus());
	}

	@Test
	void cancelsUnpaidOrder() {

		assertThat(orderManager.cancelOrder(order)).isTrue();
		assertThat(order.getOrderStatus()).isEqualTo(CANCELLED);
	}

	@Test
	void cancelIsIdempotent() {

		assertThat(orderManager.cancelOrder(order)).isTrue();
		assertThat(order.getOrderStatus()).isEqualTo(CANCELLED);

		assertThat(orderManager.cancelOrder(order)).isFalse();
		assertThat(order.getOrderStatus()).isEqualTo(CANCELLED);
	}

	@Test
	void allowsCancellingAnAlreadyPaidOrder() {

		orderManager.payOrder(order);

		assertThat(orderManager.cancelOrder(order)).isTrue();
		assertThat(order.getOrderStatus()).isEqualTo(CANCELLED);
	}

	@Test
	void cancelsPaidOrder() {

		orderManager.payOrder(order);
		orderManager.completeOrder(order);

		assertThat(orderManager.cancelOrder(order)).isTrue();
		assertThat(order.getOrderStatus()).isEqualTo(CANCELLED);
	}

	@Test
	void payOrderTest() {

		assertThat(orderManager.payOrder(order)).isTrue();
		assertThat(order.getOrderStatus()).isEqualTo(PAID);
	}

	@Test
	void payingAnOrderIsIdempotent() {

		assertThat(orderManager.payOrder(order)).isTrue();
		assertThat(order.getOrderStatus()).isEqualTo(PAID);

		assertThat(orderManager.payOrder(order)).isFalse();
	}

	@Test
	void completeOrderTest() {

		orderManager.payOrder(order);
		orderManager.completeOrder(order);

		assertEquals(OrderStatus.COMPLETED, order.getOrderStatus());
	}

	@Test
	void doesNotCompleteUnpaidOrder() {

		try {
			orderManager.completeOrder(order);
		} catch (OrderCompletionFailure o_O) {
			assertThat(order.getOrderStatus()).isEqualTo(OPEN);
		}
	}

	@Test // #226
	@SuppressWarnings("deprecation")
	void allowsAttachingAChargeLineToAnOrderLine() {

		Product product = catalog.save(new Product("Some product", Money.of(2700.0, EURO)));
		OrderLine firstOrderLine = order.add(new OrderLine(product, Quantity.of(10)));
		OrderLine secondOrderLine = order.add(new OrderLine(product, Quantity.of(15)));

		orderManager.save(order);

		ChargeLine chargeLine = order.addChargeLine(Money.of(42, EURO), "Some description");
		AttachedChargeLine first = order.addChargeLine(Money.of(15, EURO), "Some service!", 0);
		AttachedChargeLine second = order.addChargeLine(Money.of(15, EURO), "Some other service!", firstOrderLine);
		AttachedChargeLine third = order.addChargeLine(Money.of(41, EURO), "Yet another service!", secondOrderLine);

		// Allows lookup of order bound charge lines and all of them
		assertThat(order.getChargeLines()).containsExactly(chargeLine);
		assertThat(order.getAllChargeLines()).containsExactlyInAnyOrder(chargeLine, first, second, third);

		// Allows lookup of attached charge lines via order line (index)
		assertThat(order.getChargeLines(0)).containsExactly(first, second);
		assertThat(order.getChargeLines(firstOrderLine)).containsExactly(first, second);
		assertThat(order.getChargeLines(secondOrderLine)).containsExactly(third);

		// Exposes total of looked up charge lines
		assertThat(order.getChargeLines(firstOrderLine).getTotal()).isEqualTo(Money.of(30, EURO));

		// Overall total of charge lines includes the
		assertThat(order.getChargeLinesPrice()).isEqualTo(Money.of(113, EURO));

		// Removing attached charge lines works
		order.removeChargeLinesFor(secondOrderLine);
		assertThat(order.getChargeLines(secondOrderLine)).isEmpty();

		// Removing an order line removes all attached charge lines
		order.remove(firstOrderLine);
		assertThat(order.getChargeLines(firstOrderLine)).isEmpty();
	}

	@Test // #226
	void rejectsChargeLineForOrderLineIndexOutOfBounds() {

		assertThatExceptionOfType(IllegalArgumentException.class) //
				.isThrownBy(() -> order.addChargeLine(ZERO_EURO, "Description", 0));
	}
}
