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
package example;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.salespointframework.EnableSalespoint;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.catalog.Cookie;
import org.salespointframework.catalog.Product.ProductIdentifier;
import org.salespointframework.order.OrderLine;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.web.UserAccountWebTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.security.authorization.method.AuthorizationManagerBeforeMethodInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

/**
 * Integration tests for {@link EnableSalespoint}.
 *
 * @author Oliver Gierke
 */
@SpringBootTest
class EnableSalespointIntegrationTests {

	@EnableSalespoint
	static class TestConfiguration {}

	@Autowired ConfigurableApplicationContext context;
	@Autowired RequestMappingHandlerAdapter adapter;

	@Test
	void componentsFromSalespointAndExampleAreAvailable() {

		assertThat(context.getBeanProvider(DummyRepository.class)).hasSize(1);
		assertThat(context.getBeanProvider(ResolvableType.forClassWithGenerics(Catalog.class, Cookie.class))).isNotEmpty();

		// Assert that @EnableMethodSecurity works
		assertThat(context.getBeanProvider(AuthorizationManagerBeforeMethodInterceptor.class)).isNotEmpty();
	}

	@Autowired FormattingConversionService conversionService;

	@Test // #185
	void registersQuantityFormatter() {

		assertThat(conversionService.canConvert(Quantity.class, String.class)).isTrue();
		assertThat(conversionService.canConvert(String.class, Quantity.class)).isTrue();
	}

	@Test // #241
	void registersSalespointIdentifierConverter() throws Exception {

		// Needs SalespointIdentifierConverter registered
		assertThat(conversionService.canConvert(String.class, ProductIdentifier.class)).isTrue();

		// Needs JpaEntityConverter registered -> OrderLine = non-aggregate root
		assertThat(conversionService.canConvert(String.class, OrderLine.class)).isTrue();
	}

	@Test // #243, #241
	void registersLoggedInHttpMessageConverter() throws Exception {

		assertThat(adapter.getArgumentResolvers()) //
				.hasAtLeastOneElementOfType(UserAccountWebTestUtils.getLoggedInArgumentResolverType());
	}
}
