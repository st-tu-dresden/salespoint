package test.catalog;

import static junit.framework.Assert.assertEquals;

import javax.persistence.EntityManagerFactory;

import org.junit.BeforeClass;
import org.junit.Test;
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
		KeksCatalog catalog = new KeksCatalog();
	}

	@Test(expected = ArgumentNullException.class)
	public void testNullCheckArgument() {

		KeksCatalog catalog = new KeksCatalog();

		catalog.add(null);

	}

	@Test
	public void testFindById() {

		KeksProduct keks1 = new KeksProduct("bla", new Money(0));
		KeksCatalog catalog = new KeksCatalog();

		catalog.add(keks1);

		KeksProduct keks2 = catalog.getProductType(KeksProduct.class, keks1.getProductIdentifier());

		assertEquals(keks1, keks2);
	}

}
