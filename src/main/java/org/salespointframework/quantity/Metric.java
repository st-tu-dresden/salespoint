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

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.util.Assert;

/**
 * All available metrics.
 *
 * @author Oliver Gierke
 */
@Getter
@RequiredArgsConstructor
public enum Metric {

	METER("m"), KILOGRAM("kg"), LITER("l"), UNIT("");

	private final String abbreviation;

	/**
	 * Returns whether the given {@link Metric} is
	 * 
	 * @param metric must not be {@literal null}.
	 * @return
	 */
	public boolean isCompatibleWith(Metric metric) {

		Assert.notNull(metric, "Metric must not be null!");

		return this.equals(metric);
	}

	/**
	 * Returns the {@link Metric} for the given abbreviation.
	 * 
	 * @param abbreviation
	 * @return will never be {@literal null}.
	 * @throws IllegalArgumentException if no {@link Metric} can be found for the given abbreviation.
	 */
	public static Metric from(String abbreviation) {

		for (Metric metric : Metric.values()) {
			if (metric.getAbbreviation().equals(abbreviation)) {
				return metric;
			}
		}

		throw new IllegalArgumentException(String.format("Unsupported abbreviation %s!", abbreviation));
	}
}
