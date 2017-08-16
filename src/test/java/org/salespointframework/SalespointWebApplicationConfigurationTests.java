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
package org.salespointframework;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.salespointframework.catalog.ProductIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * Integration test for Salespoint web configuration setup.
 * 
 * @author Oliver Gierke
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { SalespointWebConfiguration.class, Salespoint.class })
public class SalespointWebApplicationConfigurationTests {

	@Autowired ConversionService conversionService;
	@Autowired CharacterEncodingFilter encodingFilter;
	@Autowired PasswordEncoder passwordEncoder;

	@Test
	public void conversionServicePrepared() {
		assertThat(conversionService.canConvert(String.class, ProductIdentifier.class), is(true));
	}

	@Test
	public void encodingFilterRegistered() {
		assertThat(encodingFilter, is(notNullValue()));
	}

	@Test
	public void passwordEncoderRegistered() {
		assertThat(passwordEncoder, is(notNullValue()));
	}
}
