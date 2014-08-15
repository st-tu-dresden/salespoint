package org.salespointframework.core.inventory;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.core.catalog.Cookie;
import org.salespointframework.core.catalog.Product;
import org.salespointframework.core.catalog.Products;
import org.salespointframework.core.quantity.Units;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings({ "javadoc" })
public class InventoryCatalogInteractionTests extends AbstractIntegrationTests {

	@Autowired
	private Inventory inventory;
	@Autowired
	private Products<Product> catalog;
	
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
		inventory.add(item);
	}

	@Test
	public void addInInventoryAddsProductInCatalog() {
		inventory.add(item);
		assertThat(catalog.exists(cookie.getIdentifier()), is(true));
	}

	@Test
	public void removeInInventory() {
		inventory.add(item);
		inventory.remove(item.getIdentifier());
	}

	// TODO negativ-test davon:
	@Test
	public void removeInInventoryThenRemoveInCatalog() {
		catalog.save(cookie);
		inventory.add(item);
		inventory.remove(item.getIdentifier());
		catalog.delete(cookie.getIdentifier());
	}

	@Test
	public void removeInInventoryDoesNotRemoveInCatalog() {
		catalog.save(cookie);
		inventory.add(item);
		inventory.remove(item.getIdentifier());
		assertThat(catalog.exists(cookie.getIdentifier()), is(true));
	}

}