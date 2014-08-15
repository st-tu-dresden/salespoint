package org.salespointframework.core.catalog;

import static org.junit.Assert.*;

import java.util.Random;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class ProductTests {

	private Cookie cookie;

	@Before
	public void before() {
		cookie = new Cookie("Schoooki", Money.zero(CurrencyUnit.EUR));
	}

	@Test(expected = NullPointerException.class)
	public void addNullCategory() {
		cookie.addCategory(null);
	}

	@Test(expected = NullPointerException.class)
	public void removeNullCategory() {
		cookie.removeCategory(null);
	}

	@Test
	public void addCategory() {
		assertTrue(cookie.addCategory("Sci-Fi"));
	}

	@Test
	public void addCategory2() {
		cookie.addCategory("Fantasy");
		assertFalse(cookie.addCategory("Fantasy"));
	}

	@Test
	public void removeCategory() {
		cookie.addCategory("Sci-Fi");
		assertTrue(cookie.removeCategory("Sci-Fi"));
	}

	@Test
	public void removeCategory2() {
		assertFalse(cookie.removeCategory(Double.toString(new Random()
				.nextDouble())));
	}

}
