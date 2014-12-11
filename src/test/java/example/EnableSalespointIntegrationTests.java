/*
 * Copyright 2014 the original author or authors.
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Integration tests for {@link EnableSalespoint}.
 * 
 * @author Oliver Gierke
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration
public class EnableSalespointIntegrationTests {

	@Configuration
	@EnableSalespoint
	static class TestConfiguration {}

	@Autowired ApplicationContext context;
	@Autowired Catalog<Product> catalog;
	@Autowired DummyRepository repository;

	@Test
	public void componentsFromSalespointAndExampleAreAvailable() {

		assertThat(context, is(notNullValue()));
		assertThat(repository, is(notNullValue()));
		assertThat(catalog, is(notNullValue()));
	}
}
