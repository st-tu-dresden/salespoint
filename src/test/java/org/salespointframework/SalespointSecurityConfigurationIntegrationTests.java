/*
 * Copyright 2017-2022 the original author or authors.
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

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

/**
 * Integration test for security configuration setup.
 *
 * @author Oliver Gierke
 */
@SpringBootTest
class SalespointSecurityConfigurationIntegrationTests {

	@Configuration
	@Import({ Salespoint.class })
	static class Config {

		@Bean
		Controller securedController() {
			return new Controller();
		}
	}

	@Autowired Controller controller;

	@Test // #41
	void preventsInvocationOfSecuredMethodWithoutAuthentication() {

		assertThatExceptionOfType(AuthenticationCredentialsNotFoundException.class) //
				.isThrownBy(() -> controller.securedMethod());
	}

	/**
	 * Sample secured controller.
	 *
	 * @author Oliver Gierke
	 */
	@PreAuthorize("hasRole('ADMIN')")
	static class Controller {
		void securedMethod() {}
	}
}
