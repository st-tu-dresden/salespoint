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
package org.salespointframework.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import org.javamoney.moneta.Money;
import org.junit.Test;
import org.salespointframework.catalog.Product;
import org.salespointframework.core.Currencies;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.MetricMismatchException;
import org.salespointframework.quantity.Quantity;

/**
 * Unit tests for {@link CartItem}.
 *
 * @author Paul Henke
 * @author Oliver Gierke
 */
public class CartItemUnitTests {

	static final Quantity QUANTITY = Quantity.of(10);
	static final Product PRODUCT = new Product("name", Money.of(1, Currencies.EURO));

	@Test(expected = IllegalArgumentException.class) // #44
	public void rejectsNullProduct() {
		new CartItem(null, QUANTITY);
	}

	@Test(expected = IllegalArgumentException.class) // #44
	public void rejectsNullQuantity() {
		new CartItem(PRODUCT, null);
	}

	@Test(expected = MetricMismatchException.class) // #44
	public void rejectsQuantityWithInvalidMetric() {
		new CartItem(PRODUCT, Quantity.of(0, Metric.KILOGRAM));
	}

	@Test // #44
	public void returnsCorrectDetails() {

		CartItem item = new CartItem(PRODUCT, QUANTITY);

		assertThat(item.getId(), is(notNullValue()));
		assertThat(item.getProduct(), is(PRODUCT));
		assertThat(item.getQuantity(), is(QUANTITY));
		assertThat(item.getProductName(), is(PRODUCT.getName()));
	}

	@Test // #44
	public void calculatesPriceCorrectly() {

		CartItem item = new CartItem(PRODUCT, QUANTITY);

		assertThat(item.getPrice(), is(PRODUCT.getPrice().multiply(QUANTITY.getAmount())));
	}

	@Test // #44
	public void createsOrderLineCorrectly() {

		OrderLine orderLine = new CartItem(PRODUCT, QUANTITY).toOrderLine();

		assertThat(orderLine, is(notNullValue()));
		assertThat(orderLine.getProductIdentifier(), is(PRODUCT.getId()));
		assertThat(orderLine.getQuantity(), is(QUANTITY));
	}

	@Test // #201
	public void updateingQuantityCreatesNewCartItemWithCorrectPrice() {

		CartItem item = new CartItem(PRODUCT, QUANTITY);
		assertThat(item.getPrice()).isEqualTo(PRODUCT.getPrice().multiply(QUANTITY.getAmount()));

		CartItem newItem = item.add(QUANTITY);
		assertThat(newItem.getPrice()).isEqualTo(PRODUCT.getPrice().multiply(QUANTITY.add(QUANTITY).getAmount()));
	}
}
