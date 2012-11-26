package org.salespointframework.web.spring.converter;

import org.salespointframework.core.quantity.Units;
import org.springframework.core.convert.converter.Converter;

/**
 * 
 * @author Paul Henke
 *
 */
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
