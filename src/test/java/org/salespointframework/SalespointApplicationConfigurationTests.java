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
package org.salespointframework;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.catalog.Product;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.inventory.LineItemFilter;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderManagement;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.AuthenticationManagement;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

/**
 * Integration test to bootstrap the application configuration.
 *
 * @author Oliver Gierke
 */
class SalespointApplicationConfigurationTests extends AbstractIntegrationTests {

	@Autowired UniqueInventory<UniqueInventoryItem> inventory;
	@Autowired OrderManagement<Order> orderManager;
	@Autowired Catalog<Product> product;
	@Autowired BusinessTime businessTime;
	@Autowired UserAccountManagement userAccountManagement;
	@Autowired AuthenticationManagement authenticationManagement;
	@Autowired List<HandlerMethodArgumentResolver> argumentResolvers;
	@Autowired List<DataInitializer> initializer;
	@Autowired MailSender mailSender;
	@Autowired List<LineItemFilter> lineItemFilter;

	@Autowired Environment environment;

	@Test
	void verifyModularity() throws IOException {
		ApplicationModules.of(SalespointSample.class).verify();
	}

	@Test
	void createsApplicationComponents() {

		assertThat(inventory).isNotNull();
		assertThat(orderManager).isNotNull();
		assertThat(product).isNotNull();
		assertThat(userAccountManagement).isNotNull();
		assertThat(businessTime).isNotNull();
		assertThat(authenticationManagement).isNotNull();
		assertThat(argumentResolvers).hasSize(1);
		assertThat(initializer).isNotEmpty();
		assertThat(lineItemFilter).hasSize(1);

		assertThat(mailSender).isInstanceOfSatisfying(JavaMailSenderImpl.class, impl -> {
			assertThat(impl.getUsername()).isEqualTo("username");
			assertThat(impl.getHost()).isEqualTo("host");
			assertThat(impl.getPassword()).isEqualTo("password");
		});
	}

	@Test // #266
	void configuresEmbeddedDatabaseWithGeneratedName() {
		assertThat(environment.getRequiredProperty("spring.datasource.generate-unique-name", boolean.class)).isTrue();
	}

	@EnableSalespoint("Salespoint")
	static class SalespointSample {}
}
