package test.user;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.salespointframework.core.database.Database;
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
	private static EntityManager emC;
	private static EntityManager emE;
	private static MyEmployeeManager employeeManager;
	private static MyCustomerManager customerManager;

	@BeforeClass
	public static void setUp() {
		Database.INSTANCE.initializeEntityManagerFactory("SalespointFramework");
		emf = Database.INSTANCE.getEntityManagerFactory();
		emC = emf.createEntityManager();
		emE = emf.createEntityManager();
		customerManager = new MyCustomerManager(emC);
		employeeManager = new MyEmployeeManager(emE);

	}

	@After
	public void runAfterEveryTest() {
		if (emE.getTransaction().isActive()) {
			emE.getTransaction().rollback();
		}
		if (emC.getTransaction().isActive()) {
			emC.getTransaction().rollback();
		}

	}

	@Test
	public void testHasEmployeeE3AndHasPW() {
		UserIdentifier ui3 = new UserIdentifier();
		MyEmployee e3 = new MyEmployee(ui3, "lala");
		employeeManager.addUser(e3);
		// WTF. plz add e3 to eM first.
		assertEquals(
				employeeManager.getUserByIdentifier(MyEmployee.class, e3.getUserIdentifier()), e3);
		assertEquals(
				employeeManager.getUserByIdentifier(MyEmployee.class, e3.getUserIdentifier())
						.verifyPassword("lala"), true);
	}

	@Test
	public void testE3HasCapability() {
		// Yeah. same string (=identifier) as above. this will work well.
		// Instead, just auto-generate an id.
		UserIdentifier ui3 = new UserIdentifier();
		MyEmployee e3 = new MyEmployee(ui3, "lala");
		UserCapability capa2 = new UserCapability(
				"MustBeInDataBaseAfterTesting");
		// same as above. add user and capability first.
		employeeManager.addUser(e3);
		employeeManager.addCapability(e3, capa2);
		assertTrue(employeeManager.hasCapability(e3, capa2));

	}

	@Test
	public void testE3HasNOTCapability() {
		// same as above. generate new id.
		UserIdentifier ui3 = new UserIdentifier();
		MyEmployee e3 = new MyEmployee(ui3, "lala");
		UserCapability capa = new UserCapability("CrazyTestCapabilityAgain");
		// again, meaningless. of course the capability is not found if
		// e3 is ever added to the manager.
		employeeManager.addUser(e3);
		assertFalse(employeeManager.hasCapability(e3, capa));
	}

}
