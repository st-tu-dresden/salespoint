/*
 * Copyright 2018-2019 the original author or authors.
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

import org.salespointframework.catalog.Product;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.quantity.Quantity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.util.Streamable;

/**
 * Base interface for {@link InventoryItem} implementations. Choose either {@link UniqueInventory} or
 * {@link MultiInventory} for your application depending on whether you need to keep track of multiple locations (e.g.
 * warehouses) in which {@link InventoryItem}s are supposed to be managed.
 * <p>
 * {@link UniqueInventoryItem} expects a one-to-one relationship to a product, which is equivalent to modeling a single
 * warehouse. That's simple and allows to look up the {@link UniqueInventoryItem} by {@link ProductIdentifier}. I.e. the
 * {@link Quantity} contained in that {@link UniqueInventoryItem} is equivalent to the overall quantity in the system.
 * This is the simpler model in general and should be preferred. {@link Product}s held in {@link UniqueInventoryItem}s
 * are suspect to automatic inventory updates on order completion. See {@link InventoryOrderEventListener} for details.
 * <p>
 * If you absolutely need to model {@link Product}s managed in multiple warehouses, use {@link MultiInventoryItem}
 * alongside {@link MultiInventory}. {@link MultiInventory#findByProductIdentifier(ProductIdentifier)} rather returns an
 * {@link InventoryItems} instance. The overall {@link Quantity} of {@link Product}s in the system can then be obtained
 * via {@link InventoryItems#getTotalQuantity()}. {@link MultiInventoryItem}s are not suspect to auto-inventory updates
 * upon order completion as it's not clear which of the {@link InventoryItem}s is supposed to be deducted.
 *
 * @author Oliver Drotbohm
 * @since 7.2
 */
@NoRepositoryBean
public interface Inventory<T extends InventoryItem<?>> {

	/**
	 * Returns all {@link UniqueInventoryItem}s that are out of stock (i.e. the {@link Quantity}'s amount is equal or less
	 * than zero).
	 *
	 * @return will never be {@literal null}.
	 */
	@Query("select i from #{#entityName} i where i.quantity.amount <= 0")
	Streamable<T> findItemsOutOfStock();
}
