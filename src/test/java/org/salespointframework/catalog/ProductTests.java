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
package org.salespointframework.catalog;

import static org.assertj.core.api.Assertions.*;

import java.util.Random;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.core.Currencies;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;

@SuppressWarnings("javadoc")
class ProductTests {

	private Cookie cookie;

	@BeforeEach
	void before() {
		cookie = new Cookie("Schoooki", Currencies.ZERO_EURO);
	}

	@Test
	void addNullCategory() {
		assertThatExceptionOfType(IllegalArgumentException.class) //
				.isThrownBy(() -> cookie.addCategory(null));
	}

	@Test
	void removeNullCategory() {

		assertThatExceptionOfType(IllegalArgumentException.class) //
				.isThrownBy(() -> cookie.removeCategory(null));
	}

	@Test
	void addCategory() {
		assertThat(cookie.addCategory("Sci-Fi")).isTrue();
	}

	@Test
	void addCategory2() {
		cookie.addCategory("Fantasy");
		assertThat(cookie.addCategory("Fantasy")).isFalse();
	}

	@Test
	void removeCategory() {
		cookie.addCategory("Sci-Fi");
		assertThat(cookie.removeCategory("Sci-Fi")).isTrue();
	}

	@Test
	void removeCategory2() {
		assertThat(cookie.removeCategory(Double.toString(new Random().nextDouble()))).isFalse();
	}

	@Test // #198
	void createsQuantity() {

		Product product = new Product("Some name", Money.of(10, Currencies.EURO), Metric.LITER);

		assertThat(product.createQuantity(10L)).isEqualTo(Quantity.of(10L, Metric.LITER));
		assertThat(product.createQuantity(10.0)).isEqualTo(Quantity.of(10.0, Metric.LITER));
	}
}
