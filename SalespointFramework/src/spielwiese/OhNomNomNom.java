package spielwiese;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.*;
import static junit.framework.Assert.assertEquals;

import org.salespointframework.core.database.Database;
import org.salespointframework.core.money.Money;

public class OhNomNomNom {

	private EntityManagerFactory emf = Database.INSTANCE.getEntityManagerFactory();
	
	@BeforeClass
	public static void setUp() {
		Database.INSTANCE.initializeEntityManagerFactory("SalespointFramework");
	}
	
	@Test
	public void blablu() {
		EntityManager em = emf.createEntityManager();
		
		Keks keks = new Keks("bla", new Money());
		KeksCatalog catalog = new KeksCatalog(em);
		catalog.addProductType(keks);
		assertEquals(keks, catalog.findProductTypeByProductIdentifier(keks.getProductIdentifier()));
	}
}
