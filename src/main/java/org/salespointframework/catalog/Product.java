/*
 * Copyright 2017 the original author or authors.
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
package org.salespointframework.catalog;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.money.MonetaryAmount;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.salespointframework.core.AbstractEntity;
import org.salespointframework.core.Streamable;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.MetricMismatchException;
import org.salespointframework.quantity.Quantity;
import org.springframework.util.Assert;

/**
 * A product.
 * 
 * @author Paul Henke
 * @author Oliver Gierke
 */
@Entity
@ToString
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class Product extends AbstractEntity<ProductIdentifier> implements Comparable<Product> {

	private static final long serialVersionUID = 6645371648836029780L;

	@EmbeddedId //
	@AttributeOverride(name = "id", column = @Column(name = "PRODUCT_ID")) //
	private ProductIdentifier productIdentifier = new ProductIdentifier();
	private @NonNull @Getter @Setter String name;
	private @NonNull @Getter @Setter MonetaryAmount price;
	private @ElementCollection Set<String> categories = new HashSet<String>();
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

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.data.domain.Persistable#getId()
	 */
	@Override
	public ProductIdentifier getId() {
		return productIdentifier;
	}

	/**
	 * Returns the categories the {@link Product} is assigned to.
	 * 
	 * @return will never be {@literal null}.
	 */
	public final Streamable<String> getCategories() {
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
			throw new MetricMismatchException(String.format("Product %s does not support quantity %s!", this, quantity));
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
}
