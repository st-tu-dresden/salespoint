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
package org.salespointframework.catalog;

import static org.assertj.core.api.Assertions.*;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.core.Currencies;
import org.salespointframework.quantity.Metric;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for {@link Catalog}.
 *
 * @author Oliver Gierke
 */
@Transactional
@ApplicationModuleTest
@RequiredArgsConstructor
class CatalogIntegrationTests {

	private final Catalog<Product> catalog;
	private final Catalog<Cookie> cookies;
	private final CookieCatalog cookieCatalog;

	Cookie cookie;

	@BeforeEach
	void before() {
		cookie = new Cookie("Schoki", Currencies.ZERO_EURO);
	}

	@Test // #19
	void findsProductsByCategory() {

		var product = new Product("MacBook", Money.of(2700.0, Currencies.EURO), Metric.UNIT);
		product.addCategory("Apple");

		catalog.save(product);

		assertThat(catalog.findByCategory("Apple")).containsExactly(product);
	}

	@Test // #19
	void findsSameInstanceOnGenericRepositories() {

		catalog.save(cookie);

		Optional<Cookie> kT1 = cookies.findById(cookie.getId());
		Optional<Product> kT2 = catalog.findById(cookie.getId());

		assertThat(kT1).hasValueSatisfying(first -> {
			assertThat(kT2).hasValueSatisfying(second -> {
				assertThat(first).isSameAs(second);
			});
		});
	}

	@Test // #19
	void addTest() {

		var expected = catalog.save(cookie);
		var cookie = catalog.findById(expected.getId());

		assertThat(cookie).hasValue(expected);
	}

	@Test // #19
	void testRemove() {

		catalog.save(cookie);
		catalog.deleteById(cookie.getId());

		assertThat(catalog.existsById(cookie.getId())).isFalse();
	}

	@Test // #19
	void testContains() {

		var result = catalog.save(cookie);

		assertThat(catalog.existsById(result.getId())).isTrue();
	}

	@Test // #19
	void getTest() {

		var reference = catalog.save(cookie);
		var result = cookies.findById(cookie.getId());

		assertThat(result).hasValue(reference);
	}

	@Test // #19
	void persistsProductSubtypesCorrectly() {

		var doubleChoc = createCookie();

		assertKeksFound(cookies.findAll(), doubleChoc);
		assertKeksFound(cookies.findByCategory("chocolate"), doubleChoc);
	}

	@Test // #19
	void generalRepoInstancesFinds() {

		var doubleChoc = createCookie();
		var product = catalog.findById(doubleChoc.getId());

		assertThat(product).hasValue(doubleChoc);
	}

	@Test // #232
	void findsByAllCategories() {

		var first = createCookie();

		var second = createCookie();
		second.addCategory("special");

		assertThat(catalog.findByAllCategories("chocolate", "special")) //
				.containsExactly(second) //
				.doesNotContain(first);
	}

	@Test // #232
	void findsByAnyCategory() {

		var first = createCookie();
		first.addCategory("standard");

		var second = createCookie();
		second.addCategory("special");

		assertThat(catalog.findByAnyCategory("standard", "special")) //
				.containsExactlyInAnyOrder(first, second);
	}

	@Test // #304
	@SuppressWarnings("deprecation")
	void rejectsInstanceCreatedViaDefaultConstructor() {

		assertThatExceptionOfType(InvalidDataAccessApiUsageException.class) //
				.isThrownBy(() -> catalog.save(new Product()));
	}

	private Cookie createCookie() {

		var doubleChoc = new Cookie("DoubleChoc", Money.of(1.25d, Currencies.EURO));
		doubleChoc.addCategory("chocolate");
		doubleChoc.property = "Yummy!";

		return cookies.save(doubleChoc);
	}

	private static void assertKeksFound(Iterable<Cookie> result, Cookie cookie) {

		assertThat(result).containsExactly(cookie)
				.element(0)
				.extracting(it -> it.property)
				.isEqualTo(cookie.property);
	}
}
