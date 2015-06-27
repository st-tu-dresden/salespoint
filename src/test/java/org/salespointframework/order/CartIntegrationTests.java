package org.salespointframework.order;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.javamoney.moneta.Money;
import org.junit.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.catalog.Product;
import org.salespointframework.core.Currencies;
import org.salespointframework.quantity.Units;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Integration tests for {@link Cart}.
 * 
 * @author Paul Henke
 * @author Oliver Gierke
 */
public class CartIntegrationTests extends AbstractIntegrationTests {

	@Autowired UserAccountManager userAccountManager;

	/**
	 * @see #44
	 */
	@Test
	public void createsOrderFromCartCorrectly() {

		Cart cart = new Cart();
		CartItem cartItem = cart.addOrUpdateItem(//
				new Product("name", Money.of(1, Currencies.EURO), Units.METRIC), Units.TEN);

		Order order = new Order(userAccountManager.create("foobar", "barfoo"));
		cart.addItemsTo(order);

		assertThat(order.getOrderedLinesPrice(), is(cartItem.getPrice()));
		assertThat(order.getOrderLines(), is(iterableWithSize(1)));
	}
}
