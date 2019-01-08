/*
 * Copyright 2017-2019 the original author or authors.
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

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link Metric}.
 * 
 * @author Oliver Gierke
 * @soundtrack Dave Matthews Band - JTR (DMB Live 25)
 */
class MetricUnitTests {

	@Test // #185
	void parsesMetricFromSource() {

		assertThat(Metric.from("l")).isEqualTo(Metric.LITER);
		assertThat(Metric.from("  l  ")).isEqualTo(Metric.LITER);
	}

	@Test // #195
	void parsesMetricFromAdditionalAbbreviations() {
		assertThat(Metric.from("m2")).isEqualTo(Metric.SQUARE_METER);
	}

	@Test // #195
	void rejectsNullAbbreviationSource() {
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> Metric.from(null));
	}
}
