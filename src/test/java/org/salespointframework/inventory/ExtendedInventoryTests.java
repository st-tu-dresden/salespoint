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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;

import org.junit.Before;
import org.junit.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.catalog.Cookie;
import org.salespointframework.catalog.Product;
import org.salespointframework.core.Currencies;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Integration tests for {@link ExtendedInventory}.
 * 
 * @author Oliver Gierke
 */
public class ExtendedInventoryTests extends AbstractIntegrationTests {

	@Autowired ExtendedInventory inventory;
	@Autowired Catalog<Product> catalog;

	Cookie cookie;
	Wine wine;
	InventoryItem cookieItem, wineItem;

	@Before
	public void setUp() {

		cookie = catalog.save(new Cookie("Add Superkeks", Currencies.ZERO_EURO));
		cookieItem = inventory.save(new InventoryItem(cookie, Quantity.of(10)));

		wine = catalog.save(new Wine("SomeWine", Currencies.ZERO_EURO));
		wineItem = inventory.save(new InventoryItem(wine, Quantity.of(10, Metric.LITER)));
	}

	@Test // #114
	public void findsItemsWithSameMetricAndMatchingAmount() {

		Iterable<InventoryItem> result = inventory.findByQuantityGreaterThan(Quantity.of(5, Metric.LITER));

		assertThat(result, is(iterableWithSize(1)));
		assertThat(result, hasItem(wineItem));
	}

	@Entity
	static class Wine extends Product {

		public Wine(String name, MonetaryAmount price) {
			super(name, price, Metric.LITER);
		}

		Wine() {}
	}
}
