package org.salespointframework.order;

import static org.junit.Assert.*;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.accountancy.payment.Cash;
import org.salespointframework.catalog.Cookie;
import org.salespointframework.catalog.Product;
import org.salespointframework.catalog.Products;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderLine;
import org.salespointframework.quantity.Units;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;


public class OrderLineTests extends AbstractIntegrationTests {

	
	@Autowired
	private UserAccountManager userAccountManager;
	
	@Autowired
	private Products<Product> catalog;

	private static int keksCounter = 0;
	private UserAccount user;
	private Order order;
	private OrderLine orderLine;

	@Before
	public void before() {
		Product keks = new Cookie("OrderLine Cookie " + keksCounter++, Money.zero(CurrencyUnit.EUR));

		catalog.save(keks);

		user = userAccountManager.create("userId", "");
		order = new Order(user, Cash.CASH);
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
}
