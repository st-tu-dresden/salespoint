package org.salespointframework.order;

import static org.junit.Assert.*;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.catalog.Cookie;
import org.salespointframework.catalog.Product;
import org.salespointframework.payment.Cash;
import org.salespointframework.quantity.Units;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderLineTests extends AbstractIntegrationTests {

	@Autowired UserAccountManager userAccountManager;
	@Autowired Catalog<Product> catalog;

	private static int keksCounter = 0;

	private UserAccount user;
	private Order order;
	private OrderLine orderLine;

	@Before
	public void before() {
		Product keks = new Cookie("OrderLine Cookie " + keksCounter++, Money.zero(CurrencyUnit.EUR));

		catalog.save(keks);

		user = userAccountManager.create("userId", "password");
		order = new Order(user, Cash.CASH);
		orderLine = new OrderLine(keks, Units.TEN);
	}

	@Test(expected = IllegalArgumentException.class)
	public void nullTest() {
		order.add((OrderLine) null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void nullTest2() {
		order.remove((OrderLine) null);
	}

	@Test
	public void addTest() {
		assertTrue(order.add(orderLine));
	}

	@Test
	public void addTest2() {
		order.add(orderLine);
		assertFalse(order.add(orderLine));
	}

	@Test
	public void removeTest() {
		order.add(orderLine);
		assertTrue(order.remove(orderLine));
	}

	@Test
	public void removeTest2() {
		assertFalse(order.remove(orderLine));
	}
}
