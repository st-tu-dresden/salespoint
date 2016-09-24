package org.salespointframework.order;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.javamoney.moneta.Money;
import org.junit.Test;
import org.salespointframework.catalog.Product;
import org.salespointframework.core.Currencies;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.MetricMismatchException;
import org.salespointframework.quantity.Quantity;

/**
 * Unit tests for {@link CartItem}.
 *
 * @author Paul Henke
 * @author Oliver Gierke
 */
public class CartItemUnitTests {

	static final Quantity QUANTITY = Quantity.of(10);
	static final Product PRODUCT = new Product("name", Money.of(1, Currencies.EURO));

	/**
	 * @see #44
	 */
	@Test(expected = IllegalArgumentException.class)
	public void rejectsNullProduct() {
		new CartItem(null, QUANTITY);
	}

	/**
	 * @see #44
	 */
	@Test(expected = IllegalArgumentException.class)
	public void rejectsNullQuantity() {
		new CartItem(PRODUCT, null);
	}

	/**
	 * @see #44
	 */
	@Test(expected = MetricMismatchException.class)
	public void rejectsQuantityWithInvalidMetric() {
		new CartItem(PRODUCT, Quantity.of(0, Metric.KILOGRAM));
	}

	/**
	 * @see #44
	 */
	@Test
	public void returnsCorrectDetails() {

		CartItem item = new CartItem(PRODUCT, QUANTITY);

		assertThat(item.getId(), is(notNullValue()));
		assertThat(item.getProduct(), is(PRODUCT));
		assertThat(item.getQuantity(), is(QUANTITY));
		assertThat(item.getProductName(), is(PRODUCT.getName()));
	}

	/**
	 * @see #44
	 */
	@Test
	public void calculatesPriceCorrectly() {

		CartItem item = new CartItem(PRODUCT, QUANTITY);

		assertThat(item.getPrice(), is(PRODUCT.getPrice().multiply(QUANTITY.getAmount())));
	}

	/**
	 * @see #44
	 */
	@Test
	public void createsOrderLineCorrectly() {

		OrderLine orderLine = new CartItem(PRODUCT, QUANTITY).toOrderLine();

		assertThat(orderLine, is(notNullValue()));
		assertThat(orderLine.getProductIdentifier(), is(PRODUCT.getId()));
		assertThat(orderLine.getQuantity(), is(QUANTITY));
	}
}
