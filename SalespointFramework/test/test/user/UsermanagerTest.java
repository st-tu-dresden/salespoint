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
import org.salespointframework.core.users.PersistentUserManager;
import org.salespointframework.core.users.UserCapability;
import org.salespointframework.core.users.UserIdentifier;
import org.salespointframework.util.Iterables;


public class UsermanagerTest {

	
	
	private static UserIdentifier ui3= new UserIdentifier("CapaEmployee");
	
	
	private static MyEmployee e3 = new MyEmployee(ui3, "lala");
	
	
	private UserCapability capa;
	
	private static EntityManagerFactory emf;
	private static EntityManager em;
	private static PersistentUserManager pum;
	
	
	
	@BeforeClass
	public static void setUp() {
		Database.INSTANCE.initializeEntityManagerFactory("SalespointFramework");
		emf = Database.INSTANCE.getEntityManagerFactory();
		em = emf.createEntityManager();
		pum = new PersistentUserManager();
		em.getTransaction().begin();
		pum.addUser(e3);
		em.getTransaction().commit();
	
	}
	
	@After   
	public void runAfterEveryTest() {   
	    if(em.getTransaction().isActive()){
	    	em.getTransaction().rollback();
	    }
	}   
	
	@Test
	public void testEmployeeMangerContainsE3(){
		assertEquals(pum.get(MyEmployee.class, e3.getUserIdentifier()), e3);
	}
	
	
	@Test
	public void testAddCostumer(){
		UserIdentifier ui= new UserIdentifier("testCustomer");
		MyCustomer c = new MyCustomer(ui, "pw1234");
		
		
		EntityTransaction t= em.getTransaction();
		t.begin();
		pum.addUser(c);
		t.commit();
		assertEquals(pum.get(MyCustomer.class, c.getUserIdentifier()), c);
	}
	
	@Test
	public void testAddEmployee(){

		UserIdentifier ui= new UserIdentifier("testEmployee");
		MyEmployee e = new MyEmployee(ui, "4321pw");
				
		em.getTransaction().begin();
		pum.addUser(e);
		em.getTransaction().commit();
		
		MyEmployee currentE = pum.get(MyEmployee.class, e.getUserIdentifier());
		assertEquals(currentE, e);
	}
	
	
	@Test
	public void testDuplicateEmployee(){
		UserIdentifier ui= new UserIdentifier("testDuplicateEmployee");
		MyEmployee e = new MyEmployee(ui, "4321pw");
		MyEmployee e2 = new MyEmployee(ui, "adfpw");
		
		em.getTransaction().begin();
		pum.addUser(e);
		em.getTransaction().commit();
		
		MyEmployee currentE = pum.get(MyEmployee.class, e.getUserIdentifier());
		assertEquals(currentE, e);
		
		em.getTransaction().begin();
		assertFalse(pum.addUser(e2));
		em.getTransaction().commit();
		
		
		
	}
	
	@Test
	public void testGetAllUsersBETTER(){
		final int usersToAdd = 4;
		final List<MyEmployee> list = new ArrayList<MyEmployee>(usersToAdd);
		
		final EntityManager entityManager = emf.createEntityManager();
		final PersistentUserManager empManager = new PersistentUserManager();
		
		final int oldCount = Iterables.toList((empManager.getUsers(MyEmployee.class))).size();

		entityManager.getTransaction().begin();
		for(int n = 0; n < usersToAdd; n++) {
			MyEmployee employee = new MyEmployee(new UserIdentifier(), "egal");		//id egal, besser leer lassen, eigene wird generiert, clasht nicht mit anderen Tests 
			list.add(employee);
			empManager.addUser(employee);
		}
		entityManager.getTransaction().commit();
		
		final int newCount = Iterables.toList((empManager.getUsers(MyEmployee.class))).size();
		
		for(MyEmployee employee : list) {
			assertEquals(empManager.get(MyEmployee.class, employee.getUserIdentifier()), employee);
		}
		assertEquals(usersToAdd, newCount - oldCount); 
	}
	
	// the following test sucks, delete plz 
	@Ignore
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
		
		em.getTransaction().begin();
		pum.addUser(me1);
		pum.addUser(me2);
		pum.addUser(me3);
		pum.addUser(me4);
		em.getTransaction().commit();
		
			
		//set this Number in order how many Employees u create during the hole test
		int numberOfEmployees =6;
		int countEmployees =0;
		for(Iterator<MyEmployee> i = pum.getUsers(MyEmployee.class).iterator(); i.hasNext(); ){
			pum.getUsers(MyEmployee.class);
			countEmployees++;
			i.next();
		}
		assertEquals("me1",pum.get(MyEmployee.class, uie1), me1);
		assertEquals("me2",pum.get(MyEmployee.class, uie2), me2);
		assertEquals("me3",pum.get(MyEmployee.class, uie3), me3);
		assertEquals("me4",pum.get(MyEmployee.class, uie4), me4);
		assertEquals("iterator", numberOfEmployees,countEmployees);
	}
	
	@Test
	public void testCreateCapabilityAndGetName(){
		capa= new UserCapability("CrazyTestCapability");
		assertEquals("CrazyTestCapability", capa.getName());
		
	}
	
	@Test
	public void testAddCapabilityToEmployee(){
		capa= new UserCapability("CrazyTestCapabilityAgain");
		UserCapability capa2= new UserCapability("MustBeInDataBaseAfterTesting");
		em.getTransaction().begin();
		boolean addCapa = pum.addCapability(e3, capa);
		em.getTransaction().commit();
		em.getTransaction().begin();
		boolean addCapa2 = pum.addCapability(e3, capa2);
		em.getTransaction().commit();
		
		assertEquals("NoSuchUser!", true,  addCapa);
		assertEquals("NoSuchUser!", true,  addCapa2);
		
		boolean hasCapa = pum.hasCapability(e3, capa);
		boolean hasCapa2 = pum.hasCapability(e3, capa2);
		assertEquals("1",true,  hasCapa);
		assertEquals("2",true,  hasCapa2);
	}
	
	

	@Test
	public void testRemoveCapability(){
		capa= new UserCapability("RemoveTestCapabilityAgain");
		em.getTransaction().begin();
		pum.addCapability(e3, capa);
		em.getTransaction().commit();
		
		boolean hasCapa = pum.hasCapability(e3, capa);
		assertEquals("befor removing", true,  hasCapa);
		
		em.getTransaction().begin();
		boolean remo =pum.removeCapability(e3, capa);
		em.getTransaction().commit();
		assertEquals("during removing", true,  remo);
		
		boolean hasCapa2 = pum.hasCapability(e3, capa);
		assertEquals("after removing", false,  hasCapa2);
	}
	
}
