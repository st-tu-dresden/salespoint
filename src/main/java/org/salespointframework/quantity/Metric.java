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

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

import org.springframework.util.Assert;

/**
 * All available metrics.
 *
 * @author Oliver Gierke
 */
@Getter
@RequiredArgsConstructor
public enum Metric {

	SQUARE_METER("m²", "m2"), METER("m"), KILOGRAM("kg"), LITER("l"), UNIT("");

	private final String abbreviation;
	private final List<String> abbreviations;

	/**
	 * Creates a new {@link Metric} with the given primary abbreviation and a possibly additional ones that are used for
	 * parsing {@link Metric} instances from {@link String} sources.
	 * 
	 * @param primaryAbbreviation must not be {@literal null}.
	 * @param additionalAbbreviations must not be {@literal null}, see {@link #from(String)}.
	 */
	private Metric(String primaryAbbreviation, String... additionalAbbreviations) {

		Assert.notNull(primaryAbbreviation, "Primary abbreviation must not be null!");
		Assert.notNull(additionalAbbreviations, "Additional abbreviations must not be null!");

		this.abbreviation = primaryAbbreviation;
		this.abbreviations = Arrays.asList(additionalAbbreviations);
	}

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
	 * @param abbreviation must not be {@literal null}.
	 * @return will never be {@literal null}.
	 * @throws IllegalArgumentException if no {@link Metric} can be found for the given abbreviation.
	 */
	public static Metric from(String abbreviation) {

		Assert.notNull(abbreviation, "Abbreviation source must not be null!");

		String source = abbreviation.trim();

		return Arrays.stream(Metric.values()) //
				.filter(it -> it.abbreviation.equals(source) || it.abbreviations.contains(source)) //
				.findFirst() //
				.orElseThrow(() -> new IllegalArgumentException(String.format("Unsupported abbreviation %s!", abbreviation)));
	}
}
