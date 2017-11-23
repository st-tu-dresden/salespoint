/*
 * Copyright 2017 the original author or authors.
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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.salespointframework.EnableSalespoint;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.catalog.Product;
import org.salespointframework.quantity.Quantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

/**
 * Integration tests for {@link EnableSalespoint}.
 * 
 * @author Oliver Gierke
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class EnableSalespointIntegrationTests {

	@Configuration
	@EnableSalespoint
	static class TestConfiguration {}

	@Autowired ApplicationContext context;
	@Autowired Catalog<Product> catalog;
	@Autowired DummyRepository repository;
	@Autowired Java8TimeDialect dialect;
	@Autowired MethodSecurityMetadataSource securityMetadataSource;

	@Test
	public void componentsFromSalespointAndExampleAreAvailable() {

		assertThat(context, is(notNullValue()));
		assertThat(repository, is(notNullValue()));
		assertThat(catalog, is(notNullValue()));

		// Make sure the web configuration is included if not declared explicitly
		assertThat(dialect, is(notNullValue()));

		// Assert that @EnableGlobalMethodSecurity works
		assertThat(securityMetadataSource, is(notNullValue()));
	}

	@Autowired FormattingConversionService conversionService;

	@Test // #185
	public void registersQuantityFormatter() {

		conversionService.canConvert(Quantity.class, String.class);
		conversionService.canConvert(String.class, Quantity.class);
	}
}
