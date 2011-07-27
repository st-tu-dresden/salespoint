package test.inventory;

import static junit.framework.Assert.assertEquals;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.BeforeClass;
import org.junit.Test;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.money.Money;
import org.salespointframework.util.ArgumentNullException;

import test.product.KeksInstance;
import test.product.KeksProduct;

public class InventoryTest {
	private EntityManagerFactory emf = Database.INSTANCE.getEntityManagerFactory();
	
	@BeforeClass
	public static void setUp() {
		Database.INSTANCE.initializeEntityManagerFactory("SalespointFramework");
	}
	
	@SuppressWarnings("unused")
	@Test(expected=ArgumentNullException.class)
	public void testNullCheckConstructor() {
		KeksInventory inventory = new KeksInventory(null);
	}
	
	@Test(expected=ArgumentNullException.class)
	public void testNullCheckArgument() {
		EntityManager em = emf.createEntityManager();
		
		KeksInventory inventory = new KeksInventory(em);
		
		inventory.addProductInstance(null);
		
	}
	
	@Test
	public void testAddInstance() {
		EntityManager em = emf.createEntityManager();
		
		KeksInventory inventory = new KeksInventory(em);
		
		KeksProduct kp = new KeksProduct("Wheee" ,new Money(0));
		KeksInstance ki = new KeksInstance(kp);
		
		em.getTransaction().begin();
		
		inventory.addProductInstance(ki);
		
		em.getTransaction().commit();
	}
}


