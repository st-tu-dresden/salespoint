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

import static org.assertj.core.api.Assertions.*;

import java.text.ParseException;
import java.util.Locale;
import java.util.stream.Stream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link QuantityFormatter}.
 * 
 * @author Oliver Gierke
 * @soundtrack Dave Matthews Band - Bartender (DMB Live 25)
 */
class QuantityFormatterUnitTests {

	/**
	 * Tests to check exceptional cases.
	 *
	 * @author Oliver Gierke
	 */
	@Nested
	static class QuantityFormatterExceptionTests {

		QuantityFormatter formatter = new QuantityFormatter();

		@Test // #185
		void throwsParseExceptionIfAmountCantBeParsed() {

			assertThatExceptionOfType(ParseException.class) //
					.isThrownBy(() -> formatter.parse("foo", Locale.GERMAN)) //
					.matches(it -> it.getErrorOffset() == 0) //
					.withMessage("foo");
		}

		@Test // #185
		void throwsParseExceptionOnInvalidMetric() {

			assertThatExceptionOfType(ParseException.class) //
					.isThrownBy(() -> formatter.parse("5,1foo", Locale.GERMAN)) //
					.matches(it -> it.getErrorOffset() == 3) //
					.withMessage("foo");
		}

		@Test // #185
		void rejectsCompletelyInvalidSource() {

			assertThatExceptionOfType(ParseException.class) //
					.isThrownBy(() -> formatter.parse("?", Locale.GERMAN)) //
					.matches(it -> it.getErrorOffset() == 0) //
					.withMessage("?");
		}

		@Test // #185
		void emptyStringParsesToZeroUnit() throws ParseException {
			assertThat(formatter.parse("", Locale.GERMAN)).isEqualTo(Quantity.of(0));
		}

		@Test // #185
		void ignoresSpacesBetweenAmountAndMetricOnParsing() throws ParseException {
			assertThat(formatter.parse("5,1   l", Locale.GERMAN)).isEqualTo(Quantity.of(5.1, Metric.LITER));
		}
	}

	/**
	 * Value based tests to check for simple printing and parsing results.
	 *
	 * @author Oliver Gierke
	 */
	@Nested
	static class QuantityFormatterValueTests {

		QuantityFormatter formatter = new QuantityFormatter();

		static Stream<Arguments> data() {

			return Stream.of(Arguments.of(Quantity.of(5.1, Metric.LITER), Locale.GERMAN, "5,1l"), //
					Arguments.of(Quantity.of(5.1), Locale.GERMAN, "5,1"), //
					Arguments.of(Quantity.of(-5.1, Metric.LITER), Locale.GERMAN, "-5,1l"), //
					Arguments.of(Quantity.of(-5.1, Metric.LITER), Locale.GERMAN, " -5,1l"), //
					Arguments.of(Quantity.of(-5.1, Metric.LITER), Locale.US, "-5.1l"), //
					Arguments.of(Quantity.of(0), Locale.US, "0") //
			);
		}

		@ParameterizedTest // #185
		@MethodSource("data")
		void printsValueAsExpected(Quantity quantity, Locale locale, String value) throws ParseException {
			assertThat(formatter.print(quantity, locale)).isEqualTo(value.trim());
		}

		@ParameterizedTest // #185
		@MethodSource("data")
		void parsesValueAsExpected(Quantity quantity, Locale locale, String value) throws ParseException {
			assertThat(formatter.parse(value, locale)).isEqualTo(quantity);
		}
	}
}
