package org.salespointframework.order;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.*;

import java.util.Optional;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Test;
import org.salespointframework.catalog.Product;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.quantity.Units;

public class CartTest {

	private Cart cart;

	Product product = new Product("name", Money.of(CurrencyUnit.EUR, 1),
			Units.METRIC);
	final Quantity quantity = Units.TEN;

	@Before
	public void setUp() {
		cart = new Cart();
	}

	@Test
	public void add() {
		CartItem cartItem = cart.add(product, quantity);
		assertNotNull(cartItem);
		assertEquals(product, cartItem.getProduct());
		assertEquals(quantity, cartItem.getQuantity());
		assertThat(cart, is(iterableWithSize(1)));
		assertThat(cart, hasItem(cartItem));
	}

	@Test(expected = IllegalArgumentException.class)
	public void addFail() {
		cart.add(product, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void addFail2() {
		cart.add(null, quantity);
	}

	@Test(expected = IllegalArgumentException.class)
	public void removeFail() {
		cart.remove(null);
	}

	@Test
	public void remove() {
		CartItem item = cart.add(product, quantity);
		Optional<CartItem> optionalItem = cart.remove(item.getIdentifier());
		assertTrue(optionalItem.isPresent());
		assertThat(cart, is(iterableWithSize(0)));
	}

	@Test
	public void get() {
		CartItem item = cart.add(product, quantity);
		Optional<CartItem> optionalItem = cart.get(item.getIdentifier());
		assertTrue(optionalItem.isPresent());
		assertEquals(item, optionalItem.get());
	}

	@Test(expected = IllegalArgumentException.class)
	public void getFail() {
		cart.get(null);
	}

	@Test
	public void clear() {
		cart.add(product, quantity);
		cart.clear();
		assertThat(cart, is(iterableWithSize(0)));
	}

	@Test
	public void isEmpty() {
		assertTrue(cart.isEmpty());
		cart.add(product, quantity);
		assertFalse(cart.isEmpty());
	}

	@Test(expected = IllegalArgumentException.class)
	public void toOrderFail() {
		cart.toOrder(null);
	}

}
