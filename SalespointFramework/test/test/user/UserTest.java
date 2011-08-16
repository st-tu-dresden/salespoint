package test.user;

import static org.junit.Assert.*;

import org.junit.Test;
import org.salespointframework.core.users.UserIdentifier;
import org.salespointframework.util.ArgumentNullException;


public class UserTest {
	
	private UserIdentifier ui1= new UserIdentifier("testCustomer");
	private UserIdentifier ui2= new UserIdentifier("testEmployee");
	
	private MyCustomer c = new MyCustomer(ui1, "pw1234");
	private MyEmployee e = new MyEmployee(ui2, "4321pw");
	
	@Test(expected = ArgumentNullException.class)
	public void testNotNullUserId(){
		@SuppressWarnings("unused")
		MyCustomer c0= new MyCustomer(null, "IHaveNoUserPassword");
	}

	@Test(expected = ArgumentNullException.class)
	public void testNotNullPassword(){
		UserIdentifier ui3= new UserIdentifier("notWorkingGuy");
		@SuppressWarnings("unused")
		MyCustomer c0= new MyCustomer(ui3, null);
	}
	
	@Test
	public void testCustomerUserId(){
		UserIdentifier u= new UserIdentifier("testCustomer");
		assertEquals(u, c.getUserIdentifier());
	}
	
	@Test
	public void testCustomerPassword(){
		
		assertEquals(true, c.verifyPassword("pw1234"));
	}
	
	
	@Test
	public void testEmployeeUserId(){
		UserIdentifier u= new UserIdentifier("testEmployee");
		assertEquals(u, e.getUserIdentifier());
	}
	
	@Test
	public void testEmployeePassword(){
		
		assertEquals(true, e.verifyPassword("4321pw"));
	}
	


}
