package test.order;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.salespointframework.core.catalog.PersistentCatalog;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.order.PersistentOrder;
import org.salespointframework.core.order.PersistentOrderLine;
import org.salespointframework.core.user.PersistentUser;
import org.salespointframework.core.user.UserIdentifier;
import org.salespointframework.util.ArgumentNullException;

import test.product.KeksType;

@SuppressWarnings("javadoc")
public class OrderLineTest {

	private PersistentUser user;
	private PersistentOrder order;
	private KeksType keksType;
	private PersistentOrderLine orderLine;
	
	@BeforeClass
	public static void beforeClass() {
		Database.INSTANCE.initializeEntityManagerFactory("SalespointFramework");
	}
	
	@SuppressWarnings("deprecation")
	@Before
	public void before() {
		keksType = new KeksType("OrderLine Keks", Money.ZERO);
		
		PersistentCatalog catalog = new PersistentCatalog();
		catalog.add(keksType);
		
		user = new PersistentUser(new UserIdentifier(), "");
		order = new PersistentOrder(user.getIdentifier());	//TODO
		orderLine = new PersistentOrderLine(keksType.getIdentifier());
	}
	
	@Test(expected=ArgumentNullException.class)
	public void nullTest() {
		order.addOrderLine(null);
	}
	
	@Test(expected=ArgumentNullException.class)
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
}
