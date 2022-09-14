/*
 * Copyright 2020-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.salespointframework.quantity;

import lombok.RequiredArgsConstructor;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A {@link Converter} implementation that is registered for application property conversion (via
 * {@link ConfigurationPropertiesBinding}).
 *
 * @author Oliver Drotbohm
 * @since 7.3
 */
@Component
@RequiredArgsConstructor
@ConfigurationPropertiesBinding
class StringToQuantityConverter implements Converter<String, Quantity> {

	private final QuantityFormatter formatter;

	/*
	 * (non-Javadoc)
	 * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
	 */
	@Override
	public Quantity convert(String source) {

		try {

			return formatter.parse(source, Locale.US);

		} catch (ParseException o_O) {

			var sourceDescriptor = TypeDescriptor.valueOf(String.class);
			var targetDescriptor = TypeDescriptor.valueOf(Quantity.class);

			throw new ConversionFailedException(sourceDescriptor, targetDescriptor, source, o_O);
		}
	}
}
