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

import javax.money.MonetaryAmount;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * JPA {@link AttributeConverter} to serialize {@link Quantity} instances into a {@link String}. Auto-applied to all
 * entity properties of type {@link MonetaryAmount}.
 *
 * @author Oliver Gierke
 */
@Converter(autoApply = true)
public class QuantityAttributeConverter implements AttributeConverter<Quantity, String> {

	/*
	 * (non-Javadoc)
	 * @see javax.persistence.AttributeConverter#convertToDatabaseColumn(java.lang.Object)
	 */
	@Override
	public String convertToDatabaseColumn(Quantity quantity) {

		if (quantity == null) {
			return null;
		}

		var abbreviation = quantity.getMetric().getAbbreviation();
		var amount = quantity.getAmount();

		// Cannot be inlined further as the java compiler otherwise derives double
		// as type and immediately converts the long value into a double

		var value = amount.scale() == 0 //
				? String.valueOf(amount.longValue()) //
				: String.valueOf(amount.doubleValue());

		return String.format("%s %s", value, abbreviation).trim();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.persistence.AttributeConverter#convertToEntityAttribute(java.lang.Object)
	 */
	@Override
	public Quantity convertToEntityAttribute(String source) {

		if (source == null) {
			return null;
		}

		var parts = source.split(" ");
		var metric = parts.length == 1 ? Metric.UNIT : Metric.from(parts[1]);

		return parts[0].contains(".") //
				? Quantity.of(Double.parseDouble(parts[0]), metric) //
				: Quantity.of(Long.parseLong(parts[0]), metric);
	}
}
