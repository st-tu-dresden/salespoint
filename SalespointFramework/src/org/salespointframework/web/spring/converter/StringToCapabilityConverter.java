package org.salespointframework.web.spring.converter;

import org.salespointframework.core.user.Capability;
import org.springframework.core.convert.converter.Converter;

/**
 * Converts a {@link String} to a {@link Capability}
 * @author Paul Henke
 *
 */
public class StringToCapabilityConverter implements Converter<String, Capability> {

	@Override
	public Capability convert(String text) {
		return new Capability(text);
	}
}
