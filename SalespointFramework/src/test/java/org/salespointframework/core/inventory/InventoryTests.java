package org.salespointframework.core.inventory;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.core.catalog.Cookie;
import org.salespointframework.core.quantity.Units;
import org.springframework.beans.factory.annotation.Autowired;

public class InventoryTests extends AbstractIntegrationTests {

	@Autowired
	private Inventory inventory;
	private Cookie cookie;
	private InventoryItem item;

	@Before
	public void before() {
		cookie = new Cookie("Add Superkeks", Money.zero(CurrencyUnit.EUR));

		item = new InventoryItem(cookie, Units.TEN);

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
		assertThat(inventory.contains(item.getIdentifier()), is(false));
	}

	@Test
	public void testContains() {
		inventory.add(item);
		assertThat(inventory.contains(item.getIdentifier()), is(true));
	}

	@Test
	public void testGet() {
		inventory.add(item);
		assertThat(inventory.get(InventoryItem.class, item.getIdentifier()), is(item));
	}
}