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
package org.salespointframework.catalog;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.util.Random;

import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.salespointframework.core.Currencies;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;

@SuppressWarnings("javadoc")
public class ProductTests {

	private Cookie cookie;

	@Before
	public void before() {
		cookie = new Cookie("Schoooki", Currencies.ZERO_EURO);
	}

	@Test(expected = IllegalArgumentException.class)
	public void addNullCategory() {
		cookie.addCategory(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void removeNullCategory() {
		cookie.removeCategory(null);
	}

	@Test
	public void addCategory() {
		assertTrue(cookie.addCategory("Sci-Fi"));
	}

	@Test
	public void addCategory2() {
		cookie.addCategory("Fantasy");
		assertFalse(cookie.addCategory("Fantasy"));
	}

	@Test
	public void removeCategory() {
		cookie.addCategory("Sci-Fi");
		assertTrue(cookie.removeCategory("Sci-Fi"));
	}

	@Test
	public void removeCategory2() {
		assertFalse(cookie.removeCategory(Double.toString(new Random().nextDouble())));
	}
	
	@Test // #198
	public void createsQuantity() {

		Product product = new Product("Some name", Money.of(10, Currencies.EURO), Metric.LITER);

		assertThat(product.createQuantity(10L)).isEqualTo(Quantity.of(10L, Metric.LITER));
		assertThat(product.createQuantity(10.0)).isEqualTo(Quantity.of(10.0, Metric.LITER));
	}
}
