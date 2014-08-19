package org.salespointframework.order;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.payment.Cash;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Integration tests for {@link OrderManager}.
 * 
 * @author Hannes Weissbach
 * @author Paul Henke
 * @author Oliver Gierke
 */
public class OrderManagerTests extends AbstractIntegrationTests {

	@Autowired UserAccountManager userAccountManager;

	@Autowired OrderManager<Order> orderManager;

	UserAccount user;
	Order order;

	@Before
	public void before() {
		user = userAccountManager.create("userId", "password");
		userAccountManager.save(user);
		order = new Order(user, Cash.CASH);
	}

	@Test(expected = IllegalArgumentException.class)
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

		order = orderManager.add(order);

		Optional<Order> result = orderManager.get(order.getIdentifier());

		assertThat(result.isPresent(), is(true));
		assertThat(result.get(), is(order));
	}
}
