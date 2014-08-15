package org.salespointframework.inventory;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.catalog.Cookie;
import org.salespointframework.catalog.Product;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.inventory.Inventory;
import org.salespointframework.inventory.InventoryItem;
import org.salespointframework.quantity.Units;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings({ "javadoc" })
public class InventoryCatalogInteractionTests extends AbstractIntegrationTests {

	@Autowired
	private Inventory<InventoryItem> inventory;
	@Autowired
	private Catalog<Product> catalog;
	
	private Cookie cookie;
	private InventoryItem item;

	private static int counter = 0;

	@Before
	public void before() {
		
		cookie = new Cookie("Superkeks " + (counter++), Money.zero(CurrencyUnit.EUR));
		item = new InventoryItem(cookie, Units.TEN);
	}

	@Test
	public void addInCatalogThenInInventoryThrowsNoException() {
		catalog.save(cookie);
		inventory.save(item);
	}

	@Test
	public void addInInventoryAddsProductInCatalog() {
		inventory.save(item);
		assertThat(catalog.exists(cookie.getIdentifier()), is(true));
	}

	@Test
	public void removeInInventory() {
		inventory.save(item);
		inventory.delete(item.getIdentifier());
	}

	// TODO negativ-test davon:
	@Test
	public void removeInInventoryThenRemoveInCatalog() {
		catalog.save(cookie);
		inventory.save(item);
		inventory.delete(item.getIdentifier());
		catalog.delete(cookie.getIdentifier());
	}

	@Test
	public void removeInInventoryDoesNotRemoveInCatalog() {
		catalog.save(cookie);
		inventory.save(item);
		inventory.delete(item.getIdentifier());
		assertThat(catalog.exists(cookie.getIdentifier()), is(true));
	}

}