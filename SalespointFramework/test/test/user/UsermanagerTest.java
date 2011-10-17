package test.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.user.PersistentUserManager;
import org.salespointframework.core.user.UserCapability;
import org.salespointframework.core.user.UserIdentifier;
import org.salespointframework.util.Iterables;


@SuppressWarnings("javadoc")
public class UsermanagerTest {

	private static PersistentUserManager userManager;
	
	private static UserIdentifier userIdentifier = new UserIdentifier();
	private static Employee employee = new Employee(userIdentifier, "lala");
	private UserCapability capa;
	
	
	@BeforeClass
	public static void beforeClass() {
		Database.INSTANCE.initializeEntityManagerFactory("SalespointFramework");
		userManager = new PersistentUserManager();
		userManager.add(employee);
	}
		
	@Test
	public void testEmployeeMangerContainsE3(){
		assertEquals(userManager.get(Employee.class, employee.getIdentifier()), employee);
	}
	
	
	@Test
	public void testAddCostumer(){
		UserIdentifier ui= new UserIdentifier("testCustomer");
		Customer c = new Customer(ui, "pw1234");
		
		userManager.add(c);

		assertEquals(userManager.get(Customer.class, c.getIdentifier()), c);
	}
	
	@Test
	public void testAddEmployee(){

		UserIdentifier ui= new UserIdentifier("testEmployee");
		Employee e = new Employee(ui, "4321pw");
				
		
		userManager.add(e);
		
		
		Employee currentE = userManager.get(Employee.class, e.getIdentifier());
		assertEquals(currentE, e);
	}
	
	
	// FIXME
	@Test
	public void testDuplicateEmployee(){
		UserIdentifier ui= new UserIdentifier("testDuplicateEmployee");
		Employee e = new Employee(ui, "4321pw");
		
		
		userManager.add(e);
		
		
		Employee currentE = userManager.get(Employee.class, e.getIdentifier());
		assertEquals(currentE, e);
		
		
		//assertFalse(pum.add(e2));
		
		
		
		
	}
	
	@Test
	public void testGetAllUsersBETTER(){
		final int usersToAdd = 4;
		final List<Employee> list = new ArrayList<Employee>(usersToAdd);
		

		final PersistentUserManager empManager = new PersistentUserManager();
		
		final int oldCount = Iterables.asList((empManager.find(Employee.class))).size();


		for(int n = 0; n < usersToAdd; n++) {
			Employee employee = new Employee(new UserIdentifier(), "egal");		//id egal, besser leer lassen, eigene wird generiert, clasht nicht mit anderen Tests 
			list.add(employee);
			empManager.add(employee);
		}

		
		final int newCount = Iterables.asList((empManager.find(Employee.class))).size();
		
		for(Employee employee : list) {
			assertEquals(empManager.get(Employee.class, employee.getIdentifier()), employee);
		}
		assertEquals(usersToAdd, newCount - oldCount); 
	}
	
	// the following test sucks, delete plz 
	@Ignore
	@Test
	public void testGetAllUsers(){

		UserIdentifier uie1= new UserIdentifier("me1");
		Employee me1= new Employee(uie1 ,"egal");
		UserIdentifier uie2= new UserIdentifier("me2");
		Employee me2= new Employee(uie2 ,"egal");
		UserIdentifier uie3= new UserIdentifier("me3");
		Employee me3= new Employee(uie3 ,"egal");
		UserIdentifier uie4= new UserIdentifier("me4");
		Employee me4= new Employee(uie4 ,"egal");
		
		
		userManager.add(me1);
		userManager.add(me2);
		userManager.add(me3);
		userManager.add(me4);
		
		
			
		//set this Number in order how many Employees u create during the hole test
		int numberOfEmployees =6;
		int countEmployees =0;
		for(Iterator<Employee> i = userManager.find(Employee.class).iterator(); i.hasNext(); ){
			userManager.find(Employee.class);
			countEmployees++;
			i.next();
		}
		assertEquals("me1",userManager.get(Employee.class, uie1), me1);
		assertEquals("me2",userManager.get(Employee.class, uie2), me2);
		assertEquals("me3",userManager.get(Employee.class, uie3), me3);
		assertEquals("me4",userManager.get(Employee.class, uie4), me4);
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
		
		assertTrue(employee.addCapability(capa));
		assertTrue(employee.addCapability(capa2));
		
		assertTrue(employee.hasCapability(capa));
		assertTrue(employee.hasCapability(capa2));
	}
	
	

	@Test
	public void testRemoveCapability(){
		capa= new UserCapability("RemoveTestCapabilityAgain");
		
		employee.addCapability(capa);
		
		
		assertTrue(employee.hasCapability(capa));
		
		
		assertTrue(employee.removeCapability(capa));
		
		assertFalse(employee.hasCapability(capa));
	}
	
	@Test
	public void testUpdateEmployee() {
		Employee e= new Employee(new UserIdentifier(), "", "Torsten", "Lehmann");
		final PersistentUserManager empManager = new PersistentUserManager();
		
		empManager.add(e);
		
		Employee e2= empManager.get(Employee.class, e.getIdentifier());
		assertEquals(e.getName(), e2.getName());
		assertEquals(e.getLastname(), e2.getLastname());
		
		//e2.setName("Hans");
		e2.addCapability(new UserCapability("test"));
		empManager.update(e2);
		
		Employee e3= empManager.get(Employee.class, e.getIdentifier());
		assertTrue(e3.hasCapability(new UserCapability("test")));
		
		
		
	}
	
	@Test
	public void testTokenGetter() {
	    Employee e = new Employee(new UserIdentifier(), "test");
	    PersistentUserManager mgr = new PersistentUserManager();
	    
	    mgr.add(e);
	    mgr.login(e, "SESSION");
	    
	    assertEquals(e, mgr.getUserByToken(Employee.class, "SESSION"));
	}
	
	
	
}
