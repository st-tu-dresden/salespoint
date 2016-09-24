package org.salespointframework.order;

import lombok.EqualsAndHashCode;
import lombok.Value;

import org.salespointframework.order.Order.OrderCompleted;
import org.springframework.util.Assert;

/**
 * Exception to be thrown if a listener for {@link OrderCompleted} event wants to indicate failure of the completion.
 * 
 * @author Oliver Gierke
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class OrderCompletionFailure extends RuntimeException {

	private static final long serialVersionUID = 1405708816840121734L;

	Order order;
	OrderCompletionReport report;

	/**
	 * Creates a new {@link OrderCompletionFailure} for the given {@link Order} and message.
	 * 
	 * @param order must not be {@literal null}.
	 * @param message must not be {@literal null}.
	 */
	public OrderCompletionFailure(Order order, String message) {

		super(message);

		this.order = order;
		this.report = OrderCompletionReport.failed(order);
	}

	/**
	 * Creates a new {@link OrderCompletionFailure} for the given {@link OrderCompletionReport}.
	 * 
	 * @param report must not be {@literal null}.
	 */
	public OrderCompletionFailure(OrderCompletionReport report) {

		Assert.notNull(report, "OrderCompletionReport must not be null!");
		Assert.isTrue(report.hasErrors(), "OrderCompletionReport must contain errors!");

		this.order = report.getOrder();
		this.report = report;
	}
}
