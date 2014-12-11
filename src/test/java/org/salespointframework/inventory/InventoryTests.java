package org.salespointframework.inventory;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Optional;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.catalog.Cookie;
import org.salespointframework.catalog.Product;
import org.salespointframework.quantity.Units;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Integration tests for {@link Inventory}.
 *
 * @author Oliver Gierke
 */
public class InventoryTests extends AbstractIntegrationTests {

	@Autowired Inventory<InventoryItem> inventory;
	@Autowired Catalog<Product> catalog;

	Cookie cookie;
	InventoryItem item;

	@Before
	public void before() {

		cookie = catalog.save(new Cookie("Add Superkeks", Money.zero(CurrencyUnit.EUR)));
		item = inventory.save(new InventoryItem(cookie, Units.TEN));
	}

	@Test
	public void savesItemsCorrectly() {
		assertThat(inventory.save(item).getIdentifier(), is(notNullValue()));
	}

	/**
	 * @see #34
	 */
	@Test
	public void deletesItemsCorrectly() {

		inventory.delete(item.getIdentifier());

		assertThat(inventory.exists(item.getIdentifier()), is(false));
	}

	/**
	 * @see #34
	 */
	@Test
	public void testExists() {
		assertThat(inventory.exists(item.getIdentifier()), is(true));
	}

	/**
	 * @see #34
	 */
	@Test
	public void testGet() {

		Optional<InventoryItem> result = inventory.findOne(item.getIdentifier());

		assertThat(result.isPresent(), is(true));
		assertThat(result.get(), is(item));
	}

	/**
	 * @see #34
	 */
	@Test
	public void testFindItemsByProduct() {

		Optional<InventoryItem> result = inventory.findByProduct(cookie);

		assertThat(result.isPresent(), is(true));
		assertThat(result.get(), is(item));
	}

	/**
	 * @see #34
	 */
	@Test
	public void testFindItemsByProductId() {

		Optional<InventoryItem> result = inventory.findByProductIdentifier(cookie.getIdentifier());

		assertThat(result.isPresent(), is(true));
		assertThat(result.get(), is(item));
	}

	/**
	 * @see #34
	 */
	@Test
	public void decreasesItemAndPersistsIt() {

		InventoryItem item = inventory.findByProduct(cookie).get();
		item.decreaseQuantity(Units.ONE);

		// Trigger another finder to flush
		Optional<InventoryItem> result = inventory.findByProductIdentifier(cookie.getIdentifier());

		assertThat(result.isPresent(), is(true));
		assertThat(result.get().getQuantity(), is(Units.of(9)));
	}
}
