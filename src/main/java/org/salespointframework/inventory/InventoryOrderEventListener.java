/*
 * Copyright 2017-2019 the original author or authors.
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
package org.salespointframework.inventory;

import static org.salespointframework.order.OrderCompletionReport.OrderLineCompletion.*;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.salespointframework.catalog.Product;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.order.Order;
import org.salespointframework.order.Order.OrderCancelled;
import org.salespointframework.order.Order.OrderCompleted;
import org.salespointframework.order.OrderCompletionFailure;
import org.salespointframework.order.OrderCompletionReport;
import org.salespointframework.order.OrderCompletionReport.OrderLineCompletion;
import org.salespointframework.order.OrderLine;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * {@link ApplicationListener} for {@link OrderCompleted} events to verify that sufficient amounts of the
 * {@link Product} the {@link OrderLine}s contained in the {@link Order} point to are available in the
 * {@link Inventory}.
 * 
 * @author Oliver Gierke
 * @since 6.3
 */
@Component
@RequiredArgsConstructor
public class InventoryOrderEventListener {

	private static final String NOT_ENOUGH_STOCK = "Number of items requested by the OrderLine is greater than the number available in the Inventory. Please re-stock.";
	private static final String NO_INVENTORY_ITEM = "No inventory item with given product indentifier found in inventory. Have you initialized your inventory? Do you need to re-stock it?";

	private final @NonNull Inventory<InventoryItem> inventory;
	private final @NonNull List<LineItemFilter> filters;

	/**
	 * Invokes {@link Inventory} checks for all {@link OrderLine} of the {@link Order} in the given {@link OrderCompleted}
	 * event.
	 * 
	 * @param event must not be {@literal null}.
	 * @throws OrderCompletionFailure in case any of the {@link OrderLine} items contained in the order and supported by
	 *           the configured {@link LineItemFilter} is not available in sufficient quantity.
	 */
	@EventListener
	public void on(OrderCompleted event) throws OrderCompletionFailure {

		Assert.notNull(event, "OrderCompletedEvent must not be null!");

		Order order = event.getOrder();

		List<OrderLineCompletion> collect = order.getOrderLines().stream()//
				.map(this::verify)//
				.collect(Collectors.toList());

		OrderCompletionReport.forCompletions(order, collect) //
				.onError(OrderCompletionFailure::new);
	}

	/**
	 * Rolls back the stock decreases handled for {@link OrderCompleted} events.
	 * 
	 * @param event must not be {@literal null}.
	 */
	@EventListener
	public void on(OrderCancelled event) {

		Order order = event.getOrder();

		if (!order.isCompleted()) {
			return;
		}

		order.getOrderLines() //
				.map(this::updateStockFor) //
				.forEach(inventory::save);
	}

	/**
	 * Verifies the given {@link OrderLine} for sufficient stock in the {@link Inventory}.
	 * 
	 * @param orderLine must not be {@literal null}.
	 * @return
	 */
	private OrderLineCompletion verify(OrderLine orderLine) {

		Assert.notNull(orderLine, "OrderLine must not be null!");

		if (!LineItemFilter.shouldBeHandled(orderLine, filters)) {
			return OrderLineCompletion.success(orderLine);
		}

		ProductIdentifier identifier = orderLine.getProductIdentifier();
		Optional<InventoryItem> inventoryItem = inventory.findByProductIdentifier(identifier);

		return inventoryItem //
				.map(it -> it.hasSufficientQuantity(orderLine.getQuantity())) //
				.map(sufficient -> sufficient ? success(orderLine) : error(orderLine, NOT_ENOUGH_STOCK)) //
				.orElse(error(orderLine, NO_INVENTORY_ITEM)) //
				.onSuccess(it -> {

					inventoryItem //
							.map(item -> item.decreaseQuantity(it.getQuantity())) //
							.ifPresent(inventory::save);
				});
	}

	private InventoryItem updateStockFor(OrderLine orderLine) {

		ProductIdentifier productIdentifier = orderLine.getProductIdentifier();

		return inventory.findByProductIdentifier(productIdentifier) //
				.orElseThrow(() -> new IllegalArgumentException(
						String.format("Couldn't find InventoryItem for product %s!", productIdentifier))) //
				.increaseQuantity(orderLine.getQuantity());
	}
}
