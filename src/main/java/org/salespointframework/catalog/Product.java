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
package org.salespointframework.catalog;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.PrePersist;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.money.MonetaryAmount;

import org.jmolecules.ddd.types.Identifier;
import org.salespointframework.catalog.Product.ProductIdentifier;
import org.salespointframework.core.AbstractAggregateRoot;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.MetricMismatchException;
import org.salespointframework.quantity.Quantity;
import org.springframework.data.util.Streamable;
import org.springframework.util.Assert;

/**
 * A product.
 *
 * @author Paul Henke
 * @author Oliver Gierke
 */
@Entity
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED, onConstructor = @__(@Deprecated))
public class Product extends AbstractAggregateRoot<ProductIdentifier> implements Comparable<Product> {

	private static final String INVALID_METRIC = "Product %s does not support quantity %s using metric %s!";

	private @EmbeddedId ProductIdentifier id = ProductIdentifier.of(UUID.randomUUID().toString());
	private @NonNull @Getter @Setter String name;
	private @NonNull @Getter @Setter MonetaryAmount price;
	private @ElementCollection(fetch = FetchType.EAGER) Set<String> categories = new HashSet<String>();
	private Metric metric;

	/**
	 * Creates a new {@link Product} with the given name and price.
	 *
	 * @param name must not be {@literal null} or empty.
	 * @param price must not be {@literal null}.
	 */
	public Product(String name, MonetaryAmount price) {
		this(name, price, Metric.UNIT);
	}

	/**
	 * Creates a new {@link Product} with the given name, price and {@link Metric}.
	 *
	 * @param name the name of the {@link Product}, must not be {@literal null} or empty.
	 * @param price the price of the {@link Product}, must not be {@literal null}.
	 * @param metric the {@link Metric} of the {@link Product}, must not be {@literal null}.
	 */
	public Product(String name, MonetaryAmount price, Metric metric) {

		Assert.hasText(name, "Name must not be null or empty!");
		Assert.notNull(price, "Price must not be null!");
		Assert.notNull(metric, "Metric must not be null!");

		this.name = name;
		this.price = price;
		this.metric = metric;
	}

	/**
	 * Returns the unique id of this {@link Product}.
	 *
	 * @return will never be {@literal null}
	 */
	@Override
	public ProductIdentifier getId() {
		return id;
	}

	/**
	 * Returns the categories the {@link Product} is assigned to.
	 *
	 * @return will never be {@literal null}.
	 */
	public Streamable<String> getCategories() {
		return Streamable.of(Collections.unmodifiableSet(categories));
	}

	/**
	 * Adds the {@link Product} to the given category.
	 *
	 * @param category must not be {@literal null} or empty.
	 * @return
	 */
	public final boolean addCategory(String category) {

		Assert.hasText(category, "category must not be null");
		return categories.add(category);
	}

	public final boolean removeCategory(String category) {

		Assert.notNull(category, "category must not be null");
		return categories.remove(category);
	}

	/**
	 * Returns whether the {@link Product} supports the given {@link Quantity}.
	 *
	 * @param quantity
	 * @return
	 */
	public boolean supports(Quantity quantity) {

		Assert.notNull(quantity, "Quantity must not be null!");
		return quantity.isCompatibleWith(metric);
	}

	/**
	 * Verifies the given {@link Quantity} to match the one supported by the current {@link Product}.
	 *
	 * @param quantity
	 * @throws MetricMismatchException in case the {@link Product} does not support the given {@link Quantity}.
	 */
	public void verify(Quantity quantity) {

		if (!supports(quantity)) {
			throw new MetricMismatchException(String.format(INVALID_METRIC, this, quantity, quantity.getMetric()));
		}
	}

	/**
	 * Creates a {@link Quantity} of the given amount and the current {@link Product}'s underlying {@link Metric}.
	 *
	 * @param amount must not be {@literal null}.
	 * @return
	 */
	public Quantity createQuantity(double amount) {
		return Quantity.of(amount, metric);
	}

	/**
	 * Creates a {@link Quantity} of the given amount and the current {@link Product}'s underlying {@link Metric}.
	 *
	 * @param amount must not be {@literal null}.
	 * @return
	 */
	public Quantity createQuantity(long amount) {
		return Quantity.of(amount, metric);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Product other) {
		return this.name.compareTo(other.name);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("%s, %s, %s, handled in %s, categories %s", name, id, price, metric, categories);
	}

	/**
	 * Manual verification that invariants are met as JPA requires us to expose a default constructor that also needs to
	 * be callable from sub-classes as they need to declare one as well.
	 */
	@PrePersist
	void verifyConstraints() {

		Assert.state(metric != null,
				"No metric set! Make sure you have created the product by calling a non-default constructor!");
	}

	/**
	 * {link ProductIdentifier} serves as an identifier type for {@link Product} objects. The main reason for its
	 * existence is type safety for identifier across the Salespoint Framework. <br />
	 * {@link ProductIdentifier} instances serve as primary key attribute in {@link Product}, but can also be used as a
	 * key for non-persistent, {@link Map}-based implementations.
	 *
	 * @author Paul Henke
	 * @author Oliver Gierke
	 */
	@Embeddable
	@EqualsAndHashCode
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(force = true, access = AccessLevel.PACKAGE)
	public static class ProductIdentifier implements Identifier, Serializable {

		private static final long serialVersionUID = 67875667760921725L;

		private final String productId;

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return productId;
		}
	}
}
