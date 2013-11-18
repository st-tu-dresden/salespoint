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
public class InventoryCatalogInteractionTest extends AbstractIntegrationTests {

	@Autowired
	private Inventory inventory;
	@Autowired
	private Catalog catalog;
	
	private Keks keks;
	private InventoryItem item;

	private static int counter = 0;

	@Before
	public void before() {
		keks = new Keks("Superkeks " + (counter++), Money.ZERO);

		item = new InventoryItem(keks, Units.TEN);

	}

	@Test
	public void addInCatalogThenInInventoryThrowsNoException() {
		catalog.add(keks);
		inventory.add(item);
	}

	@Test
	public void addInInventoryAddsProductInCatalog() {
		inventory.add(item);
		assertTrue(catalog.contains(keks.getIdentifier()));
	}

	@Test
	public void removeInInventory() {
		inventory.add(item);
		inventory.remove(item.getIdentifier());
	}

	// TODO negativ-test davon:
	@Test
	public void removeInInventoryThenRemoveInCatalog() {
		catalog.add(keks);
		inventory.add(item);
		inventory.remove(item.getIdentifier());
		catalog.remove(keks.getIdentifier());
	}

	@Test
	public void removeInInventoryDoesNotRemoveInCatalog() {
		catalog.add(keks);
		inventory.add(item);
		inventory.remove(item.getIdentifier());
		assertTrue(catalog.contains(keks.getIdentifier()));
	}

}