package test.user;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.users.Customer;
import org.salespointframework.core.users.DuplicateUserException;
import org.salespointframework.core.users.AbstractUserManager;
import org.salespointframework.core.users.Employee;


public class UsermangerTest {

	private MyCustomer c = new MyCustomer("testCustomer", "pw1234");
	private MyEmployee e = new MyEmployee("testEmployee", "4321pw");
	private MyEmployee e2 = new MyEmployee("testEmployee", "4321pw");
	private MyCustomer e3 = new MyCustomer("testEmployee", "4321pw");
	
	private static EntityManagerFactory emf = Database.INSTANCE.getEntityManagerFactory();
	private static EntityManager em;
	private static AbstractUserManager<Customer> costumerManager;
	private static AbstractUserManager<Employee> employeeManager;
	
	@BeforeClass
	public static void setUp() {
		Database.INSTANCE.initializeEntityManagerFactory("SalespointFramework");
		em = emf.createEntityManager();
		costumerManager= new AbstractUserManager<Customer>(em);
		employeeManager= new AbstractUserManager<Employee>(em);
		
	}	
	
	@Test
	public void testAddCostumer(){
		em.getTransaction().begin();
		costumerManager.addUser(c);
		em.getTransaction().commit();
	}
	
	@Test
	public void testAddEmployee(){
		em.getTransaction().begin();
		employeeManager.addUser(e);
		em.getTransaction().commit();
	}
	
	
	@Test(expected = DuplicateUserException.class)
	public void testDuplicateUser1(){
		em.getTransaction().begin();
		employeeManager.addUser(e2);
		em.getTransaction().commit();
		
	}
	
	@Test(expected = DuplicateUserException.class)
	public void testDuplicateUser2(){
		em.getTransaction().begin();
		costumerManager.addUser(e3);
		em.getTransaction().commit();
		
	}
	
	
}
