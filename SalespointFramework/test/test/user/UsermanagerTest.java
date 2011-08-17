package test.user;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.users.DuplicateUserException;
import org.salespointframework.core.users.UserCapability;
import org.salespointframework.core.users.UserIdentifier;
import org.salespointframework.util.Iterables;


public class UsermanagerTest {

	
	private UserIdentifier ui1= new UserIdentifier("testCustomer");
	
	private MyCustomer c = new MyCustomer(ui1, "pw1234");
	private MyCustomer c2 = new MyCustomer(ui1, "pw1234");
	
	
	private UserIdentifier ui2= new UserIdentifier("testEmployee");
	
	private static UserIdentifier ui3= new UserIdentifier("CapaEmployee");
	
	private MyEmployee e = new MyEmployee(ui2, "4321pw");
	private MyEmployee e2 = new MyEmployee(ui2, "pw");
	private static MyEmployee e3 = new MyEmployee(ui3, "lala");
	
	
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
		employeeManager= new MyEmployeeManager(emE);
		
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
		assertEquals(employeeManager.getUserByIdentifier(e3.getUserIdentifier()), e3);
	}
	
	
	@Test
	public void testAddCostumer(){
		EntityTransaction t= emC.getTransaction();
		t.begin();
		customerManager.addUser(c);
		t.commit();
		assertEquals(customerManager.getUserByIdentifier(c.getUserIdentifier()), c);
	}
	
	@Test
	public void testAddEmployee(){
		emE.getTransaction().begin();
		employeeManager.addUser(e);
		emE.getTransaction().commit();
		
		MyEmployee currentE = employeeManager.getUserByIdentifier(e.getUserIdentifier());
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
	public void testGetAllUsersBETTER(){
		final int usersToAdd = 4;
		final List<MyEmployee> list = new ArrayList<MyEmployee>(usersToAdd);
		
		final EntityManager entityManager = emf.createEntityManager();
		final MyEmployeeManager empManager = new MyEmployeeManager(entityManager);
		
		final int oldCount = Iterables.toList((empManager.getUsers())).size();

		entityManager.getTransaction().begin();
		for(int n = 0; n < usersToAdd; n++) {
			MyEmployee employee = new MyEmployee(new UserIdentifier(), "egal");		//id egal, besser leer lassen, eigene wird generiert, clasht nicht mit anderen Tests 
			list.add(employee);
			empManager.addUser(employee);
		}
		entityManager.getTransaction().commit();
		
		final int newCount = Iterables.toList((empManager.getUsers())).size();
		
		for(MyEmployee employee : list) {
			assertEquals(empManager.getUserByIdentifier(employee.getUserIdentifier()), employee);
		}
		assertEquals(usersToAdd, newCount - oldCount); 
	}
	
	// the following test sucks, delete plz 
	@Test
	public void testGetAllUsers(){

		UserIdentifier uie1= new UserIdentifier("me1");
		MyEmployee me1= new MyEmployee(uie1 ,"egal");
		UserIdentifier uie2= new UserIdentifier("me2");
		MyEmployee me2= new MyEmployee(uie2 ,"egal");
		UserIdentifier uie3= new UserIdentifier("me3");
		MyEmployee me3= new MyEmployee(uie3 ,"egal");
		UserIdentifier uie4= new UserIdentifier("me4");
		MyEmployee me4= new MyEmployee(uie4 ,"egal");
		
		emE.getTransaction().begin();
		employeeManager.addUser(me1);
		employeeManager.addUser(me2);
		employeeManager.addUser(me3);
		employeeManager.addUser(me4);
		emE.getTransaction().commit();
		
			
		//set this Number in order how many Employees u create during the hole test
		int numberOfEmployees =6;
		int countEmployees =0;
		for(Iterator<MyEmployee> i = employeeManager.getUsers().iterator(); i.hasNext(); ){
			employeeManager.getUsers();
			countEmployees++;
			i.next();
		}
		assertEquals("me1",employeeManager.getUserByIdentifier(uie1), me1);
		assertEquals("me2",employeeManager.getUserByIdentifier(uie2), me2);
		assertEquals("me3",employeeManager.getUserByIdentifier(uie3), me3);
		assertEquals("me4",employeeManager.getUserByIdentifier(uie4), me4);
		assertEquals("iterator", numberOfEmployees,countEmployees);
	}
	
	@Test
	public void testCreateCapabilityAndGetName(){
		capa= new UserCapability("CrazyTestCapability");
		assertEquals("CrazyTestCapability", capa.getName());
		
	}
	
//	@Ignore
	@Test
	public void testAddCapabilityToEmployee(){
		capa= new UserCapability("CrazyTestCapabilityAgain");
		UserCapability capa2= new UserCapability("MustBeInDataBaseAfterTesting");
		emE.getTransaction().begin();
		boolean addCapa = employeeManager.addCapability(e3, capa);
		//boolean addCapa2 = employeeManager.addCapability(e3, capa2);
		emE.getTransaction().commit();
		emE.getTransaction().begin();
		boolean addCapa2 = employeeManager.addCapability(e3, capa2);
		emE.getTransaction().commit();
		
		assertEquals("NoSuchUser!", true,  addCapa);
		assertEquals("NoSuchUser!", true,  addCapa2);
	}
	

	@Test
	public void testHasCapability(){
		capa= new UserCapability("CrazyTestCapabilityAgain");
		UserCapability capa2= new UserCapability("MustBeInDataBaseAfterTesting");
		boolean hasCapa = employeeManager.hasCapability(e3, capa);
		boolean hasCapa2 = employeeManager.hasCapability(e3, capa2);
		assertEquals("1",true,  hasCapa);
		assertEquals("2",true,  hasCapa2);
	}
	

	@Test
	public void testRemoveCapability(){
		capa= new UserCapability("CrazyTestCapabilityAgain");
		boolean hasCapa = employeeManager.hasCapability(e3, capa);
		assertEquals("befor removing", true,  hasCapa);
		
		emE.getTransaction().begin();
		boolean remo =employeeManager.removeCapability(e3, capa);
		emE.getTransaction().commit();
		assertEquals("during removing", true,  remo);
		
		boolean hasCapa2 = employeeManager.hasCapability(e3, capa);
		assertEquals("after removing", false,  hasCapa2);
	}
	
}
