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
package org.salespointframework.quantity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import javax.persistence.Embeddable;

import org.springframework.util.Assert;

/**
 * A value object to represent a quantity.
 * 
 * @author Oliver Gierke
 */
@Embeddable
@Value
@NoArgsConstructor(force = true, access = AccessLevel.PACKAGE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Quantity {

	private static final String INCOMPATIBLE = "Quantity %s is incompatible to quantity %s!";

	/**
	 * The amount of the Quantity.
	 */
	private @NonNull final BigDecimal amount;

	/**
	 * The metric of the Quantity.
	 */
	private @NonNull final Metric metric;

	/**
	 * Creates a new {@link Quantity} of the given amount. Defaults the metric to {@value Metric#UNIT}.
	 * 
	 * @param amount must not be {@literal null}.
	 * @return
	 */
	public static Quantity of(long amount) {
		return of(amount, Metric.UNIT);
	}

	/**
	 * Creates a new {@link Quantity} of the given amount. Defaults the metric to {@value Metric#UNIT}.
	 * 
	 * @param amount must not be {@literal null}.
	 * @return
	 */
	public static Quantity of(double amount) {
		return of(amount, Metric.UNIT);
	}

	/**
	 * Creates a new {@link Quantity} of the given amount and {@link Metric}.
	 * 
	 * @param amount must not be {@literal null}.
	 * @param metric must not be {@literal null}.
	 * @return
	 */
	public static Quantity of(long amount, Metric metric) {
		return new Quantity(BigDecimal.valueOf(amount), metric);
	}

	/**
	 * Creates a new {@link Quantity} of the given amount and {@link Metric}.
	 * 
	 * @param amount must not be {@literal null}.
	 * @param metric must not be {@literal null}.
	 * @return
	 */
	public static Quantity of(double amount, Metric metric) {
		return new Quantity(BigDecimal.valueOf(amount), metric);
	}

	/**
	 * Creates a new {@link Quantity} of the given amount and {@link Metric}.
	 * 
	 * @param amount must not be {@literal null}.
	 * @param metric must not be {@literal null}.
	 * @return
	 */
	public static Quantity of(BigDecimal amount, Metric metric) {
		return new Quantity(amount, metric);
	}

	/**
	 * Returns whether the {@link Quantity} is compatible with the given {@link Metric}.
	 * 
	 * @param metric must not be {@literal null}.
	 * @return
	 */
	public boolean isCompatibleWith(Metric metric) {

		Assert.notNull(metric, "Metric must not be null!");
		return this.metric.isCompatibleWith(metric);
	}

	/**
	 * Adds the given {@link Quantity} to the current one.
	 * 
	 * @param other the {@link Quantity} to add. The given {@link Quantity}'s {@link Metric} must be compatible with the
	 *          current one.
	 * @return
	 * @see #isCompatibleWith(Metric)
	 */
	public Quantity add(Quantity other) {

		assertCompatibility(other);
		return new Quantity(this.amount.add(other.amount), this.metric);
	}

	/**
	 * Subtracts the given Quantity from the current one.
	 * 
	 * @param other the {@link Quantity} to add. The given {@link Quantity}'s {@link Metric} must be compatible with the
	 *          current one.
	 * @return
	 * @see #isCompatibleWith(Metric)
	 */
	public Quantity subtract(Quantity other) {

		assertCompatibility(other);
		return new Quantity(this.amount.subtract(other.amount), this.metric);
	}

	/**
	 * Returns whether the given {@link Quantity} is less than the current one.
	 * 
	 * @param other must not be {@literal null}. The given {@link Quantity}'s {@link Metric} must be compatible with the
	 *          current one.
	 * @return
	 * @see #isCompatibleWith(Metric)
	 */
	public boolean isLessThan(Quantity other) {

		assertCompatibility(other);
		return this.amount.compareTo(other.amount) < 0;
	}

	/**
	 * Returns whether the given {@link Quantity} is greater than the current one.
	 * 
	 * @param other must not be {@literal null}. The given {@link Quantity}'s {@link Metric} must be compatible with the
	 *          current one.
	 * @return
	 * @see #isCompatibleWith(Metric)
	 */
	public boolean isGreaterThan(Quantity other) {

		assertCompatibility(other);
		return this.amount.compareTo(other.amount) > 0;
	}

	/**
	 * Returns whether the given {@link Quantity} is greater than or equal to the current one.
	 * 
	 * @param other must not be {@literal null}. The given {@link Quantity}'s {@link Metric} must be compatible with the
	 *          current one.
	 * @return
	 * @see #isCompatibleWith(Metric)
	 */
	public boolean isGreaterThanOrEqualTo(Quantity other) {

		assertCompatibility(other);
		return this.amount.compareTo(other.amount) >= 0;
	}

	/**
	 * Returns whether the current {@link Quantity} is negative.
	 * 
	 * @return
	 */
	public boolean isNegative() {
		return this.amount.compareTo(BigDecimal.ZERO) < 0;
	}

	public boolean isZeroOrNegative() {
		return this.equals(toZero()) || isNegative();
	}

	/**
	 * Returns a new {@link Quantity} of zero with the {@link Metric} of the current one.
	 * 
	 * @return will never be {@literal null}.
	 */
	public Quantity toZero() {
		return Quantity.of(0, metric);
	}

	private void assertCompatibility(Quantity quantity) {

		Assert.notNull(quantity, "Quantity must not be null!");

		if (!isCompatibleWith(quantity.metric)) {
			throw new MetricMismatchException(String.format(INCOMPATIBLE, this, quantity), metric, quantity.metric);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new DecimalFormat().format(amount).concat(metric.getAbbreviation());
	}
}
