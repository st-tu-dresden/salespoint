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

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.salespointframework.EnableSalespoint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

/**
 * Integration tests for sample components.
 *
 * @author Oliver Gierke
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ExampleControllerIntegrationTests {

	@LocalServerPort int port;

	/**
	 * Sample configuration bootstrapping Salespoint as well as the components in the {@code example} package and
	 * disabling security.
	 *
	 * @author Oliver Gierke
	 */
	@Configuration
	@EnableSalespoint
	static class Config {

		@Bean
		SecurityFilterChain testSecurity(HttpSecurity security, HandlerMappingIntrospector introspector) throws Exception {

			var mvc = new MvcRequestMatcher.Builder(introspector);

			return security
					.authorizeHttpRequests(it -> it.requestMatchers(mvc.pattern("/**")).permitAll()
							.anyRequest().authenticated())
					.csrf(it -> it.disable())
					.build();
		}
	}

	@Test // #72
	void usesUtf8ToDecodePayload() {

		var template = new RestTemplate();
		var parameters = new LinkedMultiValueMap<>(Map.of("value", List.of("äöü€")));

		template.postForLocation(String.format("http://localhost:%s/encoding", port), parameters);
	}
}
