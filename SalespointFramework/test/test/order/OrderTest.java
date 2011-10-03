package test.order;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

import org.salespointframework.core.database.Database;
import org.salespointframework.core.order.OrderStatus;
import org.salespointframework.core.order.PersistentOrder;
import org.salespointframework.core.user.PersistentUser;
import org.salespointframework.core.user.UserIdentifier;

@SuppressWarnings("javadoc")
public class OrderTest {

	private PersistentUser user;
	
	@BeforeClass
	public static void beforeClass() {
		Database.INSTANCE.initializeEntityManagerFactory("SalespointFramework");
	}
	
	@Before
	public void before() {
		user = new PersistentUser(new UserIdentifier(), "");
	}

	@SuppressWarnings("deprecation")
	@Test
	public void orderStatusOpentest() {
		PersistentOrder order = new PersistentOrder(user.getIdentifier());
		assertEquals(OrderStatus.OPEN, order.getOrderStatus());
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void cancelOrderTest() {
		PersistentOrder order = new PersistentOrder(user.getIdentifier());
		order.cancelOrder();
		assertEquals(OrderStatus.CANCELLED, order.getOrderStatus());
	}

}
