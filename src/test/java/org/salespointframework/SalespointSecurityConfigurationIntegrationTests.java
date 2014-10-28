package org.salespointframework;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Integration test for security configuration setup.
 * 
 * @author Oliver Gierke
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration
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

	/**
	 * @see #41
	 */
	@Test
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
