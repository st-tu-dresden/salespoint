package org.salespointframework.core.inventory;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.core.catalog.Catalog;
import org.salespointframework.core.catalog.Keks;
import org.salespointframework.core.inventory.Inventory;
import org.salespointframework.core.inventory.InventoryItem;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.quantity.Units;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings({ "javadoc", "unused" })
public class InventoryTests extends AbstractIntegrationTests {

	@Autowired
	private Inventory inventory;
	@Autowired
	private Catalog catalog;
	private Keks keks;
	private InventoryItem item;

	@Before
	public void before() {
		keks = new Keks("Add Superkeks", Money.ZERO);

		item = new InventoryItem(keks, Units.TEN);

	}

	@Test(expected = NullPointerException.class)
	public void testNullCheckArgument() {
		inventory.add(null);
	}

	@Test
	public void testAdd() {
		inventory.add(item);
	}

	@Test
	public void testRemove() {
		inventory.add(item);
		inventory.remove(item.getIdentifier());
		assertFalse(inventory.contains(item.getIdentifier()));
	}

	@Test
	public void testContains() {
		inventory.add(item);
		assertTrue(inventory.contains(item.getIdentifier()));
	}

	@Test
	public void testGet() {
		inventory.add(item);
		assertEquals(item,
				inventory.get(InventoryItem.class, item.getIdentifier()));
	}
}