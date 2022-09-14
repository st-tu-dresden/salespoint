/*
 * Copyright 2020-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
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

import org.salespointframework.order.Order;
import org.salespointframework.order.OrderCompletionFailure;
import org.salespointframework.order.OrderCompletionReport;
import org.salespointframework.order.OrderCompletionReport.OrderLineCompletion;
import org.salespointframework.order.OrderLine;
import org.springframework.data.util.Optionals;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * A simple facade to trigger updates to the inventory implementations.
 *
 * @author Oliver Drotbohm
 * @since 7.3
 */
@Service
@RequiredArgsConstructor
class InventoryManagement {

	private static final String NOT_ENOUGH_STOCK = "Number of items requested by the OrderLine is greater than the number available in the Inventory. Please re-stock.";
	private static final String NO_INVENTORY_ITEM = "No inventory item with given product indentifier found in inventory. Have you initialized your inventory? Do you need to re-stock it?";

	private final @NonNull MultiInventory<MultiInventoryItem> inventory;
	private final @NonNull UniqueInventory<UniqueInventoryItem> uniqueInventory;
	private final @NonNull List<LineItemFilter> filters;

	/**
	 * Verifies the stock for the products referenced from {@link OrderLine} items and updates the inventory accordingly.
	 *
	 * @param order must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	public OrderCompletionReport verifyAndUpdate(Order order) {

		Assert.notNull(order, "Order must not be null!");

		var collect = order.getOrderLines() //
				.map(this::verify)//
				.toList();

		return OrderCompletionReport.forCompletions(order, collect) //
				.onError(OrderCompletionFailure::new);
	}

	/**
	 * Updates the stock for the {@link OrderLine} items in the given, cancelled {@link Order}.
	 *
	 * @param order must not be {@literal null}.
	 */
	public void updateStockForCancelledOrder(Order order) {

		Assert.notNull(order, "Order must not be null!");

		if (!order.isCanceled()) {
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
	 * @return will never be {@literal null}.
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
	 * Verifies that the the given {@link UniqueInventoryItem}'s stock is high enough to satisfy the requested quantity of
	 * the given {@link OrderLine}. Decreases the {@link UniqueInventoryItem}'s quantity if so.
	 *
	 * @param item must not be {@literal null}.
	 * @param orderLine must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	private OrderLineCompletion verifyAndUpdateUnique(UniqueInventoryItem item, OrderLine orderLine) {
		return hasSufficientQuantity(item, orderLine)
				.onSuccess(it -> uniqueInventory.save(item.decreaseQuantity(it.getQuantity())));
	}

	/**
	 * Creates a new {@link OrderLineCompletion} verifying that at least one {@link MultiInventoryItem} exists.
	 *
	 * @param orderLine must not be {@literal null}.
	 * @return will never be {@literal null}.
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
