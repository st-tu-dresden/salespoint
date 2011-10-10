package test.inventory;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.*;

import org.salespointframework.core.catalog.PersistentCatalog;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.inventory.PersistentInventory;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.ProductFeature;
import org.salespointframework.util.ArgumentNullException;

import test.product.Keks;
import test.product.KeksType;

@SuppressWarnings({ "javadoc", "unused" })
public class InventoryTest {

	// private static final EntityManagerFactory emf =
	// Database.INSTANCE.getEntityManagerFactory();

	private final PersistentInventory inventory = new PersistentInventory();
	private final PersistentCatalog catalog = new PersistentCatalog();
	private KeksType keksType;
	private Keks keks;
	
	private final ProductFeature featureRed = ProductFeature.create("Color", "Red");
	private final ProductFeature featureBlue = ProductFeature.create("Color", "Blue");
	private final ProductFeature featureYellow = ProductFeature.create("Color", "Yellow");

	@BeforeClass
	public static void beforeClass() {
		Database.INSTANCE.initializeEntityManagerFactory("SalespointFramework");
	}

	@Before
	public void before() {
		keksType = new KeksType("Add Superkeks", Money.ZERO);

		keksType.addProductFeature(featureRed);
		keksType.addProductFeature(featureBlue);

		keks = new Keks(keksType);
		// catalog.add(keksType);
	}

	@Test(expected = ArgumentNullException.class)
	public void testNullCheckConstructor() {
		PersistentInventory inventory = new PersistentInventory(null);
	}

	@Test(expected = ArgumentNullException.class)
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

	@Test
	public void find() {

		keks = new Keks(keksType, featureRed.getIdentifier());

		inventory.add(keks);

		keks = new Keks(keksType, featureBlue.getIdentifier());
		inventory.add(keks);

		/*
		 * this is also a test: because Yellow is not in keksType, it should not
		 * be in Keks. this test is passed.
		 */
		keks = new Keks(keksType, featureYellow.getIdentifier());
		inventory.add(keks);

		System.out.println("Alle Kekse: ");
		for (Keks k : inventory.find(Keks.class)) {
			System.out.println(k);
			for (ProductFeature p : k.getProductFeatures()) {
				System.out.println("\t" + p);
			}
		}
		System.out.println("Fliegen hoch!");

		List<ProductFeature> featureSet = new ArrayList<ProductFeature>();
		/* add automated tests for:
		 * - one feature
		 * - multiple features
		 * - a feature not in productfeatures
		 */
		featureSet.add(featureRed);
		//featureSet.add(featureYellow);

		Iterable<Keks> kekse = inventory.find(Keks.class,
				keks.getProductIdentifier(), featureSet);
		for (Keks k : kekse) {
			System.out.println(k);
			for (ProductFeature p : k.getProductFeatures()) {
				System.out.println("\t" + p);
			}
		}
	}
}