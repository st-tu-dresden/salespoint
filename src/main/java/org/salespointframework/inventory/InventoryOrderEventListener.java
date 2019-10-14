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
import java.util.stream.Stream;

import org.salespointframework.catalog.Product;
import org.salespointframework.order.Order;
import org.salespointframework.order.Order.OrderCancelled;
import org.salespointframework.order.Order.OrderCompleted;
import org.salespointframework.order.OrderCompletionFailure;
import org.salespointframework.order.OrderCompletionReport;
import org.salespointframework.order.OrderCompletionReport.OrderLineCompletion;
import org.salespointframework.order.OrderLine;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.data.util.Optionals;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * {@link ApplicationListener} for {@link OrderCompleted} events to verify that sufficient amounts of the
 * {@link Product} the {@link OrderLine}s contained in the {@link Order} point to are available in the
 * {@link UniqueInventory}.
 *
 * @author Oliver Gierke
 * @since 6.3
 */
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "salespoint.inventory.disable-updates", havingValue = "false", matchIfMissing = true)
public class InventoryOrderEventListener {

	private static final String NOT_ENOUGH_STOCK = "Number of items requested by the OrderLine is greater than the number available in the Inventory. Please re-stock.";
	private static final String NO_INVENTORY_ITEM = "No inventory item with given product indentifier found in inventory. Have you initialized your inventory? Do you need to re-stock it?";

	private final @NonNull List<LineItemFilter> filters;

	private final @NonNull MultiInventory<MultiInventoryItem> inventory;
	private final @NonNull UniqueInventory<UniqueInventoryItem> uniqueInventory;

	/**
	 * Invokes {@link UniqueInventory} checks for all {@link OrderLine} of the {@link Order} in the given
	 * {@link OrderCompleted} event.
	 *
	 * @param event must not be {@literal null}.
	 * @throws OrderCompletionFailure in case any of the {@link OrderLine} items contained in the order and supported by
	 *           the configured {@link LineItemFilter} is not available in sufficient quantity.
	 */
	@EventListener
	public void on(OrderCompleted event) throws OrderCompletionFailure {

		Assert.notNull(event, "OrderCompletedEvent must not be null!");

		var order = event.getOrder();
		var collect = order.getOrderLines() //
				.map(this::verify)//
				.toList();

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

		var order = event.getOrder();

		if (!order.isCompleted()) {
			return;
		}

		order.getOrderLines() //
				.flatMap(this::updateStockFor) //
				.forEach(uniqueInventory::save);
	}

	/**
	 * Verifies the given {@link OrderLine} for sufficient stock in the {@link UniqueInventory}.
	 *
	 * @param orderLine must not be {@literal null}.
	 * @return
	 */
	private OrderLineCompletion verify(OrderLine orderLine) {

		Assert.notNull(orderLine, "OrderLine must not be null!");

		if (!LineItemFilter.shouldBeHandled(orderLine, filters)) {
			return OrderLineCompletion.success(orderLine);
		}

		var identifier = orderLine.getProductIdentifier();
		var item = uniqueInventory.findByProductIdentifier(identifier);

		return item.map(it -> verifyAndUpdateUnique(it, orderLine)) //
				.orElseGet(() -> assertAtLeastOneExists(orderLine));
	}

	/**
	 * Verifies that the given UI
	 *
	 * @param item
	 * @param orderLine
	 * @return
	 */
	private OrderLineCompletion verifyAndUpdateUnique(UniqueInventoryItem item, OrderLine orderLine) {
		return hasSufficientQuantity(item, orderLine)
				.onSuccess(it -> uniqueInventory.save(item.decreaseQuantity(it.getQuantity())));
	}

	/**
	 * Creates a new {@link OrderLineCompletion} verifying that at least one {@link MultiInventoryItem} exists.
	 *
	 * @param orderLine must not be {@literal null}.
	 * @return
	 */
	private OrderLineCompletion assertAtLeastOneExists(OrderLine orderLine) {

		var items = inventory.findByProductIdentifier(orderLine.getProductIdentifier());

		return items.isEmpty() ? error(orderLine, NO_INVENTORY_ITEM) : skipped(orderLine);
	}

	private Stream<UniqueInventoryItem> updateStockFor(OrderLine orderLine) {

		var productIdentifier = orderLine.getProductIdentifier();
		var item = uniqueInventory.findByProductIdentifier(productIdentifier)
				.map(it -> it.increaseQuantity(orderLine.getQuantity()));

		if (!item.isPresent() && inventory.findByProductIdentifier(productIdentifier).isEmpty()) {
			throw new IllegalArgumentException(
					String.format("Couldn't find InventoryItem for product %s!", productIdentifier));
		}

		return Optionals.toStream(item);
	}

	private static OrderLineCompletion hasSufficientQuantity(InventoryItem<?> item, OrderLine orderLine) {

		return item.hasSufficientQuantity(orderLine.getQuantity()) //
				? success(orderLine) //
				: error(orderLine, NOT_ENOUGH_STOCK);
	}
}
