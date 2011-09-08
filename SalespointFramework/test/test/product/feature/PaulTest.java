package test.product.feature;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.BeforeClass;
import org.junit.Test;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.features.ProductFeature_old;
import org.salespointframework.core.product.features.ProductFeatureType_old;

import test.catalog.KeksCatalog;
import test.product.KeksProduct;

public class PaulTest {

	private EntityManagerFactory emf = Database.INSTANCE.getEntityManagerFactory();
	
	@BeforeClass
	public static void setUp() {
		Database.INSTANCE.initializeEntityManagerFactory("SalespointFramework");
	}
	
	@Test
	public void testPAUL() {
		KeksProduct keks1 = new KeksProduct("Keks", Money.OVER9000);
		ProductFeatureType_old pft = new ProductFeatureType_old("Farbe", "", ProductFeature_old.create("Blau", Money.ZERO), ProductFeature_old.create("Gelb", Money.ZERO));
		keks1.addProductFeatureType(pft);
		
		EntityManager em = emf.createEntityManager();
		

		KeksCatalog  catalog = new KeksCatalog(em);
		catalog.addProductType(keks1);
		
		em.getTransaction().begin();
		em.getTransaction().commit();
		
	}
}
