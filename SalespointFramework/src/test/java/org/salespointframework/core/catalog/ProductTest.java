package org.salespointframework.core.catalog;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.salespointframework.core.money.Money;

@SuppressWarnings("javadoc")
public class ProductTest {

	private Keks keks;

	@Before
	public void before() {
		keks = new Keks("Schoooki", Money.ZERO);
	}

	@Test(expected = NullPointerException.class)
	public void addNullCategory() {
		keks.addCategory(null);
	}

	@Test(expected = NullPointerException.class)
	public void removeNullCategory() {
		keks.removeCategory(null);
	}

	@Test
	public void addCategory() {
		assertTrue(keks.addCategory("Sci-Fi"));
	}

	@Test
	public void addCategory2() {
		keks.addCategory("Fantasy");
		assertFalse(keks.addCategory("Fantasy"));
	}

	@Test
	public void removeCategory() {
		keks.addCategory("Sci-Fi");
		assertTrue(keks.removeCategory("Sci-Fi"));
	}

	@Test
	public void removeCategory2() {
		assertFalse(keks.removeCategory(Double.toString(new Random()
				.nextDouble())));
	}

}
