package org.salespointframework.core.inventory;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.core.catalog.Catalog;
import org.salespointframework.core.catalog.Keks;
import org.salespointframework.core.quantity.Units;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings({ "javadoc" })
public class InventoryCatalogInteractionTests extends AbstractIntegrationTests {

	@Autowired
	private Inventory inventory;
	@Autowired
	private Catalog catalog;
	
	private Keks keks;
	private InventoryItem item;

	private static int counter = 0;

	@Before
	public void before() {
		keks = new Keks("Superkeks " + (counter++), Money.zero(CurrencyUnit.EUR));

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
		assertThat(catalog.contains(keks.getIdentifier()), is(true));
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
		assertThat(catalog.contains(keks.getIdentifier()), is(true));
	}

}