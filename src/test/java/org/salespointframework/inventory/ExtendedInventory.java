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
package org.salespointframework.inventory;

import java.util.List;

import org.salespointframework.quantity.Quantity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Example of an {@link Inventory} extension
 * 
 * @author Oliver Gierke
 */
interface ExtendedInventory extends Inventory<InventoryItem> {

	/**
	 * Returns all {@link InventoryItem}s with {@link Quantity}s of the same metric and an amount greater than the one in
	 * the given {@link Quantity}.
	 * 
	 * @param quantity must not be {@literal null}.
	 * @return
	 */
	@Query("select i from InventoryItem i where i.quantity.amount > ?#{#quantity.amount} and i.quantity.metric = ?#{#quantity.metric}")
	List<InventoryItem> findByQuantityGreaterThan(@Param("quantity") Quantity quantity);
}
