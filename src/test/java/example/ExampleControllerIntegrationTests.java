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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.salespointframework.EnableSalespoint;
import org.salespointframework.SalespointSecurityConfiguration;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Integration tests for sample components.
 * 
 * @author Oliver Gierke
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ExampleControllerIntegrationTests {

	@LocalServerPort int port;

	/**
	 * Sample configuration bootstrapping Salespoint as well as the components in the {@code example} package and
	 * disabling security.
	 *
	 * @author Oliver Gierke
	 */
	@Configuration
	@EnableSalespoint
	static class Config extends SalespointSecurityConfiguration {

		/*
		 * (non-Javadoc)
		 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)
		 */
		@Override
		protected void configure(HttpSecurity http) throws Exception {

			http.csrf().disable();
			http.authorizeRequests().antMatchers("/**").permitAll();
		}
	}

	@Test // #72
	public void usesUtf8ToDecodePayload() {

		RestTemplate template = new RestTemplate();

		MultiValueMap<Object, Object> parameters = new LinkedMultiValueMap<>();
		parameters.add("value", "äöü€");

		template.postForLocation(String.format("http://localhost:%s/encoding", port), parameters);
	}
}
