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
package org.salespointframework.catalog;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.junit.MatcherAssert.assertThat;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.hamcrest.Matchers;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.moduliths.test.ModuleTest;
import org.salespointframework.core.Currencies;
import org.salespointframework.quantity.Metric;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.util.Streamable;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for {@link Catalog}.
 *
 * @author Oliver Gierke
 */
@Transactional
@ModuleTest
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

		Product product = new Product("MacBook", Money.of(2700.0, Currencies.EURO), Metric.UNIT);
		product.addCategory("Apple");

		catalog.save(product);

		Iterable<Product> result = catalog.findByCategory("Apple");

		assertThat(result, is(iterableWithSize(1)));
		assertThat(result, hasItem(product));
	}

	@Test // #19
	void findsSameInstanceOnGenericRepositories() {

		catalog.save(cookie);

		Optional<Cookie> kT1 = cookies.findById(cookie.getId());
		Optional<Product> kT2 = catalog.findById(cookie.getId());

		assertThat(kT1.isPresent(), is(true));
		assertThat(kT2.isPresent(), is(true));
		assertThat(kT1.get(), is(kT2.get()));
	}

	@Test // #19
	void addTest() {

		Cookie result = catalog.save(cookie);
		Optional<Product> cookie = catalog.findById(result.getId());

		assertThat(cookie.isPresent(), is(true));
		assertThat(cookie.get(), is(result));
	}

	@Test // #19
	void testRemove() {

		catalog.save(cookie);
		catalog.deleteById(cookie.getId());

		assertThat(catalog.existsById(cookie.getId()), is(false));
	}

	@Test // #19
	void testContains() {

		Cookie result = catalog.save(cookie);
		assertThat(catalog.existsById(result.getId()), is(true));
	}

	@Test // #19
	void getTest() {

		Cookie reference = catalog.save(cookie);
		Optional<Cookie> result = cookies.findById(cookie.getId());

		assertThat(result.isPresent(), is(true));
		assertThat(result.get(), is(reference));
	}

	@Test // #19
	void persistsProductSubtypesCorrectly() {

		Cookie doubleChoc = createCookie();

		assertKeksFound(cookies.findAll(), doubleChoc);
		assertKeksFound(cookies.findByCategory("chocolate"), doubleChoc);
	}

	@Test // #19
	void generalRepoInstancesFinds() {

		Cookie doubleChoc = createCookie();
		Optional<Product> product = catalog.findById(doubleChoc.getId());

		assertThat(product.isPresent(), is(true));
		assertThat(product.get(), is(instanceOf(Cookie.class)));
		assertThat(product.get(), is(doubleChoc));
	}

	@Test // #232
	void findsByAllCategories() {

		Cookie first = createCookie();

		Cookie second = createCookie();
		second.addCategory("special");

		Streamable<Product> result = catalog.findByAllCategories("chocolate", "special");

		assertThat(result) //
				.containsExactly(second) //
				.doesNotContain(first);
	}

	@Test // #232
	void findsByAnyCategory() {

		Cookie first = createCookie();
		first.addCategory("standard");

		Cookie second = createCookie();
		second.addCategory("special");

		Streamable<Product> result = catalog.findByAnyCategory("standard", "special");

		assertThat(result) //
				.containsExactlyInAnyOrder(first, second);
	}

	@Test // #304
	@SuppressWarnings("deprecation")
	void rejectsInstanceCreatedViaDefaultConstructor() {

		assertThatExceptionOfType(InvalidDataAccessApiUsageException.class) //
				.isThrownBy(() -> catalog.save(new Product()));
	}

	private Cookie createCookie() {

		Cookie doubleChoc = new Cookie("DoubleChoc", Money.of(1.25d, Currencies.EURO));
		doubleChoc.addCategory("chocolate");
		doubleChoc.property = "Yummy!";

		return cookies.save(doubleChoc);
	}

	private static void assertKeksFound(Iterable<Cookie> result, Cookie cookie) {

		assertThat(result, is(Matchers.<Cookie> iterableWithSize(1)));
		assertThat(result, hasItem(cookie));
		assertThat(result.iterator().next().property, is(cookie.property));
	}
}
