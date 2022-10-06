/*
 * Copyright 2017-2022 the original author or authors.
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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

import org.jmolecules.ddd.types.Identifier;
import org.salespointframework.catalog.Product;
import org.salespointframework.core.AbstractAggregateRoot;
import org.salespointframework.inventory.InventoryEvents.QuantityReduced;
import org.salespointframework.inventory.InventoryItem.InventoryItemIdentifier;
import org.salespointframework.quantity.Quantity;
import org.springframework.util.Assert;

/**
 * An {@link InventoryItem} associates a product with a {@link Quantity} to keep track of how many items per product are
 * available.
 *
 * @author Paul Henke
 * @author Oliver Drotbohm
 * @author Martin Morgenstern
 * @see UniqueInventoryItem
 * @see MultiInventoryItem
 * @since 7.2
 */
@MappedSuperclass
@NoArgsConstructor(force = true, access = AccessLevel.PACKAGE)
@EntityListeners(InventoryItemCreationListener.class)
public abstract class InventoryItem<T extends InventoryItem<T>> extends AbstractAggregateRoot<InventoryItemIdentifier> {

	private final @EmbeddedId InventoryItemIdentifier inventoryItemIdentifier = InventoryItemIdentifier
			.of(UUID.randomUUID().toString());

	@Getter //
	private Quantity quantity;

	/**
	 * Creates a new {@link InventoryItem} for the given {@link Product} and {@link Quantity}.
	 *
	 * @param quantity the initial {@link Quantity} for this {@link InventoryItem}, must not be {@literal null}.
	 */
	protected InventoryItem(Product product, Quantity quantity) {

		Assert.notNull(product, "Product must be not null!");
		Assert.notNull(quantity, "Quantity must be not null!");

		product.verify(quantity);

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
	@SuppressWarnings("unchecked")
	public T decreaseQuantity(Quantity quantity) {

		Assert.notNull(quantity, "Quantity must not be null!");
		Assert.isTrue(this.quantity.isGreaterThanOrEqualTo(quantity),
				String.format("Insufficient quantity! Have %s but was requested to reduce by %s.", this.quantity,
						quantity));

		getProduct().verify(quantity);

		this.quantity = this.quantity.subtract(quantity);

		registerEvent(QuantityReduced.of(this));

		return (T) this;
	}

	/**
	 * Increases the quantity of the current {@link InventoryItem} by the given {@link Quantity}.
	 *
	 * @param quantity must not be {@literal null}.
	 */
	@SuppressWarnings("unchecked")
	public T increaseQuantity(Quantity quantity) {

		Assert.notNull(quantity, "Quantity must not be null!");
		getProduct().verify(quantity);

		this.quantity = this.quantity.add(quantity);

		return (T) this;
	}

	/**
	 * Returns whether the {@link InventoryItem} belongs to the given {@link Product}.
	 *
	 * @param product must not be {@literal null}.
	 * @return
	 */
	public boolean keepsTrackOf(Product product) {
		return this.getProduct().equals(product);
	}

	/**
	 * Returns whether the given {@link InventoryItem} is a different one but keeping track of the same {@link Product}.
	 *
	 * @param other
	 * @return
	 */
	boolean isDifferentItemForSameProduct(InventoryItem<?> other) {
		return !this.equals(other) && this.keepsTrackOf(other.getProduct());
	}

	/**
	 * Manual verification that invariants are met as JPA requires us to expose a default constructor that also needs to
	 * be callable from sub-classes as they need to declare one as well.
	 */
	@PrePersist
	void verifyConstraints() {

		Assert.state(quantity != null,
				"No quantity set! Make sure you have created the product by calling a non-default constructor!");
	}

	/**
	 * Returns the {@link Product} this {@link InventoryItem} belongs to.
	 *
	 * @return must not be {@literal null}.
	 */
	protected abstract Product getProduct();

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		Product product = getProduct();

		return String.format("%s(%s) for Product(%s, \"%s\") with quantity %s", //
				getClass().getSimpleName(), getId(), product.getId(), product.getName(), getQuantity());
	}

	/**
	 * {@code InventoryItemIdentifier} serves as an identifier type for {@link UniqueInventoryItem} objects. The main
	 * reason for its existence is type safety for identifier across the Salespoint Framework. <br />
	 * {@code InventoryItemIdentifier} instances serve as primary key attribute in {@link UniqueInventoryItem}, but can
	 * also be used as a key for non-persistent, <code>Map</code>-based implementations.
	 *
	 * @author Paul Henke
	 * @author Oliver Gierke
	 */
	@Embeddable
	@EqualsAndHashCode
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(force = true, access = AccessLevel.PACKAGE)
	public static class InventoryItemIdentifier implements Identifier, Serializable {

		private static final long serialVersionUID = -3309444549353766703L;

		private final String inventoryItemId;

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return inventoryItemId;
		}
	}
}
