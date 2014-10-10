package org.salespointframework.inventory;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.salespointframework.catalog.Cookie;
import org.salespointframework.core.Currencies;
import org.salespointframework.quantity.Quantity;

/**
 * Unit tests for {@link InventoryItem}.
 *
 * @author Oliver Gierke
 */
public class InventoryItemTests {

	private static final Quantity TEN = Quantity.of(10);
	private static final Quantity TWENTY = TEN.add(TEN);

	private Cookie cookie;
	private InventoryItem item;

	@Before
	public void before() {

		cookie = new Cookie("Superkeks", Currencies.ZERO_EURO);
		item = new InventoryItem(cookie, TEN);
	}

	/**
	 * @see #34
	 */
	@Test
	public void increasesQuantityCorrectly() {

		item.increaseQuantity(Quantity.of(1));
		assertThat(item.getQuantity(), is(Quantity.of(11)));
	}

	/**
	 * @see #34
	 */
	@Test(expected = IllegalArgumentException.class)
	public void doesNotAllowDecreasingQuantityMoreThanAvailable() {
		item.decreaseQuantity(TWENTY);
	}

	/**
	 * @see #34
	 */
	@Test
	public void decreasesQuantityCorrectly() {

		item.decreaseQuantity(Quantity.of(1));

		assertThat(item.getQuantity(), is(TEN.subtract(Quantity.of(1))));
	}
}
