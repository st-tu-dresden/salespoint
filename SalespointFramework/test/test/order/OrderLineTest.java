package test.order;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.salespointframework.core.accountancy.payment.Cash;
import org.salespointframework.core.catalog.PersistentCatalog;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.order.Order;
import org.salespointframework.core.order.OrderLine;
import org.salespointframework.core.order.PersistentOrder;
import org.salespointframework.core.product.PersistentProduct;
import org.salespointframework.core.user.PersistentUser;
import org.salespointframework.core.user.User;
import org.salespointframework.core.user.UserIdentifier;

import test.product.Keks;


@SuppressWarnings({"javadoc", "unchecked"})
public class OrderLineTest {

	private static int keksCounter = 0;
	private User user;
	@SuppressWarnings("rawtypes")
	private Order order;
	private OrderLine orderLine;
	
	@BeforeClass
	public static void beforeClass() {
		Database.INSTANCE.initializeEntityManagerFactory("SalespointFramework");
	}
	

	@Before
	public void before() {
		PersistentProduct keks = new Keks("OrderLine Keks " + keksCounter++, Money.ZERO);
		
		PersistentCatalog catalog = new PersistentCatalog();
		catalog.add(keks);
		
		user = new PersistentUser(new UserIdentifier(), "");
		order = new PersistentOrder(user.getIdentifier(), Cash.CASH);	
		orderLine = null; // new PersistentOrderLine(keks.getIdentifier());
	}
	

	@Test(expected=NullPointerException.class)
	public void nullTest() {
		order.addOrderLine(null);
	}
	
	@Test(expected=NullPointerException.class)
	public void nullTest2() {
		order.removeOrderLine(null);
	}
	
	@Test
	public void addTest() {
		assertTrue(order.addOrderLine(orderLine));
	}
	
	@Test
	public void addTest2() {
		order.addOrderLine(orderLine);
		assertFalse(order.addOrderLine(orderLine));
	}
	
	@Test
	public void removeTest() {
		order.addOrderLine(orderLine);
		assertTrue(order.removeOrderLine(orderLine.getIdentifier()));
	}
	
	@Test
	public void removeTest2() {
		assertFalse(order.removeOrderLine(orderLine.getIdentifier()));
	}
	
	/*
	@Test(expected=IllegalArgumentException.class)
	@Ignore
	public void numberOrderedNegativeTest() {
		new PersistentOrderLine(new ProductIdentifier(), -1337);
	}
	*/
}
