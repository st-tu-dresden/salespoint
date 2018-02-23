/*
 * Copyright 2017-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

		super("Order completion failed! ".concat(report.toString()));

		Assert.notNull(report, "OrderCompletionReport must not be null!");
		Assert.isTrue(report.hasErrors(), "OrderCompletionReport must contain errors!");

		this.order = report.getOrder();
		this.report = report;
	}
}
