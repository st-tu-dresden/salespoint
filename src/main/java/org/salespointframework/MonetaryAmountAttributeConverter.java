package org.salespointframework;

import java.util.Locale;

import javax.money.MonetaryAmount;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.javamoney.moneta.Money;

/**
 * JPA {@link AttributeConverter} to serialize {@link MonetaryAmount} instances into a {@link String}. Auto-applied to
 * all entity properties of type {@link MonetaryAmount}.
 * 
 * @author Oliver Gierke
 */
@Converter(autoApply = true)
public class MonetaryAmountAttributeConverter implements AttributeConverter<MonetaryAmount, String> {

	private static final MonetaryAmountFormat FORMAT = MonetaryFormats.getAmountFormat(Locale.ROOT);

	/*
	 * (non-Javadoc)
	 * @see javax.persistence.AttributeConverter#convertToDatabaseColumn(java.lang.Object)
	 */
	@Override
	public String convertToDatabaseColumn(MonetaryAmount amount) {
		return amount == null ? null : amount.toString();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.persistence.AttributeConverter#convertToEntityAttribute(java.lang.Object)
	 */
	@Override
	public MonetaryAmount convertToEntityAttribute(String source) {

		if (source == null) {
			return null;
		}

		try {
			return Money.parse(source);
		} catch (RuntimeException e) {
			return Money.parse(source, FORMAT);
		}
	}
}
