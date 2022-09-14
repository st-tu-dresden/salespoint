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
package org.salespointframework.inventory;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.junit.MatcherAssert.assertThat;

import java.util.Map;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.hibernate.exception.ConstraintViolationException;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.moduliths.test.ModuleTest;
import org.moduliths.test.PublishedEvents;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.catalog.Cookie;
import org.salespointframework.catalog.Product;
import org.salespointframework.core.Currencies;
import org.salespointframework.inventory.InventoryEvents.QuantityReduced;
import org.salespointframework.inventory.InventoryEvents.StockShort;
import org.salespointframework.quantity.Quantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for {@link UniqueInventory}.
 *
 * @author Oliver Gierke
 */
@Transactional
@ModuleTest(extraIncludes = "org.salespointframework.catalog")
class InventoryTests {

	@Autowired UniqueInventory<UniqueInventoryItem> unique;
	@Autowired MultiInventory<MultiInventoryItem> multiple;
	@Autowired Catalog<Product> catalog;

	@Autowired EntityManager em;
	@Autowired NamedParameterJdbcOperations jdbc;

	Cookie cookie;
	UniqueInventoryItem item;

	@BeforeEach
	void before() {

		cookie = catalog.save(new Cookie("Add Superkeks", Currencies.ZERO_EURO));
		item = unique.save(new UniqueInventoryItem(cookie, Quantity.of(10)));

	}

	@Test
	void savesItemsCorrectly() {
		assertThat(unique.save(item).getId(), is(notNullValue()));
	}

	@Test // #34
	void deletesItemsCorrectly() {

		unique.deleteById(item.getId());

		assertThat(unique.existsById(item.getId()), is(false));
	}

	@Test // #34
	void testExists() {
		assertThat(unique.existsById(item.getId()), is(true));
	}

	@Test // #34
	void testGet() {

		var result = unique.findById(item.getId());

		assertThat(result.isPresent(), is(true));
		assertThat(result.get(), is(item));
	}

	@Test // #34
	void testFindItemsByProduct() {

		var result = unique.findByProduct(cookie);

		assertThat(result).hasValue(item);
	}

	@Test // #34
	void testFindItemsByProductId() {

		var result = unique.findByProductIdentifier(cookie.getId());

		assertThat(result).hasValue(item);
	}

	@Test // #34
	void decreasesItemAndPersistsIt() {

		var item = unique.findByProduct(cookie);

		item.map(it -> it.decreaseQuantity(Quantity.of(1)));

		// Trigger another finder to flush
		var result = unique.findByProductIdentifier(cookie.getId());

		assertThat(result).hasValueSatisfying(it -> assertThat(it.getQuantity()).isEqualTo(Quantity.of(9)));
	}

	/**
	 * @see #68
	 */
	void rejectsNewInventoryItemForExistingProducts() {

		unique.save(new UniqueInventoryItem(cookie, Quantity.of(10)));

		assertThatExceptionOfType(PersistenceException.class) //
				.isThrownBy(() -> em.flush()) //
				.withCauseExactlyInstanceOf(ConstraintViolationException.class);
	}

	@Test // #142
	void findsInventoryItemsOutOfStock() {

		assertThat(unique.findItemsOutOfStock()).isEmpty();

		var result = unique.findByProduct(cookie);

		assertThat(result).hasValueSatisfying(item -> {

			item.decreaseQuantity(Quantity.of(10));
			assertThat(unique.findItemsOutOfStock()).containsExactly(item);
		});
	}

	@Test // #163
	void looksUpMultipleInventoryItemsPerProduct() {

		var otherCookie = catalog.save(new Cookie("Other cookie", Money.of(3, Currencies.EURO)));

		var first = multiple.save(new MultiInventoryItem(otherCookie, Quantity.of(5)));
		var second = multiple.save(new MultiInventoryItem(otherCookie, Quantity.of(3)));

		var items = this.multiple.findByProduct(otherCookie);

		assertThat(items).containsExactlyInAnyOrder(first, second);
		assertThat(items.getTotalQuantity()).isEqualTo(Quantity.of(8));
	}

	@Test // #163
	void rejectsNewUniqueInventoryItemForAlreadyExistingUniqueInventoryItem() {

		var otherCookie = catalog.save(new Cookie("Other cookie", Money.of(3, Currencies.EURO)));

		var first = new UniqueInventoryItem(otherCookie, Quantity.of(5));
		var second = new UniqueInventoryItem(otherCookie, Quantity.of(0));

		assertRejectsSecond(first, it -> unique.save(it), second, it -> unique.save(it));
	}

