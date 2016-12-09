package org.salespointframework;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.javamoney.moneta.Money;
import org.junit.Test;

/**
 * Unit tests for {@link MonetaryAmountAttributeConverter}
 *
 * @author Oliver Gierke
 */
public class MonetaryAmountAttributeConverterTest {

	MonetaryAmountAttributeConverter converter = new MonetaryAmountAttributeConverter();

	/**
	 * @see #156
	 */
	@Test
	public void handlesNullValues() {

		assertThat(converter.convertToDatabaseColumn(null), is(nullValue()));
		assertThat(converter.convertToEntityAttribute(null), is(nullValue()));
	}

	/**
	 * @see #156
	 */
	@Test
	public void handlesSimpleValue() {

		assertThat(converter.convertToDatabaseColumn(Money.of(1.23, "EUR")), is("EUR 1.23"));
		assertThat(converter.convertToEntityAttribute("EUR 1.23"), is(Money.of(1.23, "EUR")));
	}

	/**
	 * @see #156
	 */
	@Test
	public void handlesNegativeValues() {

		assertThat(converter.convertToDatabaseColumn(Money.of(-1.20, "USD")), is("USD -1.2"));
		assertThat(converter.convertToEntityAttribute("USD -1.2"), is(Money.of(-1.20, "USD")));
	}

	/**
	 * @see #156
	 */
	@Test
	public void doesNotRoundValues() {
		assertThat(converter.convertToDatabaseColumn(Money.of(1.23456, "EUR")), is("EUR 1.23456"));
	}

	/**
	 * @see #156
	 */
	@Test
	public void doesNotFormatLargeValues() {
		assertThat(converter.convertToDatabaseColumn(Money.of(123456, "EUR")), is("EUR 123456"));
	}

	/**
	 * @see #156
	 */
	@Test
	public void deserializesFormattedValues() {
		assertThat(converter.convertToEntityAttribute("EUR 123,456.78"), is(Money.of(123456.78, "EUR")));
	}

	/**
	 * @see #156
	 */
	@Test
	public void convertsValuesWithTrailingZerosCorrectly() {

		Money reference = Money.of(100.0, "EUR");

		assertThat(converter.convertToEntityAttribute(converter.convertToDatabaseColumn(reference)), is(reference));
	}
}
