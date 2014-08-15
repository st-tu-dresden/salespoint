package org.salespointframework.support;

import org.salespointframework.useraccount.Role;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts a {@link String} to a {@link Role}.
 * 
 * @author Paul Henke
 * @author Oliver Gierke
 */
@Component
class StringToRoleConverter implements Converter<String, Role> {

	/*
	 * 
	 * (non-Javadoc)
	 * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
	 */
	@Override
	public Role convert(String text) {
		return new Role(text);
	}
}
