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
import org.salespointframework.util.ArgumentNullException;

import test.product.Keks;

@SuppressWarnings("javadoc")
public class CatalogTest {

	private final PersistentCatalog catalog = new PersistentCatalog();
	private Keks keks;
	
	@BeforeClass
	public static void beforeClass() {
		Database.INSTANCE.initializeEntityManagerFactory("SalespointFramework");
	}
	
	@Before
	public void before() {
		keks = new Keks("Schoki", new Money(0));
	}

	// TODO verschieben
	@Test
	public void emFindTest() {
		catalog.add(keks);
		Keks kT1 = catalog.get(Keks.class, keks.getIdentifier());
		PersistentProduct kT2 = catalog.get(PersistentProduct.class, keks.getIdentifier());
		assertEquals(kT1,kT2);
	}

	@Test(expected = ArgumentNullException.class)
	public void nullAddTest() {
		catalog.add(null);
	}

	@Test()
	public void addTest() {
		catalog.add(keks);
	}
	
	@Test
	public void testRemove() {
		catalog.add(keks);
		catalog.remove(keks.getIdentifier());
		assertFalse(catalog.contains(keks.getIdentifier()));
	}
	
	@Test
	public void testContains() {
		catalog.add(keks);
		assertTrue(catalog.contains(keks.getIdentifier()));
	}
	
	@Test
	public void getTest() {
		catalog.add(keks);
		assertEquals(keks, catalog.get(Keks.class, keks.getIdentifier()));
	}

}
