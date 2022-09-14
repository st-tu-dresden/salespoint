/*
 * Copyright 2018-2022 the original author or authors.
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

import jakarta.persistence.PrePersist;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;

import org.salespointframework.catalog.Product;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

/**
 * JPA entity listener to make sure that no new {@link MultiInventoryItems} are created for {@link Product}s already
 * managed by a {@link UniqueInventoryItem}. Also, creating a new {@link UniqueInventoryItem} for a {@link Product} for
 * which a {@link UniqueInventoryItem} already exists is prevented.
 *
 * @author Oliver Drotbohm
 * @since 7.2
 */
@Component
@RequiredArgsConstructor
class InventoryItemCreationListener {

	private static final String UNIQUE_ITEM_ALREADY_EXISTS = "Trying to persist unique inventory item for %s. The following item(s) already exist: %s.";

	private final @NonNull ObjectProvider<UniqueInventory<UniqueInventoryItem>> uniqueInventory;
	private final @NonNull ObjectProvider<MultiInventory<MultiInventoryItem>> inventory;

	/**
	 * Verifies that there's no other {@link UniqueInventoryItem} present for the product that the to be persisted
	 * {@link InventoryItem} belongs to or there's no {@link InventoryItem} for the referred to {@link Product} in general
	 * if the item to be persisted is a {@link UniqueInventoryItem}.
	 *
	 * @param item must not be {@literal null}.
	 */
	@PrePersist
	public void verify(InventoryItem<?> item) {

		assertNonUniqueItem(item);

		if (UniqueInventoryItem.class.isInstance(item)) {

			var existing = inventory.getObject().findByProductIdentifier(item.getProduct().getId());

			if (existing.isEmpty()) {
				return;
			}

			throw new IllegalStateException(String.format(UNIQUE_ITEM_ALREADY_EXISTS, item, existing.stream()//
					.map(Object::toString) //
					.collect(Collectors.joining(", "))));
		}
	}

	private void assertNonUniqueItem(InventoryItem<?> item) {

		uniqueInventory.getObject().findByProduct(item.getProduct()) //
				.filter(it -> it.isDifferentItemForSameProduct(item)) //
				.ifPresent(existing -> {
					throw new IllegalStateException(String.format(UNIQUE_ITEM_ALREADY_EXISTS, item, existing));
				});
	}
}
