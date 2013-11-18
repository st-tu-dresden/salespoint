package org.salespointframework.core.order;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.core.accountancy.payment.Cash;
import org.salespointframework.core.order.Order;
import org.salespointframework.core.order.OrderStatus;
import org.salespointframework.core.useraccount.UserAccount;
import org.salespointframework.core.useraccount.UserAccountIdentifier;
import org.salespointframework.core.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("javadoc")
public class OrderTest extends AbstractIntegrationTests {

	
	@Autowired
	private UserAccountManager userAccountManager;
	
	private UserAccount user;
	private Order order;


	@Before
	public void before() {
		user = userAccountManager.create(new UserAccountIdentifier(), "");
		order = new Order(user, Cash.CASH);
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
