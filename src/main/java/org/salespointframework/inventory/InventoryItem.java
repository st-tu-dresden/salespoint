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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.salespointframework.catalog.Product;
import org.salespointframework.core.AbstractEntity;
import org.salespointframework.quantity.Quantity;
import org.springframework.util.Assert;

/**
 * @author Paul Henke
 * @author Oliver Gierke
 */
@Entity
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class InventoryItem extends AbstractEntity<InventoryItemIdentifier> {

	private static final long serialVersionUID = 3322056345377472377L;

	@EmbeddedId //
	@AttributeOverride(name = "id", column = @Column(name = "ITEM_ID")) //
	private final InventoryItemIdentifier inventoryItemIdentifier = new InventoryItemIdentifier();

	@Getter //
	@JoinColumn(unique = true) //
	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH }) //
	private Product product;

	@Getter //
	private Quantity quantity;

	/**
	 * Creates a new {@link InventoryItem} for the given {@link Product} and {@link Quantity}.
	 * 
	 * @param product the {@link Product} for this {@link InventoryItem}, must not be {@literal null}.
	 * @param quantity the initial {@link Quantity} for this {@link InventoryItem}, must not be {@literal null}.
	 */
	public InventoryItem(Product product, Quantity quantity) {

		Assert.notNull(product, "Product must be not null!");
		Assert.notNull(quantity, "Quantity must be not null!");

		product.verify(quantity);

		this.product = product;
		this.quantity = quantity;

	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.domain.Persistable#getId()
	 */
	public final InventoryItemIdentifier getId() {
		return inventoryItemIdentifier;
	}

	/**
	 * Returns whether the {@link InventoryItem} is available in exactly or more of the given quantity.
	 * 
	 * @param quantity must not be {@literal null}.
	 * @return
	 */
	public boolean hasSufficientQuantity(Quantity quantity) {
		return !this.quantity.subtract(quantity).isNegative();
	}

	/**
	 * Decreases the quantity of the current {@link InventoryItem} by the given {@link Quantity}.
	 * 
	 * @param quantity must not be {@literal null}.
	 */
	public void decreaseQuantity(Quantity quantity) {

		Assert.notNull(quantity, "Quantity must not be null!");
		Assert.isTrue(this.quantity.isGreaterThanOrEqualTo(quantity),
				String.format("Insufficient quantity! Have %s but was requested to reduce by %s.", this.quantity, quantity));

		product.verify(quantity);

		this.quantity = this.quantity.subtract(quantity);
	}

	/**
	 * Increases the quantity of the current {@link InventoryItem} by the given {@link Quantity}.
	 * 
	 * @param quantity must not be {@literal null}.
	 */
	public void increaseQuantity(Quantity quantity) {

		Assert.notNull(quantity, "Quantity must not be null!");
		product.verify(quantity);

		this.quantity = this.quantity.add(quantity);
	}
}
