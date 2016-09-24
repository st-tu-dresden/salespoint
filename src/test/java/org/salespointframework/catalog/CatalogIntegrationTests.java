package org.salespointframework.catalog;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Optional;

import org.hamcrest.Matchers;
import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.core.Currencies;
import org.salespointframework.quantity.Metric;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Integration tests for {@link Catalog}.
 * 
 * @author Oliver Gierke
 */
public class CatalogIntegrationTests extends AbstractIntegrationTests {

	@Autowired Catalog<Product> catalog;
	@Autowired Catalog<Cookie> cookies;
	@Autowired CookieCatalog cookieCatalog;

	Cookie cookie;

	@Before
	public void before() {
		cookie = new Cookie("Schoki", Currencies.ZERO_EURO);
	}

	/**
	 * @see #19
	 */
	@Test
	public void findsProductsByCategory() {

		Product product = new Product("MacBook", Money.of(2700.0, Currencies.EURO), Metric.UNIT);
		product.addCategory("Apple");

		catalog.save(product);

		Iterable<Product> result = catalog.findByCategory("Apple");

		assertThat(result, is(iterableWithSize(1)));
		assertThat(result, hasItem(product));
	}

	/**
	 * @see #19
	 */
	@Test
	public void findsSameInstanceOnGenericRepositories() {

		catalog.save(cookie);

		Optional<Cookie> kT1 = cookies.findOne(cookie.getId());
		Optional<Product> kT2 = catalog.findOne(cookie.getId());

		assertThat(kT1.isPresent(), is(true));
		assertThat(kT2.isPresent(), is(true));
		assertThat(kT1.get(), is(kT2.get()));
	}

	/**
	 * @see #19
	 */
	@Test
	public void addTest() {

		Cookie result = catalog.save(cookie);
		Optional<Product> cookie = catalog.findOne(result.getId());

		assertThat(cookie.isPresent(), is(true));
		assertThat(cookie.get(), is(result));
	}

	/**
	 * @see #19
	 */
	@Test
	public void testRemove() {

		catalog.save(cookie);
		catalog.delete(cookie.getId());

		assertThat(catalog.exists(cookie.getId()), is(false));
	}

	/**
	 * @see #19
	 */
	@Test
	public void testContains() {

		Cookie result = catalog.save(cookie);
		assertThat(catalog.exists(result.getId()), is(true));
	}

	/**
	 * @see #19
	 */
	@Test
	public void getTest() {

		Cookie reference = catalog.save(cookie);
		Optional<Cookie> result = cookies.findOne(cookie.getId());

		assertThat(result.isPresent(), is(true));
		assertThat(result.get(), is(reference));
	}

	/**
	 * @see #19
	 */
	@Test
	public void persistsProductSubtypesCorrectly() {

		Cookie doubleChoc = createCookie();

		assertKeksFound(cookies.findAll(), doubleChoc);
		assertKeksFound(cookies.findByCategory("chocolate"), doubleChoc);
	}

	/**
	 * @see #19
	 */
	@Test
	public void generalRepoInstancesFinds() {

		Cookie doubleChoc = createCookie();
		Optional<Product> product = catalog.findOne(doubleChoc.getId());

		assertThat(product.isPresent(), is(true));
		assertThat(product.get(), is(instanceOf(Cookie.class)));
		assertThat(product.get(), is(doubleChoc));
	}

	private Cookie createCookie() {

		Cookie doubleChoc = new Cookie("DoubleChoc", Money.of(1.25d, Currencies.EURO));
		doubleChoc.addCategory("chocolate");
		doubleChoc.property = "Yummy!";

		return cookies.save(doubleChoc);
	}

	private static void assertKeksFound(Iterable<Cookie> result, Cookie cookie) {

		assertThat(result, is(Matchers.<Cookie>iterableWithSize(1)));
		assertThat(result, hasItem(cookie));
		assertThat(result.iterator().next().property, is(cookie.property));
	}
}
