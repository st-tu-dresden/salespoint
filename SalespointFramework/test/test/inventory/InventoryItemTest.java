package test.inventory;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.salespointframework.core.inventory.PersistentInventoryItem;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.quantity.Units;

import test.product.Keks;

@SuppressWarnings({ "javadoc", "unused" })
public class InventoryItemTest {
	
	private static int counter = 0;
	
	private Keks keks;
	private PersistentInventoryItem item;
	
	@Before
	public void before() {
		keks = new Keks("Superkeks " + (counter++), Money.ZERO);
		
		item = new PersistentInventoryItem(keks, Units.TEN);
	}
	
	@Test
	public void foobar() {
		item.increaseQuantiy(Units.TEN);
		assertEquals(item.getQuantity(), Units.TEN.add(Units.TEN));
	}
	
}
