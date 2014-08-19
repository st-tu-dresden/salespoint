package org.salespointframework.support;

import org.salespointframework.quantity.Units;
import org.springframework.core.convert.converter.Converter;

/**
 * @author Paul Henke
 */
@Deprecated
public class StringToUnitsConverter implements Converter<String, Units> {

	@Override
	public Units convert(String text) {
		// JAva fails hard ... again
		try {
			long l = Long.parseLong(text);
			Units units = Units.of(l);
			return units;

		} catch (NumberFormatException e) {
			throw e;
		}
	}

}
