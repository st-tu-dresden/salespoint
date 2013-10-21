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
package org.salespointframework;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.salespointframework.core.calendar.Calendar;
import org.salespointframework.core.catalog.Catalog;
import org.salespointframework.core.catalog.Product;
import org.salespointframework.core.catalog.ProductRepository;
import org.salespointframework.core.inventory.Inventory;
import org.salespointframework.core.order.OrderManager;
import org.salespointframework.core.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration test to bootstrap the application configuration.
 *
 * @author Oliver Gierke
 */
@Transactional
public class SalespointApplicationConfigurationTests extends AbstractIntegrationTests {
	
	@Autowired Inventory inventory;
	@Autowired OrderManager orderManager;
	@Autowired Catalog catalog;
	@Autowired Calendar calendar;
	@Autowired ProductRepository<Product> repository;
	
	@Autowired UserAccountManager userAccountManager;

	@Test
	public void createsApplicationComponents() {
		
		assertThat(inventory, is(notNullValue()));
		assertThat(orderManager, is(notNullValue()));
		assertThat(catalog, is(notNullValue()));
		assertThat(calendar, is(notNullValue()));
		assertThat(userAccountManager, is(notNullValue()));
		
		assertThat(repository, is(notNullValue()));
	}
}
