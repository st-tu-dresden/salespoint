package org.salespointframework.order;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.accountancy.payment.Cash;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderManager;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("javadoc")
public class OrderManagerTests extends AbstractIntegrationTests {

	
	@Autowired
	private UserAccountManager userAccountManager;
	
	@Autowired
	private OrderManager orderManager;
	private UserAccount user;
	private Order order;

	@Before
	public void before() {
		user = userAccountManager.create("userId", "");
		userAccountManager.save(user);
		order = new Order(user, Cash.CASH);
	}

	@Test(expected = NullPointerException.class)
	public void nullAddtest() {
		orderManager.add(null);
	}

	@Test
	public void addTest() {
		orderManager.add(order);
	}

	@Test
	public void testContains() {
		orderManager.add(order);
		assertTrue(orderManager.contains(order.getIdentifier()));
	}

	@Test
	public void testGet() {
		orderManager.add(order);
		assertEquals(order,
				orderManager.get(Order.class, order.getIdentifier()));
	}
}
