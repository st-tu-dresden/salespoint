package test.user;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.BeforeClass;
import org.junit.Test;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.users.Customer;
import org.salespointframework.core.users.DuplicateUserException;
import org.salespointframework.core.users.AbstractUserManager;
import org.salespointframework.core.users.Employee;
import org.salespointframework.core.users.User;


public class UsermanagerTest {

	private MyCustomer c = new MyCustomer("testCustomer", "pw1234");
	private MyEmployee e = new MyEmployee("testEmployee", "4321pw");
	private MyEmployee e2 = new MyEmployee("testEmployee", "4321pw");
	private MyCustomer c2 = new MyCustomer("testCustomer", "pw1234");
	
	private static EntityManagerFactory emf;
	private static EntityManager emC;
	private static EntityManager emE;
	private static MyCustomerManager customerManager;
	private static MyEmployeeManager employeeManager;
	
	@BeforeClass
	public static void setUp() {
		Database.INSTANCE.initializeEntityManagerFactory("SalespointFramework");
		emf = Database.INSTANCE.getEntityManagerFactory();
		emC = emf.createEntityManager();
		emE = emf.createEntityManager();
		customerManager= new MyCustomerManager(emC);
		employeeManager= new MyEmployeeManager(emC);
		
	}	
	
	@Test
	public void testAddCostumer(){
				
		emC.getTransaction().begin();
		customerManager.addUser(c);
		emC.getTransaction().commit();
	}
	
	@Test
	public void testAddEmployee(){
		emE.getTransaction().begin();
		employeeManager.addUser(e);
		emE.getTransaction().commit();
	}
	
	
	@Test(expected = DuplicateUserException.class)
	public void testDuplicateEmployee(){
		EntityManager em = emf.createEntityManager();
		employeeManager.addUser(e2);
		em.getTransaction().commit();
		
	}
	
	@Test(expected = DuplicateUserException.class)
	public void testDuplicateCustomer(){
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		customerManager.addUser(c2);
		em.getTransaction().commit();
		
	}
	
	
}
