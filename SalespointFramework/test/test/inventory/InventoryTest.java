package test.inventory;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.salespointframework.core.catalog.PersistentCatalog;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.inventory.PersistentInventory;
import org.salespointframework.core.money.Money;

import test.product.Keks;
import test.product.KeksInstance;

@SuppressWarnings({ "javadoc", "unused" })
public class InventoryTest {

	// private static final EntityManagerFactory emf =
	// Database.INSTANCE.getEntityManagerFactory();

	private final PersistentInventory inventory = new PersistentInventory();
	private final PersistentCatalog catalog = new PersistentCatalog();
	private Keks keks;
	private KeksInstance keksInstance;
	
	//private final ProductFeature featureRed = ProductFeature.create("Color", "Red");
//	private final ProductFeature featureBlue = ProductFeature.create("Color", "Blue");
//	private final ProductFeature featureYellow = ProductFeature.create("Color", "Yellow");

	@BeforeClass
	public static void beforeClass() {
		Database.INSTANCE.initializeEntityManagerFactory("SalespointFramework");
	}

	@Before
	public void before() {
		keks = new Keks("Add Superkeks", Money.ZERO);

	//	keks.addProductFeature(featureRed);
	//	keks.addProductFeature(featureBlue);

	//	keksInstance = new KeksInstance(keks);
		// catalog.add(keksType);
	}

	@Test(expected = NullPointerException.class)
	public void testNullCheckConstructor() {
		PersistentInventory inventory = new PersistentInventory(null);
	}

	@Test(expected = NullPointerException.class)
	public void testNullCheckArgument() {
		inventory.add(null);
	}

	@Test
	public void testAdd() {
		inventory.add(keksInstance);
	}

	@Test
	public void testRemove() {
		inventory.add(keksInstance);
		inventory.remove(keksInstance.getIdentifier());
		assertFalse(inventory.contains(keksInstance.getIdentifier()));
	}

	@Test
	public void testContains() {
		inventory.add(keksInstance);
		assertTrue(inventory.contains(keksInstance.getIdentifier()));
	}

	@Test
	public void testGet() {
		inventory.add(keksInstance);
		assertEquals(keksInstance, inventory.get(KeksInstance.class, keksInstance.getIdentifier()));
	}

	/*
	@Test
	public void find() {

		keksInstance = new KeksInstance(keks, featureRed.getIdentifier());

		inventory.add(keksInstance);

		keksInstance = new KeksInstance(keks, featureBlue.getIdentifier());
		inventory.add(keksInstance);

		//
		// this is also a test: because Yellow is not in keksType, it should not
		// be in Keks. this test is passed.
		//
		keksInstance = new KeksInstance(keks, featureYellow.getIdentifier());
		inventory.add(keksInstance);

		System.out.println("Alle Kekse: ");
		for (KeksInstance k : inventory.find(KeksInstance.class)) {
			System.out.println(k);
			for (ProductFeature p : k.getProductFeatures()) {
				System.out.println("\t" + p);
			}
		}
		System.out.println("Fliegen hoch!");

		List<ProductFeature> featureSet = new ArrayList<ProductFeature>();
		// add automated tests for:
		 // - one feature
		 // - multiple features
		// - a feature not in productfeatures
		//
		featureSet.add(featureRed);
		//featureSet.add(featureYellow);

		Iterable<KeksInstance> kekse = inventory.find(KeksInstance.class,
				keksInstance.getProductIdentifier(), featureSet);
		for (KeksInstance k : kekse) {
			System.out.println(k);
			for (ProductFeature p : k.getProductFeatures()) {
				System.out.println("\t" + p);
			}
		}
	}
	
	*/
}