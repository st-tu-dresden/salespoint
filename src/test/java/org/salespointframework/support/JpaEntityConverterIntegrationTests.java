/*
 * Copyright 2017-2023 the original author or authors.
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
package org.salespointframework.support;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.junit.MatcherAssert.*;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.catalog.Product;
import org.salespointframework.core.Currencies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for {@link JpaEntityConverter}.
 *
 * @author Oliver Gierke
 */
@Transactional
@ApplicationModuleTest(extraIncludes = "org.salespointframework.catalog")
class JpaEntityConverterIntegrationTests {

	static final TypeDescriptor PRODUCT_TYPE = TypeDescriptor.valueOf(Product.class);
	static final TypeDescriptor STRING_TYPE = TypeDescriptor.valueOf(String.class);

	@Autowired JpaEntityConverter converter;
	@Autowired Catalog<Product> catalog;
	@Autowired ApplicationContext context;

	@Test
	void convertsStringIdToProduct() {

		Product product = catalog.save(new Product("iPad", Money.of(400, Currencies.EURO)));
		String identifier = product.getId().toString();

		assertThat(converter.convert(identifier, STRING_TYPE, PRODUCT_TYPE), is((Object) product));
	}
}
