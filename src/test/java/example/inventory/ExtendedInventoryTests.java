/*
 * Copyright 2017-2022 the original author or authors.
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
package example.inventory;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.junit.MatcherAssert.*;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.EnableSalespoint;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.catalog.Cookie;
import org.salespointframework.catalog.Product;
import org.salespointframework.core.Currencies;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for {@link ExtendedInventory}.
 *
 * @author Oliver Gierke
 */
@Transactional
@SpringBootTest
class ExtendedInventoryTests {

	@EnableSalespoint
	static class TestConfiguration {}

	@Autowired ExtendedUniqueInventory inventory;
	@Autowired Catalog<Product> catalog;

	Cookie cookie;
	Wine wine;
	UniqueInventoryItem cookieItem, wineItem;

	@BeforeEach
	void setUp() {

		cookie = catalog.save(new Cookie("Add Superkeks", Currencies.ZERO_EURO));
		cookieItem = inventory.save(new UniqueInventoryItem(cookie, Quantity.of(10)));

		wine = catalog.save(new Wine("SomeWine", Currencies.ZERO_EURO));
		wineItem = inventory.save(new UniqueInventoryItem(wine, Quantity.of(10, Metric.LITER)));
	}

	@Test // #114
	void findsItemsWithSameMetricAndMatchingAmount() {

		Iterable<UniqueInventoryItem> result = inventory.findByQuantityGreaterThan(Quantity.of(5, Metric.LITER));

		assertThat(result, is(iterableWithSize(1)));
		assertThat(result, hasItem(wineItem));
	}

	@Entity
	static class Wine extends Product {

		Wine(String name, MonetaryAmount price) {
			super(name, price, Metric.LITER);
		}

		Wine() {}
	}
}
