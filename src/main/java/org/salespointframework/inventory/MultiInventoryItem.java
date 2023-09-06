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
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.salespointframework.catalog.Product;
import org.salespointframework.quantity.Quantity;

/**
 * An {@link MultiInventoryItem} that establishes a many-to-one relationship to a {@link Product} instance, i.e. it can be
 * used to keep track of stock for products in e.g. multiple warehouses etc.
 *
 * @author Oliver Drotbohm
 */
@Entity
@NoArgsConstructor(force = true, access = AccessLevel.PACKAGE)
public class MultiInventoryItem extends InventoryItem<MultiInventoryItem> {

	@ManyToOne //
	private Product product;

	/**
	 * Creates a new {@link MultiInventoryItem} for the given {@link Product} and {@link Quantity}.
	 *
	 * @param product the {@link Product} for this {@link MultiInventoryItem}, must not be {@literal null}.
	 * @param quantity the initial {@link Quantity} for this {@link MultiInventoryItem}, must not be {@literal null}.
	 */
	public MultiInventoryItem(Product product, Quantity quantity) {

		super(product, quantity);

		this.product = product;
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.inventory.InventoryItem#getProduct()
	 */
	@Override
	protected Product getProduct() {
		return this.product;
	}
}
