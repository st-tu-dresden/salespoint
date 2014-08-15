package org.salespointframework.core.catalog;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.hamcrest.Matchers;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.core.quantity.Units;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;

/**
 * Integration tests for {@link Products}.
 * 
 * @author Oliver Gierke
 */
public class ProductsIntegrationTests extends AbstractIntegrationTests {

	@Autowired Products<Product> products;
	@Autowired Products<Cookie> cookies;
	@Autowired CookieRepository cookieRepository;
	
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
		
		products.save(product);
		
		Iterable<Product> result = products.findByCategory("Apple");
		
		assertThat(result, is(iterableWithSize(1)));
		assertThat(result, hasItem(product));
	}

	/**
	 * @see #19
	 */
	@Test
	public void findsSameInstanceOnGenericRepositories() {
		
		products.save(cookie);
		
		Cookie kT1 = cookies.findOne(cookie.getIdentifier());
		Product kT2 = products.findOne(cookie.getIdentifier());
		
		assertThat(kT1, is(kT2));
	}

	/**
	 * @see #19
	 */
	@Test(expected = InvalidDataAccessApiUsageException.class)
	public void nullAddTest() {
		products.save((Product) null);
	}

	/**
	 * @see #19
	 */
	@Test
	public void addTest() {
		
		Cookie result = products.save(cookie);
		assertThat(products.findOne(result.getIdentifier()), is(result));
	}
	
	/**
	 * @see #19
	 */
	@Test
	public void testRemove() {
		
		products.save(cookie);
		products.delete(cookie.getIdentifier());
		
		assertThat(products.exists(cookie.getIdentifier()), is(false));
	}
	
	/**
	 * @see #19
	 */
	@Test
	public void testContains() {
		
		Cookie result = products.save(cookie);
		assertThat(products.exists(result.getIdentifier()), is(true));
	}
	
	/**
	 * @see #19
	 */
	@Test
	public void getTest() {
		
		Cookie result = products.save(cookie);
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
		
		Product product = products.findOne(doubleChoc.getIdentifier());
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
