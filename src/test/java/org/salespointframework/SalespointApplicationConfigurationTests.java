/*
 * Copyright 2017-2018 the original author or authors.
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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import de.olivergierke.modulith.docs.Documenter;
import de.olivergierke.moduliths.model.Modules;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.catalog.Product;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.inventory.Inventory;
import org.salespointframework.inventory.InventoryItem;
import org.salespointframework.inventory.LineItemFilter;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderManager;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.AuthenticationManager;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

/**
 * Integration test to bootstrap the application configuration.
 * 
 * @author Oliver Gierke
 */
public class SalespointApplicationConfigurationTests extends AbstractIntegrationTests {

	@Autowired Inventory<InventoryItem> inventory;
	@Autowired OrderManager<Order> orderManager;
	@Autowired Catalog<Product> product;
	@Autowired BusinessTime businessTime;
	@Autowired UserAccountManager userAccountManager;
	@Autowired AuthenticationManager authenticationManager;
	@Autowired List<HandlerMethodArgumentResolver> argumentResolvers;
	@Autowired List<DataInitializer> initializer;
	@Autowired MailSender mailSender;
	@Autowired List<LineItemFilter> lineItemFilter;

	@Test
	public void verifyModularity() throws IOException {

		// Verify module structure
		Modules modules = Modules.of(SalespointSample.class);
		modules.verify();

		// Generate documentation
		Documenter documenter = new Documenter(modules);
		documenter.writePlantUml();
	}

	@Test
	public void createsApplicationComponents() {

		assertThat(inventory, is(notNullValue()));
		assertThat(orderManager, is(notNullValue()));
		assertThat(product, is(notNullValue()));
		assertThat(userAccountManager, is(notNullValue()));
		assertThat(businessTime, is(notNullValue()));
		assertThat(authenticationManager, is(notNullValue()));
		assertThat(argumentResolvers, hasSize(1));
		assertThat(initializer, is(not(emptyIterable())));
		assertThat(lineItemFilter, hasSize(1));

		assertThat(mailSender, is(instanceOf(JavaMailSenderImpl.class)));

		JavaMailSenderImpl impl = (JavaMailSenderImpl) mailSender;
		assertThat(impl.getUsername(), is("username"));
		assertThat(impl.getHost(), is("host"));
		assertThat(impl.getPassword(), is("password"));
	}

	@EnableSalespoint
	static class SalespointSample {}
}
