package spielwiese;

import org.junit.*;
import static junit.framework.Assert.assertEquals;

import org.salespointframework.core.database.Database;
import org.salespointframework.core.money.Money;

public class OhNomNomNom {

	@BeforeClass
	public static void setUp() {
		Database.getInstance().initializeEntityManagerFactory("SalespointFramework");
	}
	
	@Test
	public void blablu() {
		Keks keks = new Keks("bla", new Money());
		KeksCatalog catalog = new KeksCatalog(Keks.class);
		catalog.addProductType(keks);
		assertEquals(keks, catalog.findProductTypeByProductIdentifier(keks.getProductIdentifier()));
	}
}
