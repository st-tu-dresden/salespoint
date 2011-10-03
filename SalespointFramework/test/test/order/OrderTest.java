package test.order;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

import org.salespointframework.core.database.Database;
import org.salespointframework.core.order.Order;
import org.salespointframework.core.order.OrderStatus;
import org.salespointframework.core.order.PersistentOrder;
import org.salespointframework.core.user.PersistentUser;
import org.salespointframework.core.user.User;
import org.salespointframework.core.user.UserIdentifier;

@SuppressWarnings("javadoc")
public class OrderTest {

	private User user;
	@SuppressWarnings("rawtypes")
	private Order order;
	
	@BeforeClass
	public static void beforeClass() {
		Database.INSTANCE.initializeEntityManagerFactory("SalespointFramework");
	}
	
	@SuppressWarnings("deprecation")
	@Before
	public void before() {
		user = new PersistentUser(new UserIdentifier(), "");
		order = new PersistentOrder(user.getIdentifier());
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

	@Test
	public void payOrderTest() {
		order.payOrder();
	}
	
	@Test
	public void completeOrderTest() {
		order.completeOrder();
	}
	
}
