package test.catalog;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.salespointframework.core.catalog.PersistentCatalog;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.PersistentProduct;
import org.salespointframework.core.product.PersistentProductType;
import org.salespointframework.util.ArgumentNullException;

import test.product.KeksType;

@SuppressWarnings("javadoc")
public class ProductCatalogTest {

	private final PersistentCatalog catalog = new PersistentCatalog();
	private KeksType keksType;
	
	@BeforeClass
	public static void beforeClass() {
		Database.INSTANCE.initializeEntityManagerFactory("SalespointFramework");
	}
	
	@Before
	public void before() {
		keksType = new KeksType("Schoki", new Money(0));
	}

	// TODO verschieben
	@Test
	public void emFindTest() {
		catalog.add(keksType);
		KeksType kT1 = catalog.get(KeksType.class, keksType.getIdentifier());
		PersistentProductType kT2 = catalog.get(PersistentProductType.class, keksType.getIdentifier());
		assertEquals(kT1,kT2);
	}

	@Test(expected = ArgumentNullException.class)
	public void nullAddTest() {
		catalog.add(null);
	}

	@Test()
	public void addTest() {
		catalog.add(keksType);
	}
	
	@Test
	public void testRemove() {
		catalog.add(keksType);
		catalog.remove(keksType.getIdentifier());
		assertFalse(catalog.contains(keksType.getIdentifier()));
	}
	
	@Test
	public void testContains() {
		catalog.add(keksType);
		assertTrue(catalog.contains(keksType.getIdentifier()));
	}
	
	@Test
	public void getTest() {
		catalog.add(keksType);
		assertEquals(keksType, catalog.get(KeksType.class, keksType.getIdentifier()));
	}

}
