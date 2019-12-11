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
import org.salespointframework.core.SalespointRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.util.Assert;

/**
 * An {@link Inventory} that stores {@link MultiInventoryItem}s, i.e. {@link InventoyItem}s for which multiple might
 * exist per product, e.g. if you need to keep track of an item per warehouse or the like.
 *
 * @author Oliver Drotbohm
 * @since 7.2
 */
public interface MultiInventory<T extends MultiInventoryItem>
		extends Inventory<MultiInventoryItem>, SalespointRepository<T, InventoryItemIdentifier> {

	/**
	 * Returns all {@link InventoryItems} available for a given {@link ProductIdentifier}.
	 *
	 * @param productIdentifier must not be {@literal null}.
	 * @return
	 */
	@Query("select i from #{#entityName} i where i.product.id = ?1")
	InventoryItems<T> findByProductIdentifier(ProductIdentifier productIdentifier);

	/**
	 * Returns all {@link InventoryItems} available for a given {@link Product}.
	 *
	 * @param product must not be {@literal null}.
	 * @return
	 */
	default InventoryItems<T> findByProduct(Product product) {

		Assert.notNull(product, "Product must not be null!");

		return findByProductIdentifier(product.getId());
	}
}
