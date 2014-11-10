package org.salespointframework.order;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.math.RoundingMode;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Test;
import org.salespointframework.catalog.Product;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.MetricMismatchException;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.quantity.RoundingStrategy;
import org.salespointframework.quantity.Units;

/**
 * Unit tests for {@link CartItem}.
 *
 * @author Paul Henke
 * @author Oliver Gierke
 */
public class CartItemUnitTests {

	static final Quantity quantity = Units.TEN;
	static final Product product = new Product("name", Money.of(CurrencyUnit.EUR, 1), Units.METRIC);

	/**
	 * @see #44
	 */
	@Test(expected = IllegalArgumentException.class)
	public void rejectsNullProduct() {
		new CartItem(null, quantity);
	}

	/**
	 * @see #44
	 */
	@Test(expected = IllegalArgumentException.class)
	public void rejectsNullQuantity() {
		new CartItem(product, null);
	}

	/**
	 * @see #44
	 */
	@Test(expected = MetricMismatchException.class)
	public void rejectsQuantityWithInvalidMetric() {
		new CartItem(product, new Quantity(0, new Metric("a", "b", "c"), RoundingStrategy.ROUND_ONE));
	}

	/**
	 * @see #44
	 */
	@Test
	public void returnsCorrectDetails() {

		CartItem item = new CartItem(product, quantity);

		assertThat(item.getIdentifier(), is(notNullValue()));
		assertThat(item.getProduct(), is(product));
		assertThat(item.getQuantity(), is(quantity));
		assertThat(item.getProductName(), is(product.getName()));
	}

	/**
	 * @see #44
	 */
	@Test
	public void calculatesPriceCorrectly() {

		CartItem item = new CartItem(product, quantity);

		assertThat(item.getPrice(), is(product.getPrice().multipliedBy(quantity.getAmount(), RoundingMode.HALF_UP)));
	}

	/**
	 * @see #44
	 */
	@Test
	public void createsOrderLineCorrectly() {

		OrderLine orderLine = new CartItem(product, quantity).toOrderLine();

		assertThat(orderLine, is(notNullValue()));
		assertThat(orderLine.getProductIdentifier(), is(product.getIdentifier()));
		assertThat(orderLine.getQuantity(), is(quantity));

	}

	/**
	 * @see #44
	 */
	@Test
	public void equals() {

		CartItem item = new CartItem(product, quantity);
		CartItem item2 = new CartItem(product, quantity);

		assertThat(item, is(item2));
		assertThat(item2, is(item));
		assertThat(item, is(not(product)));
	}
}
