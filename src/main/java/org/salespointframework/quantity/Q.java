/*
 * Copyright 2014 the original author or authors.
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

import java.math.BigDecimal;

/**
 * @author Oliver Gierke
 */
public class Q {

	BigDecimal value;
	MyMetric metric;

	protected Q(BigDecimal value, MyMetric metric) {
		this.value = value;
		this.metric = metric;
	}

	public Q(double value, MyMetric metric) {

	}

	public static QuantityBuilder of(BigDecimal value) {
		return new QuantityBuilder(value);
	}

	public static QuantityBuilder of(long value) {
		return new QuantityBuilder(new BigDecimal(value));
	}

	public Q add(Q other) {
		return null;
	}

	static class QuantityBuilder {

		BigDecimal value;

		private QuantityBuilder(BigDecimal value) {
			this.value = value;
		}

		public Q kilogram() {
			return new Q(value, MyMetric.KILOGRAM);
		}
	}

	public static void main(String[] args) {

		Q threeKg = of(3).kilogram();

		Q quantity = new Q(3.5, MyMetric.KILOGRAM);
	}
}

enum MyMetric {
	METER, KILOGRAM, LITRE, UNIT;
}
