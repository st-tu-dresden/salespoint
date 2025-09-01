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
package org.salespointframework.quantity;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;

/**
 * Unit tests for {@link Quantity}.
 *
 * @author Oliver Gierke
 * @author Paul Henke
 */
class QuantityUnitTests {

	@Test // #9
	void defaultsToUnitAsMetric() {
		assertThat(Quantity.of(1).getMetric()).isEqualTo(Metric.UNIT);
	}

	@Test // #9
	void rejectsNullMetric() {

		assertThatExceptionOfType(IllegalArgumentException.class) //
				.isThrownBy(() -> Quantity.of(0, null));
	}

	@Test // #9
	void rejectsIncompatibleMetric() {

		assertThat(Quantity.of(1).isCompatibleWith(Metric.UNIT)).isTrue();
		assertThat(Quantity.of(1).isCompatibleWith(Metric.KILOGRAM)).isFalse();
	}

	@Test // #9
	void rejectsIncompatibleQuantityOnAddition() {

		assertThatExceptionOfType(MetricMismatchException.class) //
				.isThrownBy(() -> Quantity.of(1).add(Quantity.of(1, Metric.KILOGRAM)));
	}

	@Test // #9
	void addsQuantitiesCorrectly() {

		assertThat(Quantity.of(1).add(Quantity.of(1))).isEqualTo(Quantity.of(2));
		assertThat(Quantity.of(1.5).add(Quantity.of(1.5))).isEqualTo(Quantity.of(3.0));
	}

	@Test // #9
	void rejectsIncompatibleQuantityOnSubtraction() {

		assertThatExceptionOfType(MetricMismatchException.class) //
				.isThrownBy(() -> Quantity.of(1).subtract(Quantity.of(1, Metric.KILOGRAM)));
	}

	@Test // #64, #65, #9
	void subtractsQuantitiesCorrectly() {

		assertThat(Quantity.of(10).subtract(Quantity.of(1))).isEqualTo(Quantity.of(9));
		assertThat(Quantity.of(10.5).subtract(Quantity.of(7.25))).isEqualTo(Quantity.of(3.25));
	}

	@Test // #34, #9
	void comparesQuantitiesCorrectly() {

		var five = Quantity.of(5);
		var ten = Quantity.of(10);

		assertThat(five.isLessThan(ten)).isTrue();
		assertThat(five.isGreaterThan(ten)).isFalse();

		assertThat(ten.isGreaterThan(five)).isTrue();
		assertThat(ten.isLessThan(five)).isFalse();

		assertThat(ten.isGreaterThanOrEqualTo(ten)).isTrue();
		assertThat(ten.isGreaterThanOrEqualTo(five)).isTrue();
	}

	@Test // #9
	void discoversNegativeQuantity() {

		assertThat(Quantity.of(-1).isNegative()).isTrue();
		assertThat(Quantity.of(0).isNegative()).isFalse();
		assertThat(Quantity.of(1).isNegative()).isFalse();
	}

	@Test // #99, #184
	void printsReasonableToString() {

		assertThat(Quantity.of(5).toString()).isEqualTo("5");
		assertThat(Quantity.of(5, Metric.LITER).toString()).containsSubsequence("5", "l");
		assertThat(Quantity.of(5.0, Metric.LITER).toString()).containsSubsequence("5", "0", "l");
		assertThat(Quantity.of(5.1, Metric.LITER).toString()).containsSubsequence("5", "1", "l");
		assertThat(Quantity.of(5.11, Metric.LITER).toString()).containsSubsequence("5", "11", "l");
	}

	@Test // #129
	void comparesToZero() {

		var quantity = Quantity.of(5);
		var zero = Quantity.of(0, Metric.LITER);

		assertThat(quantity.isGreaterThan(quantity.toZero())).isTrue();
		assertThat(zero.equals(zero.toZero())).isTrue();
	}

	@Test
	void addingQuantityToNoneIsQuantity() {

		var quantity = Quantity.of(5);

		assertThat(Quantity.NONE.add(quantity)).isEqualTo(quantity);
		assertThat(quantity.add(Quantity.NONE)).isEqualTo(quantity);
	}

	@Test // #250
	void comparesToZeroOrNegativeWithDifferentScale() {
		assertThat(Quantity.of(0.0, Metric.UNIT).isZeroOrNegative()).isTrue();
	}

	@ParameterizedTest(name = "{0} is compatible with Quantity.NONE") // #163
	@EnumSource(Metric.class)
	void noneQuantityIsCompatibleWithAllMetrics(Metric metric) {
		assertThat(Quantity.NONE.isCompatibleWith(metric));
	}

	@ParameterizedTest(name = " 0 {0} is not compatible with unit") // #163
	@EnumSource(value = Metric.class, names = "UNIT", mode = Mode.EXCLUDE)
	void zeroMetricIsNotCompatibleWithAnyOther(Metric metric) {
		assertThat(Quantity.of(0, metric).isCompatibleWith(Metric.UNIT)).isFalse();
	}

	@Test // #251
	void multipliesCorrectly() {

		assertThat(Quantity.of(5.5).times(2)).isEqualTo(Quantity.of(11.0));
		assertThat(Quantity.of(5.5).times(2l)).isEqualTo(Quantity.of(11.0));
	}

	@Test // #284
	void considersValuesOfDifferentPrecisionStructurallyEqual() {

		var left = Quantity.of(1);
		var right = Quantity.of(1.0);

		assertThat(left.isEqualTo(right)).isTrue();
		assertThat(left).isNotEqualTo(right);
	}

	@Test // #368
	void substractsFromNoneCorrectly() {
		assertThat(Quantity.NONE.subtract(Quantity.of(5))).isEqualTo(Quantity.of(-5));
	}

	@Test // #365
	void detectsPositiveValues() {

		assertThat(Quantity.of(1).isPositive()).isTrue();
		assertThat(Quantity.of(1).isZeroOrPositive()).isTrue();

		assertThat(Quantity.NONE.isPositive()).isFalse();
		assertThat(Quantity.NONE.isZeroOrPositive()).isTrue();

		assertThat(Quantity.of(-1).isPositive()).isFalse();
		assertThat(Quantity.of(-1).isZeroOrPositive()).isFalse();
	}

	@Test // #366
	void unifiesQuantity() {

		assertThat(Quantity.of(5).toUnit()).isEqualTo(Quantity.of(5));
		assertThat(Quantity.of(5, Metric.LITER).toUnit()).isEqualTo(Quantity.of(1));
	}
}
