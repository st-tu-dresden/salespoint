package org.salespointframework.inventory;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Optional;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.catalog.Cookie;
import org.salespointframework.inventory.Inventory;
import org.salespointframework.inventory.InventoryItem;
import org.salespointframework.quantity.Units;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;

public class InventoryTests extends AbstractIntegrationTests {

	@Autowired
	private Inventory<InventoryItem> inventory;
	private Cookie cookie;
	private InventoryItem item;

	@Before
	public void before() {
		cookie = new Cookie("Add Superkeks", Money.zero(CurrencyUnit.EUR));
		item = new InventoryItem(cookie, Units.TEN);
	}

	@Test(expected = InvalidDataAccessApiUsageException.class)
	public void testNullCheckArgument() {
		inventory.save((InventoryItem) null);
	}

	@Test
	public void testAdd() {
		inventory.save(item);
	}

	@Test
	public void testRemove() {
		item = inventory.save(item);
		inventory.delete(item.getIdentifier());
		assertThat(inventory.exists(item.getIdentifier()), is(false));
	}

	@Test
	public void testContains() {
		item = inventory.save(item);
		assertThat(inventory.exists(item.getIdentifier()), is(true));
	}

	@Test
	public void testGet() {
		
		item = inventory.save(item);
		
		Optional<InventoryItem> result = inventory.findOne(item.getIdentifier());
		
		assertThat(result.isPresent(), is(true));
		assertThat(result.get(), is(item));
	}
}