package org.salespointframework.order;

import java.util.Optional;
import static org.hamcrest.CoreMatchers.is;
import org.javamoney.moneta.Money;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.catalog.Cookie;
import org.salespointframework.catalog.Product;
import org.salespointframework.core.Currencies;
import org.salespointframework.inventory.Inventory;
import org.salespointframework.inventory.InventoryItem;
import org.salespointframework.order.OrderCompletionResult.OrderCompletionStatus;
import org.salespointframework.payment.Cash;
import org.salespointframework.quantity.Quantity;
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

	@Autowired Catalog<Product> catalog;
	@Autowired Inventory<InventoryItem> inventory;

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
		orderManager.save(null);
	}

	@Test
	public void addTest() {
		orderManager.save(order);
	}

	@Test
	public void testContains() {
		orderManager.save(order);
		assertTrue(orderManager.contains(order.getIdentifier()));
	}

	@Test
	public void testGet() {

		order = orderManager.save(order);

		Optional<Order> result = orderManager.get(order.getIdentifier());

		assertThat(result.isPresent(), is(true));
		assertThat(result.get(), is(order));
	}

	/**
	 * @see #38
	 */
	@Test
	public void completesOrderIfAllLineItemsAreAvailableInSufficientQuantity() {

		Cookie cookie = catalog.save(new Cookie("Double choc", Money.of(1.2, Currencies.EURO)));
		inventory.save(new InventoryItem(cookie, Quantity.of(100)));
		order.add(new OrderLine(cookie, Quantity.of(10)));

		orderManager.payOrder(order);
		OrderCompletionResult result = orderManager.completeOrder(order);

		assertThat(result.getStatus(), is(OrderCompletionStatus.SUCCESSFUL));
	}

	/**
	 * @see #38
	 */
	@Test
	public void failsOrderCompletionIfLineItemsAreNotAvailableInSufficientQuantity() {

		Cookie cookie = catalog.save(new Cookie("Double choc", Money.of(1.2, Currencies.EURO)));
		inventory.save(new InventoryItem(cookie, Quantity.of(1)));
		order.add(new OrderLine(cookie, Quantity.of(10)));

		orderManager.payOrder(order);
		OrderCompletionResult result = orderManager.completeOrder(order);

		assertThat(result.getStatus(), is(OrderCompletionStatus.FAILED));
	}
}