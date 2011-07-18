package test.catalog;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.*;
import static junit.framework.Assert.assertEquals;

import org.salespointframework.core.database.Database;
import org.salespointframework.core.money.Money;
import org.salespointframework.util.ArgumentNullException;

import test.product.Keks;

public class ProductCatalogTest {

	private EntityManagerFactory emf = Database.INSTANCE.getEntityManagerFactory();
	
	@BeforeClass
	public static void setUp() {
		Database.INSTANCE.initializeEntityManagerFactory("SalespointFramework");
	}
	
	@SuppressWarnings("unused")
	@Test(expected=ArgumentNullException.class)
	public void testNullCheckConstructor() {
		KeksCatalog catalog = new KeksCatalog(null);
	}
	
	@Test(expected=ArgumentNullException.class)
	public void testNullCheckArgument() {
		EntityManager em = emf.createEntityManager();
		
		KeksCatalog catalog = new KeksCatalog(em);
		
		catalog.addProductType(null);
		
	}
	
	@Test
	public void testFindById() {
		EntityManager em = emf.createEntityManager();
		
		Keks keks1 = new Keks("bla",new Money(0));
		KeksCatalog catalog = new KeksCatalog(em);
		
		
		em.getTransaction().begin();
		
		catalog.addProductType(keks1);
		
		em.getTransaction().commit();
		
		Keks keks2 = catalog.findProductTypeByProductIdentifier(keks1.getProductIdentifier());
		
		assertEquals(keks1, keks2);
	}
	
	//@Test
}
