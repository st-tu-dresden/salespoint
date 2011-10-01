package test.catalog;

import static junit.framework.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.salespointframework.core.catalog.PersistentCatalog;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.money.Money;
import org.salespointframework.util.ArgumentNullException;

import test.product.KeksProduct;

public class ProductCatalogTest {

	@BeforeClass
	public static void setUp() {
		Database.INSTANCE.initializeEntityManagerFactory("SalespointFramework");
	}

	@SuppressWarnings("unused")
	@Test(expected = ArgumentNullException.class)
	public void testNullCheckConstructor() {
		PersistentCatalog catalog = new PersistentCatalog();
	}

	@Test(expected = ArgumentNullException.class)
	public void testNullCheckArgument() {

		PersistentCatalog catalog = new PersistentCatalog();

		catalog.add(null);

	}

	@Test
	public void testFindById() {

		KeksProduct keks1 = new KeksProduct("bla", new Money(0));
		PersistentCatalog catalog = new PersistentCatalog();

		catalog.add(keks1);

		KeksProduct keks2 = catalog.get(KeksProduct.class, keks1.getIdentifier());

		assertEquals(keks1, keks2);
	}

}
