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
package org.salespointframework.support;

import static org.assertj.core.api.Assertions.*;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;

/**
 * Tests for {@link QuantityFormatter}.
 * 
 * @author Oliver Gierke
 * @soundtrack Dave Matthews Band - Bartender (DMB Live 25)
 */
@RunWith(Suite.class)
@SuiteClasses({ //
		QuantityFormatterUnitTests.QuantityFormatterValueTests.class, //
		QuantityFormatterUnitTests.QuantityFormatterExceptionTests.class //
})
public class QuantityFormatterUnitTests {

	/**
	 * Tests to check exceptional cases.
	 *
	 * @author Oliver Gierke
	 */
	public static class QuantityFormatterExceptionTests {

		QuantityFormatter formatter = new QuantityFormatter();

		@Test // #185
		public void throwsParseExceptionIfAmountCantBeParsed() {

			assertThatExceptionOfType(ParseException.class) //
					.isThrownBy(() -> formatter.parse("foo", Locale.GERMAN)) //
					.matches(it -> it.getErrorOffset() == 0) //
					.withMessage("foo");
		}

		@Test // #185
		public void throwsParseExceptionOnInvalidMetric() {

			assertThatExceptionOfType(ParseException.class) //
					.isThrownBy(() -> formatter.parse("5,1foo", Locale.GERMAN)) //
					.matches(it -> it.getErrorOffset() == 3) //
					.withMessage("foo");
		}

		@Test // #185
		public void rejectsCompletelyInvalidSource() {

			assertThatExceptionOfType(ParseException.class) //
					.isThrownBy(() -> formatter.parse("?", Locale.GERMAN)) //
					.matches(it -> it.getErrorOffset() == 0) //
					.withMessage("?");
		}

		@Test // #185
		public void emptyStringParsesToZeroUnit() throws ParseException {
			assertThat(formatter.parse("", Locale.GERMAN)).isEqualTo(Quantity.of(0));
		}

		@Test // #185
		public void ignoresSpacesBetweenAmountAndMetricOnParsing() throws ParseException {
			assertThat(formatter.parse("5,1   l", Locale.GERMAN)).isEqualTo(Quantity.of(5.1, Metric.LITER));
		}
	}

	/**
	 * Value based tests to check for simple printing and parsing results.
	 *
	 * @author Oliver Gierke
	 */
	@RunWith(Parameterized.class)
	public static class QuantityFormatterValueTests {

		QuantityFormatter formatter = new QuantityFormatter();

		@Parameters(name = "{0} {1} -> {2}")
		public static Collection<Object[]> data() {

			return Arrays.asList(new Object[][] { //
					{ Quantity.of(5.1, Metric.LITER), Locale.GERMAN, "5,1l" }, //
					{ Quantity.of(5.1), Locale.GERMAN, "5,1" }, //
					{ Quantity.of(-5.1, Metric.LITER), Locale.GERMAN, "-5,1l" }, //
					{ Quantity.of(-5.1, Metric.LITER), Locale.GERMAN, " -5,1l" }, //
					{ Quantity.of(-5.1, Metric.LITER), Locale.US, "-5.1l" }, //
					{ Quantity.of(0), Locale.US, "0" } //
			});
		}

		public @Parameter(0) Quantity quantity;
		public @Parameter(1) Locale locale;
		public @Parameter(2) String value;

		@Test // #185
		public void printsValueAsExpected() throws ParseException {
			assertThat(formatter.print(quantity, locale)).isEqualTo(value.trim());
		}

		@Test // #185
		public void parsesValueAsExpected() throws ParseException {
			assertThat(formatter.parse(value, locale)).isEqualTo(quantity);
		}
	}
}
