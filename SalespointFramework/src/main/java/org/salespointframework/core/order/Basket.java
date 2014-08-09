package org.salespointframework.core.order;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.salespointframework.util.Iterables;

public class Basket {
	
	private final List<OrderLine> orderLines = new LinkedList<>();
	
	public boolean addOrderLine(OrderLine orderLine) {
		Objects.requireNonNull(orderLine, "orderLine must not be null");
		return orderLines.add(orderLine);
	}
	
	public boolean removeOrderLine(OrderLineIdentifier orderLineIdentifier) {
		Objects.requireNonNull(orderLineIdentifier,	"orderLineIdentifier must not be null");

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
		return Iterables.of(orderLines);
	}
	
	public boolean isEmpty() {
		return orderLines.isEmpty();
	}
	
	public Money getPrice() {
		Money price = Money.zero(CurrencyUnit.EUR);
		for (OrderLine orderLine : orderLines) {
			price = price.plus(orderLine.getPrice());
		}
		return price;
	}
	
	public void commit(Order order) {
		Objects.requireNonNull(order, "order must not be null");
		if(order.getOrderStatus() != OrderStatus.OPEN) throw new IllegalArgumentException("OrderStatus must be OPEN");
		for(OrderLine orderLine : orderLines) {
			order.addOrderLine(orderLine);
		}
	}
	
	
}
