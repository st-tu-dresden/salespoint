package org.salespointframework.order;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.accountancy.payment.Cash;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderCompletionResult;
import org.salespointframework.order.OrderManager;
import org.salespointframework.order.OrderStatus;
import org.salespointframework.order.OrderCompletionResult.OrderCompletionStatus;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * @author Hannes Weissbach
 * @author Paul Henke
 * @author Oliver Gierke
 */
public class OrderTests extends AbstractIntegrationTests {

	@Autowired UserAccountManager userAccountManager;
	@Autowired OrderManager<Order> orderManager;

	UserAccount user;
	Order order;

	private static int foobar = 0;

	@Before
	public void before() {
		
		user = userAccountManager.save(userAccountManager.create("OrderTests " + foobar, ""));
		order = new Order(user, Cash.CASH);
		foobar++;
	}

	@Test
	public void orderStatusOpentest() {
		assertEquals(OrderStatus.OPEN, order.getOrderStatus());
	}

	@Test
	public void cancelOrderTest() {
		boolean result = orderManager.cancelOrder(order);
		assertEquals(OrderStatus.CANCELLED, order.getOrderStatus());
		assertTrue(result);
	}

	@Test
	public void cancelOrderTest2() {
		orderManager.cancelOrder(order);
		boolean result = orderManager.cancelOrder(order);
		assertEquals(OrderStatus.CANCELLED, order.getOrderStatus());
		assertFalse(result);
	}

	@Test
	public void cancelOrderTest3() {
		orderManager.payOrder(order);
		boolean result = orderManager.cancelOrder(order);
		assertEquals(OrderStatus.PAYED, order.getOrderStatus());
		assertFalse(result);
	}

	@Test
	public void cancelOrderTest4() {
		orderManager.payOrder(order);
		orderManager.completeOrder(order);
		boolean result = orderManager.cancelOrder(order);
		assertEquals(OrderStatus.COMPLETED, order.getOrderStatus());
		assertFalse(result);
	}

	@Test
	public void payOrderTest() {
		boolean result = orderManager.payOrder(order);
		assertEquals(OrderStatus.PAYED, order.getOrderStatus());
		assertTrue(result);
	}

	@Test
	public void payOrderTest2() {
		orderManager.payOrder(order);
		boolean result = orderManager.payOrder(order);
		assertEquals(OrderStatus.PAYED, order.getOrderStatus());
		assertFalse(result);
	}

	@Test
	public void completeOrderTest() {
		orderManager.payOrder(order);
		OrderCompletionResult result = orderManager.completeOrder(order);
		assertEquals(OrderStatus.COMPLETED, order.getOrderStatus());
		assertEquals(OrderCompletionStatus.SUCCESSFUL, result.getStatus());
	}

	@Test
	public void completeOrderTest2() {
		OrderCompletionResult result = orderManager.completeOrder(order);
		assertEquals(OrderStatus.OPEN, order.getOrderStatus());
		assertEquals(OrderCompletionStatus.FAILED, result.getStatus());
	}

}
