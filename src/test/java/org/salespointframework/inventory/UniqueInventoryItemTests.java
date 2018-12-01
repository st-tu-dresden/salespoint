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
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.junit.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.catalog.Cookie;
import org.salespointframework.core.Currencies;
import org.salespointframework.quantity.Quantity;

/**
 * Unit tests for {@link UniqueInventoryItem}.
 *
 * @author Oliver Gierke
 */
class UniqueInventoryItemTests {

	private static final Quantity TEN = Quantity.of(10);
	private static final Quantity TWENTY = TEN.add(TEN);

	private Cookie cookie;
	private UniqueInventoryItem item;

	@BeforeEach
	void before() {

		cookie = new Cookie("Superkeks", Currencies.ZERO_EURO);
		item = new UniqueInventoryItem(cookie, TEN);
	}

	@Test // #34
	void increasesQuantityCorrectly() {

		item.increaseQuantity(Quantity.of(1));
		assertThat(item.getQuantity(), is(Quantity.of(11)));
	}

	@Test // #34
	void doesNotAllowDecreasingQuantityMoreThanAvailable() {

		assertThatExceptionOfType(IllegalArgumentException.class) //
				.isThrownBy(() -> item.decreaseQuantity(TWENTY));
	}

	@Test // #34
	void decreasesQuantityCorrectly() {

		item.decreaseQuantity(Quantity.of(1));

		assertThat(item.getQuantity(), is(TEN.subtract(Quantity.of(1))));
	}
}
