package org.salespointframework.order;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.core.accountancy.payment.Cash;
import org.salespointframework.core.order.Order;
import org.salespointframework.core.order.OrderManager;
import org.salespointframework.core.user.User;
import org.salespointframework.core.user.UserIdentifier;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("javadoc")
public class OrderManagerTest extends AbstractIntegrationTests {

	@Autowired
	private OrderManager orderManager;
	private User user;
	private Order order;

	@Before
	public void before() {
		user = new User(new UserIdentifier(), "");
		order = new Order(user.getIdentifier(), Cash.CASH);
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
