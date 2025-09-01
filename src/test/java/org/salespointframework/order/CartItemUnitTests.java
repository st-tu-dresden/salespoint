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
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.MetricMismatchException;
import org.salespointframework.quantity.Quantity;

/**
 * Unit tests for {@link CartItem}.
 *
 * @author Paul Henke
 * @author Oliver Gierke
 */
class CartItemUnitTests {

	static final Quantity QUANTITY = Quantity.of(10);
	static final Product PRODUCT = new Product("name", Money.of(1, Currencies.EURO));

	@Test // #44
	void rejectsNullProduct() {

		assertThatExceptionOfType(IllegalArgumentException.class) //
				.isThrownBy(() -> new CartItem(null, QUANTITY));
	}

	@Test // #44
	void rejectsNullQuantity() {

		assertThatExceptionOfType(IllegalArgumentException.class) //
				.isThrownBy(() -> new CartItem(PRODUCT, null));
	}

	@Test // #44
	void rejectsQuantityWithInvalidMetric() {

		assertThatExceptionOfType(MetricMismatchException.class) //
				.isThrownBy(() -> new CartItem(PRODUCT, Quantity.of(1, Metric.KILOGRAM)));
	}

	@Test // #44
	void returnsCorrectDetails() {

		var item = new CartItem(PRODUCT, QUANTITY);

		assertThat(item.getId()).isNotNull();
		assertThat(item.getProduct()).isEqualTo(PRODUCT);
		assertThat(item.getQuantity()).isEqualTo(QUANTITY);
		assertThat(item.getProductName()).isEqualTo(PRODUCT.getName());
	}

	@Test // #44
	void calculatesPriceCorrectly() {

		var item = new CartItem(PRODUCT, QUANTITY);

		assertThat(item.getPrice()).isEqualTo(PRODUCT.getPrice().multiply(QUANTITY.getAmount()));
	}

	@Test // #44
	void createsOrderLineCorrectly() {

		var orderLine = new CartItem(PRODUCT, QUANTITY).toOrderLine();

		assertThat(orderLine).isNotNull();
		assertThat(orderLine.getProductIdentifier()).isEqualTo(PRODUCT.getId());
		assertThat(orderLine.getQuantity()).isEqualTo(QUANTITY);
	}

	@Test // #201
	void updateingQuantityCreatesNewCartItemWithCorrectPrice() {

		var item = new CartItem(PRODUCT, QUANTITY);
		assertThat(item.getPrice()).isEqualTo(PRODUCT.getPrice().multiply(QUANTITY.getAmount()));

		var newItem = item.add(QUANTITY);
		assertThat(newItem.getPrice()).isEqualTo(PRODUCT.getPrice().multiply(QUANTITY.add(QUANTITY).getAmount()));
	}

	@Test // #365
	void rejectsNonPositiveAmount() {

		assertThatIllegalArgumentException()
				.isThrownBy(() -> new CartItem(PRODUCT, Quantity.NONE));
	}
}
