package org.salespointframework.order;

import java.util.LinkedList;
import java.util.List;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.util.Assert;

public class Basket {

	private final List<OrderLine> orderLines = new LinkedList<>();

	public boolean add(OrderLine orderLine) {

		Assert.notNull(orderLine, "orderLine must not be null");
		return orderLines.add(orderLine);
	}

	public boolean removeOrderLine(OrderLineIdentifier orderLineIdentifier) {

		Assert.notNull(orderLineIdentifier, "orderLineIdentifier must not be null");

		OrderLine temp = null;
		for (OrderLine pol : orderLines) {
			if (pol.getIdentifier().equals(orderLineIdentifier)) {
				temp = pol;
				break;
			}
		}
		return orderLines.remove(temp);
	}

	public void clear() {
		orderLines.clear();
	}

	public Iterable<OrderLine> getOrderLines() {
		return orderLines;
	}

	public boolean isEmpty() {
		return orderLines.isEmpty();
	}

	public Money getTotalPrice() {

		return orderLines.stream().//
				map(OrderLine::getPrice).//
				reduce((left, right) -> left.plus(right)).orElse(Money.zero(CurrencyUnit.EUR));
	}

	public void commit(Order order) {

		Assert.notNull(order, "order must not be null");

		if (order.getOrderStatus() != OrderStatus.OPEN) {
			throw new IllegalArgumentException("OrderStatus must be OPEN");
		}

		for (OrderLine orderLine : orderLines) {
			order.add(orderLine);
		}
	}
}
