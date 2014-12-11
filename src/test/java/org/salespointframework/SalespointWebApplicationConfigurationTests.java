package org.salespointframework;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.salespointframework.catalog.ProductIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * Integration test for Salespoint web configuration setup.
 * 
 * @author Oliver Gierke
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = { SalespointWebConfiguration.class, Salespoint.class })
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
