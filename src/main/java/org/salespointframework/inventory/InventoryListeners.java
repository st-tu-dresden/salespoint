/*
 * Copyright 2020 the original author or authors.
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

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.salespointframework.catalog.Product;
import org.salespointframework.inventory.InventoryEvents.QuantityReduced;
import org.salespointframework.inventory.InventoryEvents.StockShort;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderCompletionFailure;
import org.salespointframework.order.OrderEvents.OrderCanceled;
import org.salespointframework.order.OrderEvents.OrderCompleted;
import org.salespointframework.order.OrderLine;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Event listeners of the inventory module.
 *
 * @author Oliver Drotbohm
 * @since 7.3
 */
class InventoryListeners {

	/**
	 * Event listener to monitor {@link QuantityReduced} events and compare against the
	 * {@link InventoryProperties#getRestockThreshold()} configured.
	 *
	 * @author Oliver Drotbohm
	 * @since 7.3
	 */
	@Component
	@RequiredArgsConstructor
	static class InternalInventoryListeners {

		private final InventoryProperties configuration;

		@EventListener
		StockShort on(QuantityReduced event) {

			var threshold = configuration.getRestockThreshold();
			var item = event.getItem();

			return item.hasSufficientQuantity(threshold)
					? null
					: StockShort.of(item, threshold);
		}
	}

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
	static class InventoryOrderEventListener {

		private final @NonNull InventoryManagement management;

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
			management.verifyAndUpdate(event.getOrder());
		}

		/**
		 * Rolls back the stock decreases handled for {@link OrderCompleted} events.
		 *
		 * @param event must not be {@literal null}.
		 */
		@EventListener
		public void on(OrderCanceled event) {
			management.cancelOrder(event.getOrder());
		}
	}
}
