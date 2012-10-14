package test.tran.inventory;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.salespointframework.core.catalog.TransientCatalog;
import org.salespointframework.core.inventory.InventoryItem;
import org.salespointframework.core.inventory.InventoryItemIdentifier;
import org.salespointframework.core.inventory.TransientInventory;
import org.salespointframework.core.inventory.TransientInventoryItem;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.order.TransientOrderManager;
import org.salespointframework.core.quantity.Units;

import test.tran.product.TransientKeks;

public class TransientInventoryTest
{
	TransientInventory inventory = new TransientInventory();
	TransientKeksItem keksItem;
	TransientKeks keks;
	
	@Before
	public void before() {
		keks = new TransientKeks("Schoki", new Money(0), Units.of(10).getMetric());
		keksItem = new TransientKeksItem(keks, Units.of(10));
	}
	
	@Test
	public void add() {
		inventory.add(keksItem);
		InventoryItem item = inventory.get(TransientInventoryItem.class, keksItem.getIdentifier());
		assertEquals(keksItem, item);
	}
	
	@Test
	public void contains() {
		inventory.add(keksItem);
		boolean contains = inventory.contains(keksItem.getIdentifier());
		assertEquals(true, contains);
	}
	
	@Test
	public void contains2() {
		boolean contains = inventory.contains(new InventoryItemIdentifier());
		assertEquals(false, contains);
	}

	@Test
	public void getWithInventoryItemIdentifier() {
		inventory.add(keksItem);
		InventoryItem item = inventory.get(TransientInventoryItem.class, keksItem.getIdentifier());
		assertEquals(keksItem, item);
	}
	
	@Test
	public void getWithInventoryItemIdentifier2() {
		inventory.add(keksItem);
		InventoryItem item = inventory.get(TransientInventoryItem.class, new InventoryItemIdentifier());
		assertNull(item);
	}
	
	public void getWithProductIdentifier() {
		inventory.add(keksItem);
		InventoryItem item = inventory.getByProductIdentifier(TransientInventoryItem.class, keks.getIdentifier());
		assertEquals(keksItem, item);
	}
}
