/*
 * Copyright 2013 the original author or authors.
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
package org.salespointframework.support;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.core.SalespointIdentifier;
import org.springframework.core.convert.TypeDescriptor;

/**
 * Units tests for {@link SalespointIdentifierConverter}.
 *
 * @author Oliver Gierke
 */
public class SalespointIdentifierConverterUnitTests {

	private static final TypeDescriptor PRODUCT_IDENTIFIER_DESCRIPTOR = TypeDescriptor.valueOf(ProductIdentifier.class);
	private static final TypeDescriptor STRING_DESCRIPTOR = TypeDescriptor.valueOf(String.class);
	private static final TypeDescriptor OBJECT_DESCRIPTOR = TypeDescriptor.valueOf(Object.class);

	SalespointIdentifierConverter converter = new SalespointIdentifierConverter();

	@Test
	public void matchesForStringSourceSalespointIdentifierTarget() {
		assertThat(converter.matches(STRING_DESCRIPTOR, PRODUCT_IDENTIFIER_DESCRIPTOR), is(true));
	}

	@Test
	public void doesNotMatchForNonStringSource() {
		assertThat(converter.matches(OBJECT_DESCRIPTOR, PRODUCT_IDENTIFIER_DESCRIPTOR), is(false));
	}

	@Test
	public void doesNotMatchForNonSalespointIdentifierTarget() {
		assertThat(converter.matches(STRING_DESCRIPTOR, OBJECT_DESCRIPTOR), is(false));
	}

	@Test
	public void convertsStringToProductIdentifier() {

		Object result = converter.convert("5", STRING_DESCRIPTOR, PRODUCT_IDENTIFIER_DESCRIPTOR);

		assertThat(result, is(instanceOf(ProductIdentifier.class)));
		assertThat(result.toString(), is("5"));
	}

	/**
	 * @see #46
	 */
	@Test
	public void createsIdentifierInstanceFromPrivateConstructor() {

		Object result = converter.convert("5", STRING_DESCRIPTOR, TypeDescriptor.valueOf(MySalespointIdentifier.class));
		assertThat(result, is(instanceOf(MySalespointIdentifier.class)));
	}

	static class MySalespointIdentifier extends SalespointIdentifier {

		private static final long serialVersionUID = 1419011598975206455L;

		public MySalespointIdentifier() {
			super();
		}

		MySalespointIdentifier(String id) {
			super(id);
		}
	}
}
