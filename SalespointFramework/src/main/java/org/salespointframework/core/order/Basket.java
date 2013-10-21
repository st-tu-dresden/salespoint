package org.salespointframework.core.order;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Basket {
	
	private List<OrderLine> orderLines = new LinkedList<>();
	
	public boolean add(OrderLine orderLine) {
		Objects.requireNonNull(orderLine, "orderLine must not be null");
		return orderLines.add(orderLine);
	}
	
	public boolean remove(OrderLineIdentifier orderLineIdentifier) {
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
	
	public void commit(Order order) {
		Objects.requireNonNull(order, "order must not be null");
		if(order.getOrderStatus() != OrderStatus.OPEN) throw new IllegalArgumentException("OrderStatus must be OPEN");
		for(OrderLine orderLine : orderLines) {
			order.addOrderLine(orderLine);
		}
	}
	
	
}