	@Test // #163
	void rejectsNewUniqueInventoryItemForAlreadyExistingInventoryItem() {

		Cookie otherCookie = catalog.save(new Cookie("Other cookie", Money.of(3, Currencies.EURO)));

		MultiInventoryItem first = new MultiInventoryItem(otherCookie, Quantity.of(5));
		UniqueInventoryItem second = new UniqueInventoryItem(otherCookie, Quantity.of(0));

		assertRejectsSecond(first, it -> multiple.save(it), second, it -> unique.save(it));
	}

	@Test // #163
	void rejectsNewInventoryItemForAlreadyExistingUniqueInventoryItem() {

		Cookie otherCookie = catalog.save(new Cookie("Other cookie", Money.of(3, Currencies.EURO)));

		UniqueInventoryItem first = new UniqueInventoryItem(otherCookie, Quantity.of(0));
		MultiInventoryItem second = new MultiInventoryItem(otherCookie, Quantity.of(5));

		assertRejectsSecond(first, it -> unique.save(it), second, it -> multiple.save(it));
	}

	@ParameterizedTest // #283
	@ValueSource(strings = { "MULTI_INVENTORY_ITEM", "UNIQUE_INVENTORY_ITEM" })
	void createsIndividualTablesForEntities(String tableName) {

		var sql = "SELECT COUNT(*) AS count FROM information_schema.tables WHERE table_name = :tableName";

		assertThat(jdbc.queryForObject(sql, Map.of("tableName", tableName), int.class)).isEqualTo(1);
	}

	@Test // #293
	void allowsConcatenatingInventoryItems() {

		var otherCookie = catalog.save(new Cookie("Other cookie", Money.of(3, Currencies.EURO)));

		MultiInventoryItem otherItem = multiple.save(new MultiInventoryItem(otherCookie, Quantity.of(5)));

		assertThat(unique.streamAll().and(multiple.streamAll())).containsExactly(item, otherItem);
		assertThat(multiple.streamAll().and(unique.streamAll())).containsExactly(otherItem, item);
	}

	@Test // #300
	void properlyLoadsLongBasedQuantity() {

		var otherCookie = catalog.save(new Cookie("Other cookie", Money.of(3, Currencies.EURO)));
		var item = unique.save(new UniqueInventoryItem(otherCookie, Quantity.of(5)));

		em.flush();
		em.clear();

		assertThat(unique.findById(item.getId())).hasValueSatisfying(it -> {
			assertThat(it.getQuantity()).isEqualTo(item.getQuantity());
		});
	}

	@Test // #252
	void emitsQuantityReducedEvent(PublishedEvents events) {

		var otherCookie = catalog.save(new Cookie("Other cookie", Money.of(3, Currencies.EURO)));
		var item = unique.save(new UniqueInventoryItem(otherCookie, Quantity.of(5)));

		unique.save(item.decreaseQuantity(Quantity.of(2)));

		// Expect QuantityReduced but no StockShort because we're not below the threshold
		assertThat(events.ofType(QuantityReduced.class)
				.matchingMapped(QuantityReduced::getItem, item::equals)).hasSize(1);
		assertThat(events.ofType(StockShort.class)).isEmpty();

		unique.save(item.decreaseQuantity(Quantity.of(2)));

		// Expect QuantityReduced *and* StockShort because we went below the threshold
		assertThat(events.ofType(QuantityReduced.class)
				.matchingMapped(QuantityReduced::getItem, item::equals)).hasSize(2);
		assertThat(events.ofType(StockShort.class)
				.matchingMapped(StockShort::getProductId, otherCookie::hasId)).hasSize(1)
						.element(0)
						.satisfies(it -> {
							assertThat(it.getCurrentQuantity()).isEqualTo(Quantity.of(1));
							assertThat(it.getThreshold()).isEqualTo(Quantity.of(3));
						});
	}

	private <S extends InventoryItem<S>, T extends InventoryItem<T>> void assertRejectsSecond(S first,
			Function<S, S> firstSaver, T second, Function<T, T> secondSaver) {

		S saved = firstSaver.apply(first);

		Throwable exception = catchThrowable(() -> secondSaver.apply(second));

		assertThat(exception).isInstanceOfSatisfying(InvalidDataAccessApiUsageException.class, outer -> {
			assertThat(outer.getCause()).isInstanceOfSatisfying(IllegalStateException.class, inner -> {
				assertThat(inner) //
						.hasMessageContaining(saved.getId().toString()) //
						.hasMessageContaining(second.getProduct().getId().toString());
			});
		});
	}
}
