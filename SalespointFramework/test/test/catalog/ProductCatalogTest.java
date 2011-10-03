package test.catalog;

import static junit.framework.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.salespointframework.core.catalog.PersistentCatalog;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.money.Money;
import org.salespointframework.util.ArgumentNullException;

import test.product.KeksType;

@SuppressWarnings("javadoc")
public class ProductCatalogTest {

	@BeforeClass
	public static void setUp() {
		Database.INSTANCE.initializeEntityManagerFactory("SalespointFramework");
	}

	@Test(expected = ArgumentNullException.class)
	public void testNullCheckArgument() {

		PersistentCatalog catalog = new PersistentCatalog();

		catalog.add(null);

	}

	@Test
	public void testFindById() {

		KeksType keks1 = new KeksType("bla", new Money(0));
		PersistentCatalog catalog = new PersistentCatalog();

		catalog.add(keks1);

		KeksType keks2 = catalog.get(KeksType.class, keks1.getIdentifier());

		assertEquals(keks1, keks2);
	}

}
