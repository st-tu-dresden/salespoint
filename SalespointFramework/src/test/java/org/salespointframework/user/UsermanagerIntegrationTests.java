package org.salespointframework.user;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.core.user.UserManager;
import org.salespointframework.core.useraccount.UserAccountIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


// FIXME
@SuppressWarnings("javadoc")
@Transactional
public class UsermanagerIntegrationTests extends AbstractIntegrationTests {

	@Autowired
	private UserManager userManager;
	
	private UserAccountIdentifier userIdentifier;
	private Customer customer;
	
	@Before
	public void before() {
		userIdentifier = new UserAccountIdentifier();
		customer = new Customer(userIdentifier, "password");
	}
	
	@Test(expected = NullPointerException.class)
	public void testAddNull() {
		userManager.add(null);
		
	}
	
	@Test
	public void testAddContains() {
		userManager.add(customer);
		assertTrue(userManager.contains(customer.getIdentifier()));
	}
	
	@Test
	public void testRemove() {
		userManager.add(customer);
		userManager.remove(customer.getIdentifier());
		assertFalse(userManager.contains(customer.getIdentifier()));
	}
	
	@Test
	public void testFind() {
		userManager.add(customer);
		Iterable<Customer> customers = userManager.find(Customer.class);
		
		assertThat(customers, is(Matchers.<Customer> iterableWithSize(1)));
		assertThat(customers, hasItem(customer));
	}

	
	@Test
	public void testGet() {
		userManager.add(customer);
		assertEquals(customer, userManager.get(Customer.class, customer.getIdentifier()));
	}

}
