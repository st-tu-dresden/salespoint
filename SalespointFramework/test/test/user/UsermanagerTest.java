package test.user;

import static org.junit.Assert.*;

import java.util.Iterator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.BeforeClass;
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
		//set this Number in order how many Employees u creat during testing
		int numberOfEmployees =2;
		int sizeOfAllEmployees =0;
		for(Iterator<MyEmployee> i = employeeManager.getUsers().iterator(); i.hasNext(); ){
			employeeManager.getUsers();
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
	
	@Test
	public void testAddCapabilityToEmployee(){
		capa= new UserCapability("CrazyTestCapabilityAgain");
		boolean addCapa = employeeManager.addCapability(e3, capa);
		assertEquals("NoSuchUser!",true,  addCapa);
	}
	
	@Test
	public void testHasCapability(){
		capa= new UserCapability("CrazyTestCapabilityAgain");
		//employeeManager.addCapability(e3, capa);
		boolean hasCapa = employeeManager.hasCapability(e3, capa);
		assertEquals(true,  hasCapa);
	}
	
}
