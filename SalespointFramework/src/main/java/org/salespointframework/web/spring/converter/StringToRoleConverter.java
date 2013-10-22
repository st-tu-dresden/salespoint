package org.salespointframework.web.spring.converter;

import org.salespointframework.core.useraccount.Role;
import org.springframework.core.convert.converter.Converter;

/**
 * Converts a {@link String} to a {@link Role}
 * @author Paul Henke
 *
 */
public class StringToRoleConverter implements Converter<String, Role> {

	@Override
	public Role convert(String text) {
		return new Role(text);
	}
}
