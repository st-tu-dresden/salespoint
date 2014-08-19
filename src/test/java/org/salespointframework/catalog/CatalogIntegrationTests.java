package org.salespointframework.catalog;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.hamcrest.Matchers;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.catalog.Product;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.quantity.Units;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;

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
		cookie = new Cookie("Schoki", Money.zero(CurrencyUnit.EUR));
	}

	/**
	 * @see #19
	 */
	@Test
	public void findsProductsByCategory() {

		Product product = new Product("MacBook", Money.of(CurrencyUnit.EUR, 2700.0), Units.METRIC);
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

		Cookie kT1 = cookies.findOne(cookie.getIdentifier());
		Product kT2 = catalog.findOne(cookie.getIdentifier());

		assertThat(kT1, is(kT2));
	}

	/**
	 * @see #19
	 */
	@Test(expected = InvalidDataAccessApiUsageException.class)
	public void nullAddTest() {
		catalog.save((Product) null);
	}

	/**
	 * @see #19
	 */
	@Test
	public void addTest() {

		Cookie result = catalog.save(cookie);
		assertThat(catalog.findOne(result.getIdentifier()), is(result));
	}

	/**
	 * @see #19
	 */
	@Test
	public void testRemove() {

		catalog.save(cookie);
		catalog.delete(cookie.getIdentifier());

		assertThat(catalog.exists(cookie.getIdentifier()), is(false));
	}

	/**
	 * @see #19
	 */
	@Test
	public void testContains() {

		Cookie result = catalog.save(cookie);
		assertThat(catalog.exists(result.getIdentifier()), is(true));
	}

	/**
	 * @see #19
	 */
	@Test
	public void getTest() {

		Cookie result = catalog.save(cookie);
		assertThat(cookies.findOne(cookie.getIdentifier()), is(result));
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

		Product product = catalog.findOne(doubleChoc.getIdentifier());
		assertThat(product, is(instanceOf(Cookie.class)));
		assertThat(product, is(doubleChoc));
	}

	private Cookie createCookie() {

		Cookie doubleChoc = new Cookie("DoubleChoc", Money.of(CurrencyUnit.EUR, 1.25d));
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
