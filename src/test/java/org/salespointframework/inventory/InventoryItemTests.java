package org.salespointframework.inventory;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Test;
import org.salespointframework.catalog.Cookie;
import org.salespointframework.quantity.Units;

/**
 * Unit tests for {@link InventoryItem}.
 *
 * @author Oliver Gierke
 */
public class InventoryItemTests {

	private static final Units TWENTY = Units.TEN.add(Units.TEN);

	private Cookie cookie;
	private InventoryItem item;

	@Before
	public void before() {

		cookie = new Cookie("Superkeks", Money.zero(CurrencyUnit.EUR));
		item = new InventoryItem(cookie, Units.TEN);
	}

	/**
	 * @see #34
	 */
	@Test
	public void increasesQuantityCorrectly() {

		item.increaseQuantity(Units.ONE);
		assertThat(item.getQuantity(), is(Units.of(11)));
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

		item.decreaseQuantity(Units.ONE);

		assertThat(item.getQuantity(), is(Units.TEN.subtract(Units.ONE)));
	}
}
