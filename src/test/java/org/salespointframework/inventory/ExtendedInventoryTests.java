package org.salespointframework.inventory;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import javax.persistence.Entity;

import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.catalog.Cookie;
import org.salespointframework.catalog.Product;
import org.salespointframework.core.Currencies;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Integration tests for {@link ExtendedInventory}.
 * 
 * @author Oliver Gierke
 */
public class ExtendedInventoryTests extends AbstractIntegrationTests {

	@Autowired ExtendedInventory inventory;
	@Autowired Catalog<Product> catalog;

	Cookie cookie;
	Wine wine;
	InventoryItem cookieItem, wineItem;

	@Before
	public void setUp() {

		cookie = catalog.save(new Cookie("Add Superkeks", Currencies.ZERO_EURO));
		cookieItem = inventory.save(new InventoryItem(cookie, Quantity.of(10)));

		wine = catalog.save(new Wine("SomeWine", Currencies.ZERO_EURO));
		wineItem = inventory.save(new InventoryItem(wine, Quantity.of(10, Metric.LITER)));
	}

	/**
	 * @see #114
	 */
	@Test
	public void findsItemsWithSameMetricAndMatchingAmount() {

		Iterable<InventoryItem> result = inventory.findByQuantityGreaterThan(Quantity.of(5, Metric.LITER));

		assertThat(result, is(iterableWithSize(1)));
		assertThat(result, hasItem(wineItem));
	}

	@Entity
	@SuppressWarnings("serial")
	static class Wine extends Product {

		public Wine(String name, Money price) {
			super(name, price, Metric.LITER);
		}

		@SuppressWarnings("deprecation")
		Wine() {}
	}
}
