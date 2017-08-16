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
package org.salespointframework.inventory;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.salespointframework.catalog.Cookie;
import org.salespointframework.core.Currencies;
import org.salespointframework.quantity.Quantity;

/**
 * Unit tests for {@link InventoryItem}.
 *
 * @author Oliver Gierke
 */
public class InventoryItemTests {

	private static final Quantity TEN = Quantity.of(10);
	private static final Quantity TWENTY = TEN.add(TEN);

	private Cookie cookie;
	private InventoryItem item;

	@Before
	public void before() {

		cookie = new Cookie("Superkeks", Currencies.ZERO_EURO);
		item = new InventoryItem(cookie, TEN);
	}

	/**
	 * @see #34
	 */
	@Test
	public void increasesQuantityCorrectly() {

		item.increaseQuantity(Quantity.of(1));
		assertThat(item.getQuantity(), is(Quantity.of(11)));
	}

	/**
	 * @see #34
	 */
	@Test(expected = IllegalArgumentException.class)
	public void doesNotAllowDecreasingQuantityMoreThanAvailable() {
		item.decreaseQuantity(TWENTY);
	}

	/**
	 * @see #34
	 */
	@Test
	public void decreasesQuantityCorrectly() {

		item.decreaseQuantity(Quantity.of(1));

		assertThat(item.getQuantity(), is(TEN.subtract(Quantity.of(1))));
	}
}
