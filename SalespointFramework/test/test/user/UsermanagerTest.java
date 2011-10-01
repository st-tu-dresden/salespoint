package test.user;

import static org.junit.Assert.assertEquals;

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


public class UsermanagerTest {

	private static UserIdentifier userIdentifier = new UserIdentifier();
	private static MyEmployee employee = new MyEmployee(userIdentifier, "lala");
	private UserCapability capa;
	private static PersistentUserManager userManager;
	
	@BeforeClass
	public static void setUp() {
		Database.INSTANCE.initializeEntityManagerFactory("SalespointFramework");

		userManager = new PersistentUserManager();
	}
	
	@Test
	public void testEmployeeMangerContainsE3(){
		assertEquals(userManager.get(MyEmployee.class, employee.getIdentifier()), employee);
	}
	
	
	@Test
	public void testAddCostumer(){
		UserIdentifier ui= new UserIdentifier("testCustomer");
		MyCustomer c = new MyCustomer(ui, "pw1234");
		
		userManager.add(c);

		assertEquals(userManager.get(MyCustomer.class, c.getIdentifier()), c);
	}
	
	@Test
	public void testAddEmployee(){

		UserIdentifier ui= new UserIdentifier("testEmployee");
		MyEmployee e = new MyEmployee(ui, "4321pw");
				
		
		userManager.add(e);
		
		
		MyEmployee currentE = userManager.get(MyEmployee.class, e.getIdentifier());
		assertEquals(currentE, e);
	}
	
	
	// FIXME
	@Test
	public void testDuplicateEmployee(){
		UserIdentifier ui= new UserIdentifier("testDuplicateEmployee");
		MyEmployee e = new MyEmployee(ui, "4321pw");
		
		
		userManager.add(e);
		
		
		MyEmployee currentE = userManager.get(MyEmployee.class, e.getIdentifier());
		assertEquals(currentE, e);
		
		
		//assertFalse(pum.add(e2));
		
		
		
		
	}
	
	@Test
	public void testGetAllUsersBETTER(){
		final int usersToAdd = 4;
		final List<MyEmployee> list = new ArrayList<MyEmployee>(usersToAdd);
		

		final PersistentUserManager empManager = new PersistentUserManager();
		
		final int oldCount = Iterables.toList((empManager.find(MyEmployee.class))).size();


		for(int n = 0; n < usersToAdd; n++) {
			MyEmployee employee = new MyEmployee(new UserIdentifier(), "egal");		//id egal, besser leer lassen, eigene wird generiert, clasht nicht mit anderen Tests 
			list.add(employee);
			empManager.add(employee);
		}

		
		final int newCount = Iterables.toList((empManager.find(MyEmployee.class))).size();
		
		for(MyEmployee employee : list) {
			assertEquals(empManager.get(MyEmployee.class, employee.getIdentifier()), employee);
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
		
		
		userManager.add(me1);
		userManager.add(me2);
		userManager.add(me3);
		userManager.add(me4);
		
		
			
		//set this Number in order how many Employees u create during the hole test
		int numberOfEmployees =6;
		int countEmployees =0;
		for(Iterator<MyEmployee> i = userManager.find(MyEmployee.class).iterator(); i.hasNext(); ){
			userManager.find(MyEmployee.class);
			countEmployees++;
			i.next();
		}
		assertEquals("me1",userManager.get(MyEmployee.class, uie1), me1);
		assertEquals("me2",userManager.get(MyEmployee.class, uie2), me2);
		assertEquals("me3",userManager.get(MyEmployee.class, uie3), me3);
		assertEquals("me4",userManager.get(MyEmployee.class, uie4), me4);
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
		
		boolean addCapa = userManager.addCapability(employee, capa);
		boolean addCapa2 = userManager.addCapability(employee, capa2);

		
		assertEquals("NoSuchUser!", true,  addCapa);
		assertEquals("NoSuchUser!", true,  addCapa2);
		
		boolean hasCapa = userManager.hasCapability(employee, capa);
		boolean hasCapa2 = userManager.hasCapability(employee, capa2);
		assertEquals("1",true,  hasCapa);
		assertEquals("2",true,  hasCapa2);
	}
	
	

	@Test
	public void testRemoveCapability(){
		capa= new UserCapability("RemoveTestCapabilityAgain");
		
		userManager.addCapability(employee, capa);
		
		
		boolean hasCapa = userManager.hasCapability(employee, capa);
		assertEquals("befor removing", true,  hasCapa);
		
		
		boolean remo =userManager.removeCapability(employee, capa);
		
		assertEquals("during removing", true,  remo);
		
		boolean hasCapa2 = userManager.hasCapability(employee, capa);
		assertEquals("after removing", false,  hasCapa2);
	}
	
}
