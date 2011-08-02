package test.user;

import static org.junit.Assert.*;

import java.util.Iterator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.users.DuplicateUserException;
import org.salespointframework.core.users.UserCapability;


public class UsermanagerTest {

	private MyCustomer c = new MyCustomer("testCustomer", "pw1234");
	private MyCustomer c2 = new MyCustomer("testCustomer", "pw1234");
	
	private MyEmployee e = new MyEmployee("testEmployee", "4321pw");
	private MyEmployee e2 = new MyEmployee("testEmployee", "4321pw");
	private static MyEmployee e3 = new MyEmployee("CapaEmployee", "lala");
	
	
	private UserCapability capa;
	
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
		
		emE.getTransaction().begin();
		employeeManager.addUser(e3);
		emE.getTransaction().commit();
		
	}
	
	@After   
	public void runAfterEveryTest() {   
	    if(emE.getTransaction().isActive()){
	    	emE.getTransaction().rollback();
	    }
	    if(emC.getTransaction().isActive()){
	    	emC.getTransaction().rollback();
	    }
	    
	}   
	
	@Test
	public void testEmployeeMangerContainsE3(){
		assertEquals(employeeManager.getUserById(e3.getUserId()), e3);
	}
	
	
	@Test
	public void testAddCostumer(){
				
		emC.getTransaction().begin();
		customerManager.addUser(c);
		emC.getTransaction().commit();
		assertEquals(customerManager.getUserById(c.getUserId()), c);
	}
	
	@Test
	public void testAddEmployee(){
		emE.getTransaction().begin();
		employeeManager.addUser(e);
		emE.getTransaction().commit();
		
		MyEmployee currentE = employeeManager.getUserById(e.getUserId());
		assertEquals(currentE, e);
	}
	
	
	@Test(expected = DuplicateUserException.class)
	public void testDuplicateEmployee(){
		emE.getTransaction().begin();
		employeeManager.addUser(e2);
		emE.getTransaction().commit();
		
	}
	
	@Test(expected = DuplicateUserException.class)
	public void testDuplicateCustomer(){
		
		emC.getTransaction().begin();
		customerManager.addUser(c2);
		emC.getTransaction().commit();
		
	}
	
	@Test
	public void testGetAllUsers(){
		//set this Number in order how many Employees u create during testing
		int numberOfEmployees =2;
		int sizeOfAllEmployees =0;
		for(Iterator<MyEmployee> i = employeeManager.getUsers().iterator(); i.hasNext(); ){
			employeeManager.getUsers();
			System.out.println("zählen");
			sizeOfAllEmployees++;
			i.next();
		}
		assertEquals(numberOfEmployees,sizeOfAllEmployees);
	}
	
	@Test
	public void testCreateCapabilityAndGetName(){
		capa= new UserCapability("CrazyTestCapability");
		assertEquals("CrazyTestCapability", capa.getName());
		
	}
	
	@Ignore
	@Test
	public void testAddCapabilityToEmployee(){
		capa= new UserCapability("CrazyTestCapabilityAgain");
		emC.getTransaction().begin();
		boolean addCapa = employeeManager.addCapability(e3, capa);
		emC.getTransaction().commit();
		assertEquals("NoSuchUser!", true,  addCapa);
	}
	
	@Ignore
	@Test
	public void testHasCapability(){
		capa= new UserCapability("CrazyTestCapabilityAgain");
		boolean hasCapa = employeeManager.hasCapability(e3, capa);
		assertEquals(true,  hasCapa);
	}
	
	@Ignore
	@Test
	public void testRemoveCapability(){
		capa= new UserCapability("CrazyTestCapabilityAgain");
		boolean hasCapa = employeeManager.hasCapability(e3, capa);
		assertEquals("befor removing", true,  hasCapa);
		
		emC.getTransaction().begin();
		boolean remo =employeeManager.removeCapability(e3, capa);
		emC.getTransaction().commit();
		assertEquals("during removing", true,  remo);
		
		boolean hasCapa2 = employeeManager.hasCapability(e3, capa);
		assertEquals("after removing", false,  hasCapa2);
	}
	
}
