package test.inventory;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.salespointframework.core.catalog.PersistentCatalog;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.inventory.PersistentInventory;
import org.salespointframework.core.inventory.PersistentInventoryItem;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.quantity.Units;

import test.product.Keks;

@SuppressWarnings({ "javadoc", "unused" })
public class InventoryCatalogInteractionTest {

	//private final EntityManagerFactory emf = Database.INSTANCE.getEntityManagerFactory();

	private final PersistentInventory inventory = new PersistentInventory();
	private final PersistentCatalog catalog = new PersistentCatalog();
	private Keks keks;
	private PersistentInventoryItem item;
	
	
	@BeforeClass
	public static void beforeClass() {
		Database.INSTANCE.initializeEntityManagerFactory("SalespointFramework");
	}

	private static int counter = 0;
	
	@Before
	public void before() {
		keks = new Keks("Superkeks " + (counter++), Money.ZERO);
		
		item = new PersistentInventoryItem(keks, Units.TEN);

	}
	
	@Test
	public void addInCatalogThenInInventoryThrowsNot() {
		catalog.add(keks);
		inventory.add(item);
	}

	@Test
	public void addInInventoryAddsProductInCatalog() {
		inventory.add(item);
		assertTrue(catalog.contains(keks.getIdentifier()));
	}
	
	@Test
	public void removeInInventoryThenRemoveInCatalog() {
		catalog.add(keks);
		inventory.add(item);
		inventory.remove(item.getIdentifier());
		catalog.remove(keks.getIdentifier());
	}


	@Test
	public void removeInInventoryDoesNotRemoveInCatalog() {
		inventory.add(item);
		inventory.remove(item.getIdentifier());
		assertTrue(catalog.contains(keks.getIdentifier()));
	}
	
	@Test
	public void removeInCatalogShouldNotRemoveInInventory() {
		catalog.add(keks);
		inventory.add(item);
		catalog.remove(keks.getIdentifier());
		assertTrue(inventory.contains(item.getIdentifier()));
	}
	
	@Test
	public void removeInCatalogShouldNotRemoveInInventory2() {
		inventory.add(item);
		catalog.remove(keks.getIdentifier());
		assertTrue(inventory.contains(item.getIdentifier()));
	}
	
	@Test
	public void removeInCatalogShouldReallyRemoveInCatalog() {
		catalog.add(keks);
		inventory.add(item);
		catalog.remove(keks.getIdentifier());
		assertFalse(catalog.contains(keks.getIdentifier()));
	}
}