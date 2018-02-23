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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Integration test for security configuration setup.
 * 
 * @author Oliver Gierke
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SalespointSecurityConfigurationIntegrationTests {

	public @Rule ExpectedException exception = ExpectedException.none();

	@Configuration
	@Import({ SalespointSecurityConfiguration.class, Salespoint.class })
	static class Config {

		@Bean
		public Controller securedController() {
			return new Controller();
		}
	}

	@Autowired Controller controller;

	@Test // #41
	public void preventsInvocationOfSecuredMethodWithoutAuthentication() {

		exception.expect(AuthenticationCredentialsNotFoundException.class);
		controller.securedMethod();
	}

	/**
	 * Sample secured controller.
	 *
	 * @author Oliver Gierke
	 */
	@PreAuthorize("hasRole(ROLE_ADMIN)")
	public static class Controller {
		public void securedMethod() {}
	}
}
