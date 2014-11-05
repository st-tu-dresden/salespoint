package org.salespointframework.order;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.*;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.catalog.Product;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.quantity.Units;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;

public class CartIntegrationTest extends AbstractIntegrationTests {

	@Autowired
	UserAccountManager userAccountManager;

	@Test
	public void toOrderFail() {
		Cart cart = new Cart();
		Product product = new Product("name", Money.of(CurrencyUnit.EUR, 1),
				Units.METRIC);
		Quantity quantity = Units.TEN;

		UserAccount userAccount = userAccountManager.create("foobar", "barfoo");
		CartItem cartItem = cart.add(product, quantity);
		Order order = new Order(userAccount);

		cart.toOrder(order);

		assertEquals(cartItem.getPrice(), order.getOrderedLinesPrice());
		assertThat(order.getOrderLines(), is(iterableWithSize(1)));
	}
}
