package test.user;

import static org.junit.Assert.*;

import org.junit.Test;
import org.salespointframework.core.user.UserIdentifier;
import org.salespointframework.util.ArgumentNullException;


@SuppressWarnings("javadoc")
public class UserTest {
	
	private UserIdentifier ui1= new UserIdentifier("testCustomer");
	private UserIdentifier ui2= new UserIdentifier("testEmployee");
	
	private Customer c = new Customer(ui1, "pw1234");
	private Employee e = new Employee(ui2, "4321pw");
	
	@Test(expected = ArgumentNullException.class)
	public void testNotNullUserId(){
		@SuppressWarnings("unused")
		Customer c0= new Customer(null, "IHaveNoUserPassword");
	}

	@Test(expected = ArgumentNullException.class)
	public void testNotNullPassword(){
		UserIdentifier ui3= new UserIdentifier("notWorkingGuy");
		@SuppressWarnings("unused")
		Customer c0= new Customer(ui3, null);
	}
	
	@Test
	public void testCustomerUserId(){
		UserIdentifier u= new UserIdentifier("testCustomer");
		assertEquals(u, c.getIdentifier());
	}
	
	@Test
	public void testCustomerPassword(){
		
		assertEquals(true, c.verifyPassword("pw1234"));
	}
	
	
	@Test
	public void testEmployeeUserId(){
		UserIdentifier u= new UserIdentifier("testEmployee");
		assertEquals(u, e.getIdentifier());
	}
	
	@Test
	public void testEmployeePassword(){
		
		assertEquals(true, e.verifyPassword("4321pw"));
	}
	


}
