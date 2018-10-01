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
package org.salespointframework.quantity;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.junit.MatcherAssert.*;

import org.junit.jupiter.api.Test;

/**
 * @author Oliver Gierke
 */
class QuantityAttributeConverterUnitTests {

	QuantityAttributeConverter converter = new QuantityAttributeConverter();

	@Test
	void convertsValueWithDoubleAndMetric() {
		assertCovertedPair(Quantity.of(3.14, Metric.LITER), "3.14 l");
	}

	@Test
	void convertsValueWithLongAndMetric() {
		assertCovertedPair(Quantity.of(3L, Metric.LITER), "3 l");
	}

	@Test
	void convertsValueWithLongWithoutMetric() {
		assertCovertedPair(Quantity.of(3L), "3");
	}

	@Test
	void convertsDoubleWithoutMetric() {
		assertCovertedPair(Quantity.of(10d / 3), "3.3333333333333335");
	}

	@Test
	void convertsNullToNull() {
		assertCovertedPair(null, null);
	}

	private void assertCovertedPair(Quantity quantity, String value) {

		assertThat(converter.convertToDatabaseColumn(quantity), is(value));
		assertThat(converter.convertToEntityAttribute(value), is(quantity));
	}
}
