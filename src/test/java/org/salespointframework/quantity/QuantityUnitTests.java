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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Unit tests for {@link Quantity}.
 *
 * @author Oliver Gierke
 * @author Paul Henke
 */
public class QuantityUnitTests {

	/**
	 * @see #9
	 */
	@Test
	public void defaultsToUnitAsMetric() {
		assertThat(Quantity.of(1).getMetric(), is(Metric.UNIT));
	}

	/**
	 * @see #9
	 */
	@Test(expected = IllegalArgumentException.class)
	public void rejectsNullMetric() {
		Quantity.of(0, null);
	}

	/**
	 * @see #9
	 */
	@Test
	public void rejectsIncompatibleMetric() {

		assertThat(Quantity.of(1).isCompatibleWith(Metric.UNIT), is(true));
		assertThat(Quantity.of(1).isCompatibleWith(Metric.KILOGRAM), is(false));
	}

	/**
	 * @see #9
	 */
	@Test(expected = MetricMismatchException.class)
	public void rejectsIncompatibleQuantityOnAddition() {
		Quantity.of(1).add(Quantity.of(1, Metric.KILOGRAM));
	}

	/**
	 * @see #9
	 */
	@Test
	public void addsQuantitiesCorrectly() {

		assertThat(Quantity.of(1).add(Quantity.of(1)), is(Quantity.of(2)));
		assertThat(Quantity.of(1.5).add(Quantity.of(1.5)), is(Quantity.of(3.0)));
	}

	/**
	 * @see #9
	 */
	@Test(expected = MetricMismatchException.class)
	public void rejectsIncompatibleQuantityOnSubtraction() {
		Quantity.of(1).subtract(Quantity.of(1, Metric.KILOGRAM));
	}

	/**
	 * @see #64, #65, #9
	 */
	@Test
	public void subtractsQuantitiesCorrectly() {

		assertThat(Quantity.of(10).subtract(Quantity.of(1)), is(Quantity.of(9)));
		assertThat(Quantity.of(10.5).subtract(Quantity.of(7.25)), is(Quantity.of(3.25)));
	}

	/**
	 * @see #34, #9
	 */
	@Test
	public void comparesQuantitiesCorrectly() {

		Quantity five = Quantity.of(5);
		Quantity ten = Quantity.of(10);

		assertThat(five.isLessThan(ten), is(true));
		assertThat(five.isGreaterThan(ten), is(false));

		assertThat(ten.isGreaterThan(five), is(true));
		assertThat(ten.isLessThan(five), is(false));

		assertThat(ten.isGreaterThanOrEqualTo(ten), is(true));
		assertThat(ten.isGreaterThanOrEqualTo(five), is(true));
	}

	/**
	 * @see #9
	 */
	@Test
	public void discoversNegativeQuantity() {

		assertThat(Quantity.of(-1).isNegative(), is(true));
		assertThat(Quantity.of(0).isNegative(), is(false));
		assertThat(Quantity.of(1).isNegative(), is(false));
	}

	/**
	 * @see #99
	 */
	@Test
	public void printsReasonableToString() {

		assertThat(Quantity.of(5).toString(), is("5"));
		assertThat(Quantity.of(5, Metric.LITER).toString(), is("5l"));
		assertThat(Quantity.of(5.0, Metric.LITER).toString(), is("5l"));
		assertThat(Quantity.of(5.1, Metric.LITER).toString(), is("5,1l"));
		assertThat(Quantity.of(5.11, Metric.LITER).toString(), is("5,11l"));
	}

	/**
	 * @see #129
	 */
	@Test
	public void comparesToZero() {

		Quantity quantity = Quantity.of(5);
		Quantity zero = Quantity.of(0, Metric.LITER);

		assertThat(quantity.isGreaterThan(quantity.toZero()), is(true));
		assertThat(zero.equals(zero.toZero()), is(true));
	}
}
