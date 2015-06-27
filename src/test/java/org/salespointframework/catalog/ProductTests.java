package org.salespointframework.catalog;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.salespointframework.core.Currencies;

@SuppressWarnings("javadoc")
public class ProductTests {

	private Cookie cookie;

	@Before
	public void before() {
		cookie = new Cookie("Schoooki", Currencies.ZERO_EURO);
	}

	@Test(expected = IllegalArgumentException.class)
	public void addNullCategory() {
		cookie.addCategory(null);
	}

	@Test(expected = IllegalArgumentException.class)
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
		assertFalse(cookie.removeCategory(Double.toString(new Random().nextDouble())));
	}
}
