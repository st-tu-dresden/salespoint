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
package org.salespointframework.quantity;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.springframework.util.Assert;

/**
 * A value object to represent a quantity.
 *
 * @author Oliver Gierke
 * @author Martin Morgenstern
 */
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(force = true, access = AccessLevel.PACKAGE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Access(AccessType.PROPERTY)
public class Quantity {

	public static final Quantity NONE = Quantity.of(0);
	private static final String INCOMPATIBLE = "Quantity %s is incompatible to quantity %s!";

	/**
	 * The amount of the Quantity. Explicitly set a prefixed column name to avoid name conflicts.
	 */
	@Getter(onMethod = @__(@Column(name = "quantity_amount"))) //
	private @NonNull BigDecimal amount;

	/**
	 * The metric of the Quantity. Explicitly set a prefixed column name to avoid name conflicts.
	 */
	@Getter(onMethod = @__(@Column(name = "quantity_metric"))) //
	private @NonNull Metric metric;

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
	static Quantity of(BigDecimal amount, Metric metric) {
		return new Quantity(amount, metric);
	}

	/**
	 * Returns whether the {@link Quantity} is compatible with the given {@link Metric}.
	 *
	 * @param metric must not be {@literal null}.
	 * @return
	 */
	public boolean isCompatibleWith(Metric metric) {

		if (this == Quantity.NONE) {
			return true;
		}

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

		if (this == NONE) {
			return other;
		}

		if (other == NONE) {
			return this;
		}

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

		if (this == NONE) {
			return other.negate();
		}

		if (other == NONE) {
			return this;
		}

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
	 * Creates a new {@link Quantity} of the current one multiplied with the given int.
	 *
	 * @param multiplier
	 * @return will never be {@literal null}.
	 */
	public Quantity times(int multiplier) {
		return times((long) multiplier);
	}

	/**
	 * Creates a new {@link Quantity} of the current one multiplied with the given long.
	 *
	 * @param multiplier
	 * @return will never be {@literal null}.
	 */
	public Quantity times(long multiplier) {
		return new Quantity(amount.multiply(BigDecimal.valueOf(multiplier)), metric);
	}

	/**
	 * Returns whether the current {@link Quantity} is equal to the given one negelecting potential differences in
	 * precision of the underlying amount. I.e. an amount of 1 is considered equal to an amount of 1.0.
	 *
	 * @param other must not be {@literal null}.
	 * @return
	 * @since 7.2.2
	 */
	public boolean isEqualTo(Quantity other) {

		Assert.notNull(other, "Quantity must not be null!");

		return metric.isCompatibleWith(other.metric) //
				&& this.amount.compareTo(other.amount) == 0;
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
	 * Returns whether the current {@link Quantity} is positive.
	 *
	 * @return
	 * @since 7.5
	 */
	@Transient
	public boolean isPositive() {
		return this.amount.compareTo(BigDecimal.ZERO) > 0;
	}

	/**
	 * Returns whether the current {@link Quantity} is zero or positive.
	 *
	 * @return
	 * @since 7.5
	 */
	@Transient
	public boolean isZeroOrPositive() {
		return isGreaterThanOrEqualTo(toZero());
	}

	/**
	 * Returns whether the current {@link Quantity} is negative.
	 *
	 * @return
	 */
	@Transient
	public boolean isNegative() {
		return this.amount.compareTo(BigDecimal.ZERO) < 0;
	}

	/**
	 * Returns whether the current {@link Quantity} is zero or negative.
	 *
	 * @return
	 */
	@Transient
	public boolean isZeroOrNegative() {
		return !isGreaterThan(toZero());
	}

	/**
	 * Returns a new {@link Quantity} of zero with the {@link Metric} of the current one.
	 *
	 * @return will never be {@literal null}.
	 */
	public Quantity toZero() {
		return Quantity.of(0, metric);
	}

	/**
	 * Returns the current {@link Quantity} as units, flattening all non unit metrics into a single unit.
	 *
	 * @return will never be {@literal null}.
	 * @since 7.5
	 */
	public Quantity toUnit() {
		return metric.equals(Metric.UNIT) ? this : Quantity.of(1, Metric.UNIT);
	}

	/**
	 * Returns the negated {@link Quantity}.
	 *
	 * @return will never be {@literal null}.
	 * @since 7.5
	 */
	public Quantity negate() {
		return new Quantity(amount.negate(), metric);
	}

	private void assertCompatibility(Quantity quantity) {

		Assert.notNull(quantity, "Quantity must not be null!");

		if (this == NONE || quantity == NONE) {
			return;
		}

		if (!isCompatibleWith(quantity.metric)) {
			throw new MetricMismatchException(String.format(INCOMPATIBLE, this, quantity), metric, quantity.metric);
		}
	}

	// Tweaks to properly support long-based Quantities for UNIT metric

	void setMetric(Metric metric) {

		this.metric = metric;

		if (amount != null && Metric.UNIT == metric) {
			this.amount = BigDecimal.valueOf(amount.longValue());
		}
	}

	void setAmount(BigDecimal amount) {

		this.amount = amount;

		if (Metric.UNIT == this.metric) {
			this.amount = BigDecimal.valueOf(amount.longValue());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		DecimalFormat format = new DecimalFormat();
		format.setMinimumFractionDigits(amount.scale());

		return format.format(amount).concat(metric.getAbbreviation());
	}
}
