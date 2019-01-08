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

import java.util.Optional;

import org.salespointframework.catalog.Product;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.quantity.Quantity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

/**
 * Repository interface for {@link InventoryItem}s.
 * 
 * @author Oliver Gierke
 */
// tag::inventory[]
public interface Inventory<T extends InventoryItem> extends CrudRepository<T, InventoryItemIdentifier> {

	/**
	 * Returns all {@link InventoryItem}s that are out of stock (i.e. the {@link Quantity}'s amount is equal or less than
	 * zero).
	 * 
	 * @return will never be {@literal null}.
	 */
	@Query("select i from InventoryItem i where i.quantity.amount <= 0")
	Streamable<T> findItemsOutOfStock();

	/**
	 * Returns the {@link InventoryItem} for the {@link Product} with the given identifier.
	 * 
	 * @param productIdentifier must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	@Query("select i from InventoryItem i where i.product.id = ?1")
	Optional<T> findByProductIdentifier(ProductIdentifier productIdentifier);

	/**
	 * Returns the {@link InventoryItem} for the given {@link Product}.
	 * 
	 * @param product must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	default Optional<T> findByProduct(Product product) {
		return findByProductIdentifier(product.getId());
	}
}
// end::inventory[]
