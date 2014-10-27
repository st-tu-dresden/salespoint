package org.salespointframework.order;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.util.Assert;

/**
 * Abstraction of a shopping cart.
 *
 * @authow Paul Henke
 * @author Oliver Gierke
 */
public class Cart implements Iterable<OrderLine> {

	private final List<OrderLine> orderLines = new ArrayList<>();

	/**
	 * Adds the given {@link OrderLine} to the cart.
	 * 
	 * @param orderLine must not be {@literal null}.
	 * @return
	 */
	public boolean add(OrderLine orderLine) {

		Assert.notNull(orderLine, "OrderLine must not be null!");
		return orderLines.add(orderLine);
	}

	/**
	 * Removes all {@link OrderLine}s with the given {@link OrderLineIdentifier}.
	 * 
	 * @param orderLineIdentifier must not be {@literal null}.
	 * @return
	 */
	public boolean removeOrderLine(OrderLineIdentifier orderLineIdentifier) {

		Assert.notNull(orderLineIdentifier, "OrderLineIdentifier must not be null!");
		return orderLines.removeIf(item -> item.getIdentifier().equals(orderLineIdentifier));
	}

	/**
	 * Clears the cart.
	 */
	public void clear() {
		orderLines.clear();
	}

	/**
	 * Returns whether the cart is currently empty.
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return orderLines.isEmpty();
	}

	/**
	 * Returns the total price of the cart.
	 * 
	 * @return
	 */
	public Money getTotalPrice() {

		return orderLines.stream().//
				map(OrderLine::getPrice).//
				reduce((left, right) -> left.plus(right)).orElse(Money.zero(CurrencyUnit.EUR));
	}

	/**
	 * Turns the current state of the cart into an {@link Order}.
	 * 
	 * @param order must not be {@literal null}.
	 */
	public void toOrder(Order order) {

		Assert.notNull(order, "Order must not be null!");

		if (order.getOrderStatus() != OrderStatus.OPEN) {
			throw new IllegalArgumentException("OrderStatus must be OPEN");
		}

		orderLines.forEach(item -> order.add(item));
	}

	/* 
	 * (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<OrderLine> iterator() {
		return orderLines.iterator();
	}
}
