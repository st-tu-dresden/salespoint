package test.user;

import static org.junit.Assert.*;

import org.junit.Test;
import org.salespointframework.core.user.UserIdentifier;


@SuppressWarnings("javadoc")
public class UserTest {
	
	private static final String passwordCustomer = "pw1234";
	private static final String passwordEmployee = "4321pw"; 
	
	
	private UserIdentifier ui1 = new UserIdentifier("testCustomer");
	private UserIdentifier ui2 = new UserIdentifier("testEmployee");
	
	private Customer customer = new Customer(ui1, passwordCustomer);
	private Employee employee = new Employee(ui2, passwordEmployee);
	
	@Test(expected = NullPointerException.class)
	public void testNotNullUserId(){
		@SuppressWarnings("unused")
		Customer c0= new Customer(null, "IHaveNoUserPassword");
	}

	@Test(expected = NullPointerException.class)
	public void testNotNullPassword(){
		UserIdentifier ui3= new UserIdentifier("notWorkingGuy");
		@SuppressWarnings("unused")
		Customer c0= new Customer(ui3, null);
	}
	
	@Test
	public void testCustomerUserId(){
		UserIdentifier u= new UserIdentifier("testCustomer");
		assertEquals(u, customer.getIdentifier());
	}
	
	@Test
	public void testCustomerPassword(){
		
		assertEquals(true, customer.verifyPassword(passwordCustomer));
	}
	
	
	@Test
	public void testEmployeeUserId(){
		UserIdentifier u= new UserIdentifier("testEmployee");
		assertEquals(u, employee.getIdentifier());
	}
	
	@Test
	public void testEmployeePassword(){
		
		assertEquals(true, employee.verifyPassword(passwordEmployee));
	}
	


}
