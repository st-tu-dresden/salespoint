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

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Locale;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.format.Formatter;
import org.springframework.format.number.NumberStyleFormatter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * A dedicated Spring {@link Formatter} to print and parse {@link Quantity} instances. Uses a
 * {@link NumberStyleFormatter} for parsing of the amount.
 *
 * @author Oliver Gierke
 * @see NumberStyleFormatter
 * @since 6.4.1
 * @soundtrack Dave Matthews Band - Bartender (DMB Live 25)
 */
@Component
class QuantityFormatter implements Formatter<Quantity> {

	private static final Pattern QUANTITY_PATTERN;
	private static final NumberStyleFormatter NUMBER_FORMATTER = new NumberStyleFormatter();

	static {

		StringBuilder builder = new StringBuilder();

		builder.append("("); // group 1 start
		builder.append("[+-]?"); // optional sign
		builder.append("\\d*[\\.\\,]?\\d*"); // decimals with . or , in between
		builder.append(")"); // group 1 end

		builder.append("("); // group 2 start
		builder.append("\\s*\\w*"); // any kind of text as metric source
		builder.append(")"); // group 2 end;

		QUANTITY_PATTERN = Pattern.compile(builder.toString());
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.format.Printer#print(java.lang.Object, java.util.Locale)
	 */
	@Override
	public String print(Quantity object, Locale locale) {

		return String.format("%s%s", NUMBER_FORMATTER.print(object.getAmount(), locale),
				object.getMetric().getAbbreviation());
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.format.Parser#parse(java.lang.String, java.util.Locale)
	 */
	@Override
	public Quantity parse(String text, Locale locale) throws ParseException {

		if (!StringUtils.hasText(text)) {
			return Quantity.of(0);
		}

		Matcher matcher = QUANTITY_PATTERN.matcher(text.trim());

		if (!matcher.matches()) {
			throw new ParseException(text, 0);
		}

		Metric metric = parseMetric(matcher.group(2), o_O -> new ParseException(matcher.group(2), matcher.start(2)));

		Number number = NUMBER_FORMATTER.parse(matcher.group(1), locale);

		return number instanceof BigDecimal //
				? Quantity.of((BigDecimal) number, metric) //
				: Quantity.of(number.doubleValue(), metric);
	}

	/**
	 * Tries to create a {@link Metric} from the given source and reports invalid attempts as {@link ParseException}
	 * applying the given exception mapper.
	 *
	 * @param source the source value that symbolizes the {@link Metric}
	 * @param index the index at which the metric part was
	 * @return
	 * @throws ParseException in case the creation of a {@link Metric} fails for the given source.
	 */
	private static Metric parseMetric(String source, Function<IllegalArgumentException, ParseException> exceptionMapper)
			throws ParseException {

		try {
			return StringUtils.hasText(source) ? Metric.from(source) : Metric.UNIT;
		} catch (IllegalArgumentException o_O) {
			throw exceptionMapper.apply(o_O);
		}
	}
}
