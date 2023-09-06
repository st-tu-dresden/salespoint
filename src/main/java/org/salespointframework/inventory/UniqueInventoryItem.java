/*
 * Copyright 2017-2023 the original author or authors.
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

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.salespointframework.catalog.Product;
import org.salespointframework.quantity.Quantity;

/**
 * An {@link InventoryItem} that requires a unique one-to-one relationship to a {@link Product} instance.
 *
 * @author Oliver Drotbohm
 * @since 7.2
 */
@Entity
@NoArgsConstructor(force = true, access = AccessLevel.PACKAGE)
public class UniqueInventoryItem extends InventoryItem<UniqueInventoryItem> {

	@OneToOne //
	@JoinColumn(unique = true) //
	private Product product;

	/**
	 * Creates a new {@link UniqueInventoryItem} for the given {@link Product} and {@link Quantity}.
	 *
	 * @param product the {@link Product} for this {@link UniqueInventoryItem}, must not be {@literal null}.
	 * @param quantity the initial {@link Quantity} for this {@link UniqueInventoryItem}, must not be {@literal null}.
	 */
	public UniqueInventoryItem(Product product, Quantity quantity) {

		super(product, quantity);

		this.product = product;
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.inventory.InventoryItem#getProduct()
	 */
	@Override
	public Product getProduct() {
		return this.product;
	}
}
