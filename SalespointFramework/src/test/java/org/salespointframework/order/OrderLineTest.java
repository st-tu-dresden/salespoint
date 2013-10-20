package org.salespointframework.order;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.catalog.Keks;
import org.salespointframework.core.accountancy.payment.Cash;
import org.salespointframework.core.catalog.Catalog;
import org.salespointframework.core.catalog.Product;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.order.Order;
import org.salespointframework.core.order.OrderLine;
import org.salespointframework.core.quantity.Units;
import org.salespointframework.core.user.User;
import org.salespointframework.core.useraccount.UserAccountIdentifier;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings({ "javadoc" })
public class OrderLineTest extends AbstractIntegrationTests {

	@Autowired
	private Catalog catalog;

	private static int keksCounter = 0;
	private User user;
	private Order order;
	private OrderLine orderLine;

	@Before
	public void before() {
		Product keks = new Keks("OrderLine Keks " + keksCounter++, Money.ZERO);

		catalog.add(keks);

		user = new User(new UserAccountIdentifier(), "");
		order = new Order(user.getIdentifier(), Cash.CASH);
		orderLine = new OrderLine(keks, Units.TEN);
	}

	@Test(expected = NullPointerException.class)
	public void nullTest() {
		order.addOrderLine(null);
	}

	@Test(expected = NullPointerException.class)
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
	 * @Test(expected=IllegalArgumentException.class)
	 * 
	 * @Ignore public void numberOrderedNegativeTest() { new
	 * PersistentOrderLine(new ProductIdentifier(), -1337); }
	 */
}
