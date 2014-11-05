package org.salespointframework.order;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

public class CartItemTest {

	Product product = new Product("name", Money.of(CurrencyUnit.EUR, 1),
			Units.METRIC);
	final Quantity quantity = Units.TEN;

	@Test(expected = IllegalArgumentException.class)
	public void ctorFail() {
		new CartItem(null, quantity);
	}

	@Test(expected = IllegalArgumentException.class)
	public void ctorFail2() {
		new CartItem(product, null);
	}

	@Test(expected = MetricMismatchException.class)
	public void ctorFail3() {
		Quantity tempQuantity = new Quantity(0, new Metric("a", "b", "c"),
				RoundingStrategy.ROUND_ONE);
		new CartItem(product, tempQuantity);
	}

	@Test
	public void getter() {
		final Money price = product.getPrice().multipliedBy(
				quantity.getAmount(), RoundingMode.HALF_UP);

		CartItem item = new CartItem(product, quantity);

		assertNotNull(item.getIdentifier());
		assertEquals(product, item.getProduct());
		assertEquals(quantity, item.getQuantity());
		assertEquals(product.getName(), item.getProductName());
		assertEquals(price, item.getPrice());
	}

	@Test
	public void toOrderLine() {
		CartItem item = new CartItem(product, quantity);
		OrderLine orderLine = item.toOrderline();
		assertNotNull(orderLine);
		assertEquals(product.getIdentifier(), orderLine.getProductIdentifier());
		assertEquals(quantity, orderLine.getQuantity());
	}

	@Test
	public void equals() {
		CartItem item = new CartItem(product, quantity);
		CartItem item2 = new CartItem(product, quantity);

		assertEquals(false, item.equals(null));
		assertEquals(true, item.equals(item));
		assertEquals(false, item.equals(product));
		assertEquals(true, item.equals(item2));
	}
}
