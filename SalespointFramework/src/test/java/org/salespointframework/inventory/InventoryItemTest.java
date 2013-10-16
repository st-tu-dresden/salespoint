package org.salespointframework.inventory;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.salespointframework.catalog.Keks;
import org.salespointframework.core.inventory.InventoryItem;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.quantity.Units;

@SuppressWarnings({ "javadoc"})
public class InventoryItemTest {

	private static int counter = 0;

	private Keks keks;
	private InventoryItem item;

	@Before
	public void before() {
		keks = new Keks("Superkeks " + (counter++), Money.ZERO);

		item = new InventoryItem(keks, Units.TEN);
	}

	@Test
	public void foobar() {
		item.increaseQuantity(Units.TEN);
		assertEquals(item.getQuantity(), Units.TEN.add(Units.TEN));
	}

}
