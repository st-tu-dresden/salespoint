package test.order;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.salespointframework.core.accountancy.payment.Cash;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.order.PersistentOrder;
import org.salespointframework.core.order.PersistentOrderManager;
import org.salespointframework.core.user.PersistentUser;
import org.salespointframework.core.user.User;
import org.salespointframework.core.user.UserIdentifier;


@SuppressWarnings("javadoc")
public class OrderManagerTest {
	
	private final PersistentOrderManager orderManager = new PersistentOrderManager();
	private User user;
	private PersistentOrder order;
	
	@BeforeClass
	public static void beforeClass() {
		Database.INSTANCE.initializeEntityManagerFactory("SalespointFramework");
	}
	

	@Before
	public void before() {
		user = new PersistentUser(new UserIdentifier(), "");
		order = new PersistentOrder(user.getIdentifier(), Cash.CASH);
	}

	@Test(expected=NullPointerException.class)
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
		assertEquals(order, orderManager.get(PersistentOrder.class, order.getIdentifier()));
	}
}
