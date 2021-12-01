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
package example;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.junit.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;
import org.salespointframework.EnableSalespoint;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.catalog.Product;
import org.salespointframework.catalog.Product.ProductIdentifier;
import org.salespointframework.order.OrderLine;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.web.UserAccountWebTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

/**
 * Integration tests for {@link EnableSalespoint}.
 *
 * @author Oliver Gierke
 */
@SpringBootTest
class EnableSalespointIntegrationTests {

	@EnableSalespoint
	static class TestConfiguration {}

	@Autowired ApplicationContext context;
	@Autowired Catalog<Product> catalog;
	@Autowired DummyRepository repository;
	@Autowired MethodSecurityMetadataSource securityMetadataSource;
	@Autowired RequestMappingHandlerAdapter adapter;

	@Test
	void componentsFromSalespointAndExampleAreAvailable() {

		assertThat(context, is(notNullValue()));
		assertThat(repository, is(notNullValue()));
		assertThat(catalog, is(notNullValue()));

		// Assert that @EnableGlobalMethodSecurity works
		assertThat(securityMetadataSource, is(notNullValue()));
	}

	@Autowired FormattingConversionService conversionService;

	@Test // #185
	void registersQuantityFormatter() {

		conversionService.canConvert(Quantity.class, String.class);
		conversionService.canConvert(String.class, Quantity.class);
	}

	@Test // #241
	void registersSalespointIdentifierConverter() throws Exception {

		// Needs SalespointIdentifierConverter registered
		conversionService.canConvert(String.class, ProductIdentifier.class);

		// Needs JpaEntityConverter registered -> OrderLine = non-aggregate root
		conversionService.canConvert(String.class, OrderLine.class);
	}

	@Autowired Java8TimeDialect dialect;

	@Test // #186
	void registersJava8ThymeleafDialect() {
		assertThat(dialect, is(notNullValue()));
	}

	@Test // #243, #241
	void registersLoggedInHttpMessageConverter() throws Exception {

		assertThat(adapter.getArgumentResolvers()) //
				.hasAtLeastOneElementOfType(UserAccountWebTestUtils.getLoggedInArgumentResolverType());
	}
}
