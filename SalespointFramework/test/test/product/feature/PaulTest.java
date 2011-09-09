package test.product.feature;

import javax.persistence.EntityManagerFactory;

import org.junit.BeforeClass;
import org.junit.Test;
import org.salespointframework.core.catalog.PersistentCatalog;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.PersistentProductType;
import org.salespointframework.core.product.ProductFeature;

// FIXME
public class PaulTest {

	@BeforeClass
	public static void setUp() {
		Database.INSTANCE.initializeEntityManagerFactory("SalespointFramework");
	}
	
	@Test
	public void testPAUL() {
		PersistentProductType cookie = new PersistentProductType("Cookie", new Money(10));
		ProductFeature pf1 = ProductFeature.create("Crispyness", "crisp", Money.ZERO);
		ProductFeature pf2 = ProductFeature.create("Crispyness", "hard", Money.ZERO);
		
		cookie.addProductFeature(pf1);
		cookie.addProductFeature(pf2);
		
		PersistentCatalog catalog = new PersistentCatalog();
		catalog.add(cookie);
	}
}
