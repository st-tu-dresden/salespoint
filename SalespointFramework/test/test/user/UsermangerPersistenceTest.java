package test.user;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.users.PersistentUserManager;
import org.salespointframework.core.users.UserCapability;
import org.salespointframework.core.users.UserIdentifier;

/**
 * Test if some Date is or is not in the Database Use of the test: 1. ensure
 * that in persistence.xml method is on DROP AND CREATE TABLES 2. run
 * UsermangerTest, must be successful 3. change methode persistence.xml to only
 * CREATE or (none) 4. run this Test
 * 
 * @author Christopher Bellmann
 * 
 */
public class UsermangerPersistenceTest {

	private static EntityManagerFactory emf;
	private static EntityManager em;
	private static PersistentUserManager pum;

	@BeforeClass
	public static void setUp() {
		Database.INSTANCE.initializeEntityManagerFactory("SalespointFramework");
		emf = Database.INSTANCE.getEntityManagerFactory();
		em = emf.createEntityManager();
		pum = new PersistentUserManager();
	}

	@After
	public void runAfterEveryTest() {
		if (em.getTransaction().isActive()) {
			em.getTransaction().rollback();
		}
	}

	@Test
	public void testHasEmployeeE3AndHasPW() {
		UserIdentifier ui3 = new UserIdentifier();
		MyEmployee e3 = new MyEmployee(ui3, "lala");
		em.getTransaction().begin();
		pum.addUser(e3);
		em.getTransaction().commit();
		assertEquals(
				pum.get(MyEmployee.class, e3.getUserIdentifier()), e3);
		assertEquals(
				pum.get(MyEmployee.class, e3.getUserIdentifier())
						.verifyPassword("lala"), true);
	}

	@Test
	public void testE3HasCapability() {
		UserIdentifier ui3 = new UserIdentifier();
		MyEmployee e3 = new MyEmployee(ui3, "lala");
		UserCapability capa2 = new UserCapability(
				"MustBeInDataBaseAfterTesting");

		em.getTransaction().begin();
		pum.addUser(e3);
		pum.addCapability(e3, capa2);
		em.getTransaction().commit();
		assertTrue(pum.hasCapability(e3, capa2));

	}

	@Test
	public void testE3HasNOTCapability() {
		UserIdentifier ui3 = new UserIdentifier();
		MyEmployee e3 = new MyEmployee(ui3, "lala");
		UserCapability capa = new UserCapability("CrazyTestCapabilityAgain");
		em.getTransaction().begin();
		pum.addUser(e3);
		em.getTransaction().commit();
		assertFalse(pum.hasCapability(e3, capa));
	}

}
