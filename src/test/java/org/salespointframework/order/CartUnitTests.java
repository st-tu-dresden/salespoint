package org.salespointframework.order;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Optional;

import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.salespointframework.catalog.Product;
import org.salespointframework.core.Currencies;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.quantity.Units;

/**
 * Unit tests for {@link Cart}.
 * 
 * @author Paul Henke
 * @author Oliver Gierke
 */
public class CartUnitTests {

	static final Quantity quantity = Units.TEN;
	static final Product product = new Product("name", Money.of(1, Currencies.EURO), Units.METRIC);

	Cart cart;

	@Before
	public void setUp() {
		cart = new Cart();
	}

	/**
	 * @see #44
	 */
	@Test
	public void addsCartItemCorrectly() {

		CartItem reference = cart.addOrUpdateItem(product, quantity);

		assertThat(cart, hasItem(reference));
		assertThat(cart, is(iterableWithSize(1)));
	}

	/**
	 * @see #44
	 */
	@Test(expected = IllegalArgumentException.class)
	public void rejectsNullQuantityOnAdding() {
		cart.addOrUpdateItem(product, null);
	}

	/**
	 * @see #44
	 */
	@Test(expected = IllegalArgumentException.class)
	public void rejectsNullProductOnAdding() {
		cart.addOrUpdateItem(null, quantity);
	}

	/**
	 * @see #44
	 */
	@Test(expected = IllegalArgumentException.class)
	public void rejectsNullOnRemovingAnItem() {
		cart.removeItem(null);
	}

	/**
	 * @see #44
	 */
	@Test
	public void removesItemsCorrectly() {

		CartItem reference = cart.addOrUpdateItem(product, quantity);

		assertThat(cart.removeItem(reference.getIdentifier()).isPresent(), is(true));
		assertThat(cart, is(iterableWithSize(0)));
	}

	/**
	 * @see #44
	 */
	@Test
	public void providesAccessToCartItem() {

		CartItem reference = cart.addOrUpdateItem(product, quantity);
		Optional<CartItem> item = cart.getItem(reference.getIdentifier());

		assertThat(item.isPresent(), is(true));
		assertThat(item.get(), is(reference));
	}

	/**
	 * @see #44
	 */
	@Test
	public void returnsEmptyOptionalForNonExistingIdentifier() {

		cart.addOrUpdateItem(product, quantity);

		assertThat(cart.getItem("foobar"), is(Optional.empty()));
	}

	/**
	 * @see #44
	 */
	@Test(expected = IllegalArgumentException.class)
	public void rejectsNullIdentifier() {
		cart.getItem(null);
	}

	/**
	 * @see #44
	 */
	@Test
	public void clearsCartCorrectly() {

		cart.addOrUpdateItem(product, quantity);
		cart.clear();

		assertThat(cart, is(iterableWithSize(0)));
		assertThat(cart.isEmpty(), is(true));
	}

	/**
	 * @see #44
	 */
	@Test
	public void isEmpty() {

		assertThat(cart.isEmpty(), is(true));

		cart.addOrUpdateItem(product, quantity);
		assertThat(cart.isEmpty(), is(false));
	}

	/**
	 * @see #44
	 */
	@Test(expected = IllegalArgumentException.class)
	public void toOrderFail() {
		cart.addItemsTo(null);
	}

	/**
	 * @see #44
	 */
	@Test
	public void updatesCartItemIfOneForProductAlreadyExists() {

		CartItem item = cart.addOrUpdateItem(product, quantity);

		assertThat(item.getProduct(), is(product));
		assertThat(item.getQuantity(), is(quantity));
		assertThat(cart, is(iterableWithSize(1)));

		CartItem updated = cart.addOrUpdateItem(product, quantity);

		assertThat(updated, is(not(item)));
		assertThat(cart, is(iterableWithSize(1)));
		assertThat(updated.getProduct(), is(product));
		assertThat(updated.getQuantity(), is(quantity.add(quantity)));
	}

}
