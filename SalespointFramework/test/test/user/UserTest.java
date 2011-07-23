package test.user;

import static org.junit.Assert.*;

import org.junit.Test;
import org.salespointframework.util.ArgumentNullException;


public class UserTest {
	
	private MyCustomer c = new MyCustomer("testCustomer", "pw1234");
	private MyEmployee e = new MyEmployee("testEmployee", "4321pw");
	
	@Test(expected = ArgumentNullException.class)
	public void testNotNullUserId(){
		@SuppressWarnings("unused")
		MyCustomer c0= new MyCustomer(null, "IHaveNoUserPassword");
	}

	@Test(expected = ArgumentNullException.class)
	public void testNotNullPassword(){
		@SuppressWarnings("unused")
		MyCustomer c0= new MyCustomer("notWorkingGuy", null);
	}
	
	@Test
	public void testCustomerUserId(){
		assertEquals("testCustomer", c.getUserId());
	}
	
	@Test
	public void testCustomerPassword(){
		
		assertEquals(true, c.verifyPassword("pw1234"));
	}
	
	
	@Test
	public void testEmployeeUserId(){
		assertEquals("testEmployee", e.getUserId());
	}
	
	@Test
	public void testEmployeePassword(){
		
		assertEquals(true, e.verifyPassword("4321pw"));
	}
	


}
