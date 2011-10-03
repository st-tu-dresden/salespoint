package test.inventory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.BeforeClass;
import org.junit.Test;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.inventory.PersistentInventory;
import org.salespointframework.core.money.Money;
import org.salespointframework.util.ArgumentNullException;

import test.product.Keks;
import test.product.KeksType;

@SuppressWarnings("javadoc")
public class InventoryTest {
	private EntityManagerFactory emf = Database.INSTANCE.getEntityManagerFactory();
	
	@BeforeClass
	public static void setUp() {
		Database.INSTANCE.initializeEntityManagerFactory("SalespointFramework");
	}
	
	@SuppressWarnings("unused")
	@Test(expected=ArgumentNullException.class)
	public void testNullCheckConstructor() {
		PersistentInventory inventory = new PersistentInventory(null);
	}
	
	@Test(expected=ArgumentNullException.class)
	public void testNullCheckArgument() {
		EntityManager em = emf.createEntityManager();
		
		PersistentInventory inventory = new PersistentInventory(em);
		
		inventory.add(null);
		
	}
	
	@Test
	public void testAddInstance() {
		EntityManager em = emf.createEntityManager();
		
		PersistentInventory inventory = new PersistentInventory();
		
		KeksType kp = new KeksType("Wheee", Money.ZERO);
		Keks ki = new Keks(kp);
		
		em.getTransaction().begin();
		
		inventory.add(ki);
		
		em.getTransaction().commit();
	}
}


