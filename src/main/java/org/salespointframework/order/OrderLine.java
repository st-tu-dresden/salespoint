/*
 * Copyright 2017-2021 the original author or authors.
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
package org.salespointframework.order;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

import javax.money.MonetaryAmount;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.jmolecules.ddd.types.Identifier;
import org.salespointframework.catalog.Product;
import org.salespointframework.catalog.Product.ProductIdentifier;
import org.salespointframework.core.AbstractEntity;
import org.salespointframework.order.OrderLine.OrderLineIdentifier;
import org.salespointframework.quantity.MetricMismatchException;
import org.salespointframework.quantity.Quantity;
import org.springframework.util.Assert;

/**
 * An order line represents the price and the {@link Quantity} of a {@link Product} that is intended to be purchased as
 * part of an {@link Order}.
 * <p>
 * Order lines should not be used to represent expenses for services, such as shipping. For this purpose,
 * {@link ChargeLine} should be used instead.
 * <p>
 * Note that the constructor of this class creates a copy of the product's name and price, so that changes to those
 * attributes do not affect existing orders.
 *
 * @see ChargeLine
 * @author Paul Henke
 * @author Oliver Gierke
 */
@Entity
@ToString
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class OrderLine extends AbstractEntity<OrderLineIdentifier> implements Priced {

	private @EmbeddedId OrderLineIdentifier orderLineIdentifier = OrderLineIdentifier.of(UUID.randomUUID().toString());

	private @Getter ProductIdentifier productIdentifier;
	private @Getter MonetaryAmount price;
	private @Getter Quantity quantity;
	private @Getter String productName;

	/**
	 * Creates a new {@link OrderLine} for the given {@link Product} and {@link Quantity}.
	 *
	 * @param product must not be {@literal null}.
	 * @param quantity must not be {@literal null}.
	 */
	OrderLine(Product product, Quantity quantity) {

		Assert.notNull(product, "Product must be not null!");
		Assert.notNull(quantity, "Quantity must be not null!");

		if (!product.supports(quantity)) {
			throw new MetricMismatchException(String.format("Product %s does not support quantity %s!", product, quantity));
		}

		this.productIdentifier = product.getId();
		this.quantity = quantity;
		this.price = product.getPrice().multiply(quantity.getAmount());
		this.productName = product.getName();
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.domain.Persistable#getId()
	 */
	@Override
	public OrderLineIdentifier getId() {
		return orderLineIdentifier;
	}

	/**
	 * Returns whether the {@link OrderLine} refers to the given {@link Product}.
	 *
	 * @param product must not be {@literal null}.
	 * @return
	 * @since 7.1
	 */
	public boolean refersTo(Product product) {

		Assert.notNull(product, "Product must not be null!");

		return this.productIdentifier.equals(product.getId());
	}

	/**
	 * Returns whether the {@link OrderLine} refers to the {@link Product} with the given identifier.
	 *
	 * @param identifier must not be {@literal null}.
	 * @return
	 * @since 7.1
	 */
	public boolean refersTo(ProductIdentifier identifier) {

		Assert.notNull(identifier, "Product identifier must not be null!");

		return this.productIdentifier.equals(identifier);
	}

	/**
	 * {@link OrderLineIdentifier} serves as an identifier type for {@link OrderLine} objects. The main reason for its
	 * existence is type safety for identifier across the Salespoint Framework. <br />
	 * {@link OrderLineIdentifier} instances serve as primary key attribute in {@link OrderLine}, but can also be used as
	 * a key for non-persistent, {@link Map}-based implementations.
	 *
	 * @author Thomas Dedek
	 * @author Oliver Gierke
	 */
	@Embeddable
	@EqualsAndHashCode
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
	static class OrderLineIdentifier implements Identifier, Serializable {

		private static final long serialVersionUID = 2978461586574769497L;

		private final String orderLineId;

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return orderLineId;
		}
	}
}
