package org.salespointframework.inventory;

import static org.junit.Assert.*;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Test;
import org.salespointframework.catalog.Cookie;
import org.salespointframework.inventory.InventoryItem;
import org.salespointframework.quantity.Units;

@SuppressWarnings({ "javadoc"})
public class InventoryItemTests {

	private static int counter = 0;

	private Cookie cookie;
	private InventoryItem item;

	@Before
	public void before() {
		cookie = new Cookie("Superkeks " + counter++, Money.zero(CurrencyUnit.EUR));

		item = new InventoryItem(cookie, Units.TEN);
	}

	@Test
	public void foobar() {
		item.increaseQuantity(Units.TEN);
		assertEquals(item.getQuantity(), Units.TEN.add(Units.TEN));
	}

}
