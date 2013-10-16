package org.salespointframework.order;

import org.junit.Before;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

import org.salespointframework.core.accountancy.payment.Cash;

import org.salespointframework.core.order.Order;
import org.salespointframework.core.order.OrderStatus;

import org.salespointframework.core.user.User;
import org.salespointframework.core.user.UserIdentifier;

@SuppressWarnings("javadoc")
public class OrderTest {

	private User user;
	@SuppressWarnings("rawtypes")
	private Order order;


	@Before
	public void before() {
		user = new User(new UserIdentifier(), "");
		order = new Order(user.getIdentifier(), Cash.CASH);
	}

	@Test
	public void orderStatusOpentest() {
		assertEquals(OrderStatus.OPEN, order.getOrderStatus());
	}

	@Test
	public void cancelOrderTest() {
		order.cancelOrder();
		assertEquals(OrderStatus.CANCELLED, order.getOrderStatus());
	}

	// FIXME
	/*
	 * @Test public void payOrderTest() { order.payOrder(); }
	 * 
	 * @Test public void completeOrderTest() { order.completeOrder(); }
	 */

}
