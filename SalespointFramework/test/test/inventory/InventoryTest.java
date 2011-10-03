package test.inventory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.*;

import org.salespointframework.core.database.Database;
import org.salespointframework.core.inventory.PersistentInventory;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.ProductFeature;
import org.salespointframework.util.ArgumentNullException;

import test.product.Keks;
import test.product.KeksType;

@SuppressWarnings({"javadoc", "unused"})
public class InventoryTest {
	
	//private static final EntityManagerFactory emf = Database.INSTANCE.getEntityManagerFactory();
	
	private final PersistentInventory inventory = new PersistentInventory();
	private KeksType keksType;
	private Keks keks;
	
	@BeforeClass
	public static void beforeClass() {
		Database.INSTANCE.initializeEntityManagerFactory("SalespointFramework");
	}
	
	@Before
	public void before() {
		keksType = new KeksType("Add Superkeks", Money.ZERO);
		
		ProductFeature feature1 = ProductFeature.create("Color", "Red");
		ProductFeature feature2 = ProductFeature.create("Color", "Blue");
		keksType.addProductFeature(feature1);
		keksType.addProductFeature(feature2);
		
		keks = new Keks(keksType);
	}
	
	@Test(expected=ArgumentNullException.class)
	public void testNullCheckConstructor() {
		PersistentInventory inventory = new PersistentInventory(null);
	}
	
	@Test(expected=ArgumentNullException.class)
	public void testNullCheckArgument() {
		inventory.add(null);
	}
	
	@Test
	public void testAdd() {
		inventory.add(keks);
	}
	
	@Test
	public void testRemove() {
		inventory.add(keks);
		inventory.remove(keks.getSerialNumber());
		assertFalse(inventory.contains(keks.getSerialNumber()));
	}
	
	@Test
	public void testContains() {
		inventory.add(keks);
		assertTrue(inventory.contains(keks.getSerialNumber()));
	}
	
	@Test
	public void testGet() {
		inventory.add(keks);
		assertEquals(keks, inventory.get(Keks.class, keks.getSerialNumber()));
	}
}