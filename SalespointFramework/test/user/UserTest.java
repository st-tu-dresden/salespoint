package user;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.salespointframework.core.users.DuplicateUserException;


public class UserTest {
	
	private MyCustomer c;
	private MyEmployee e;
	
	@BeforeClass
	public void createCustomerAndEmployee(){
		 c= new MyCustomer("testCustomer", "pw1234");
		 e = new MyEmployee("testEmployee", "4321pw");
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
	
	@Test(expected = DuplicateUserException.class)
	public void testDuplicateUser1(){
		MyEmployee e2 = new MyEmployee("testEmployee", "4321pw");
	}
	
	@Test(expected = DuplicateUserException.class)
	public void testDuplicateUser2(){
		MyCustomer e3 = new MyCustomer("testEmployee", "4321pw");
	}

}
