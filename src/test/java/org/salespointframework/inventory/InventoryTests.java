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

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.catalog.Cookie;
import org.salespointframework.catalog.Product;
import org.salespointframework.core.Currencies;
import org.salespointframework.quantity.Quantity;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Integration tests for {@link Inventory}.
 *
 * @author Oliver Gierke
 */
public class InventoryTests extends AbstractIntegrationTests {

	public @Rule ExpectedException exception = ExpectedException.none();

	@Autowired Inventory<InventoryItem> inventory;
	@Autowired Catalog<Product> catalog;
	@Autowired EntityManager em;

	Cookie cookie;
	InventoryItem item;

	@Before
	public void before() {

		cookie = catalog.save(new Cookie("Add Superkeks", Currencies.ZERO_EURO));
		item = inventory.save(new InventoryItem(cookie, Quantity.of(10)));
	}

	@Test
	public void savesItemsCorrectly() {
		assertThat(inventory.save(item).getId(), is(notNullValue()));
	}

	@Test // #34
	public void deletesItemsCorrectly() {

		inventory.deleteById(item.getId());

		assertThat(inventory.existsById(item.getId()), is(false));
	}

	@Test // #34
	public void testExists() {
		assertThat(inventory.existsById(item.getId()), is(true));
	}

	@Test // #34
	public void testGet() {

		Optional<InventoryItem> result = inventory.findById(item.getId());

		assertThat(result.isPresent(), is(true));
		assertThat(result.get(), is(item));
	}

	@Test // #34
	public void testFindItemsByProduct() {

		Optional<InventoryItem> result = inventory.findByProduct(cookie);

		assertThat(result.isPresent(), is(true));
		assertThat(result.get(), is(item));
	}

	@Test // #34
	public void testFindItemsByProductId() {

		Optional<InventoryItem> result = inventory.findByProductIdentifier(cookie.getId());

		assertThat(result.isPresent(), is(true));
		assertThat(result.get(), is(item));
	}

	@Test // #34
	public void decreasesItemAndPersistsIt() {

		InventoryItem item = inventory.findByProduct(cookie).get();
		item.decreaseQuantity(Quantity.of(1));

		// Trigger another finder to flush
		Optional<InventoryItem> result = inventory.findByProductIdentifier(cookie.getId());

		assertThat(result.isPresent(), is(true));
		assertThat(result.get().getQuantity(), is(Quantity.of(9)));
	}

	/**
	 * @see #68
	 */
	public void rejectsNewInventoryItemForExistingProducts() {

		exception.expect(PersistenceException.class);
		exception.expectCause(is(instanceOf(ConstraintViolationException.class)));

		inventory.save(new InventoryItem(cookie, Quantity.of(10)));

		em.flush();
	}

	@Test // #142
	public void findsInventoryItemsOutOfStock() {

		assertThat(inventory.findItemsOutOfStock(), is(emptyIterable()));

		Optional<InventoryItem> result = inventory.findByProduct(cookie);

		assertThat(result.isPresent(), is(true));

		result.ifPresent(item -> {

			item.decreaseQuantity(Quantity.of(10));
			assertThat(inventory.findItemsOutOfStock(), is(iterableWithSize(1)));
		});
	}
}
