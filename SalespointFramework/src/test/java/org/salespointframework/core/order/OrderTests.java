package org.salespointframework.core.order;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.core.accountancy.payment.Cash;
import org.salespointframework.core.order.OrderCompletionResult.OrderCompletionStatus;
import org.salespointframework.core.useraccount.UserAccount;
import org.salespointframework.core.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("javadoc")
public class OrderTests extends AbstractIntegrationTests {

	
	@Autowired
	private UserAccountManager userAccountManager;
	
	@Autowired
	private OrderManager orderManager;
	
	private UserAccount user;
	private Order order;


	private static int foobar = 0;
	
	@Before
	public void before() {
		user = userAccountManager.create("OrderTests "+foobar, "");
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
