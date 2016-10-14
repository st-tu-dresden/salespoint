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

	/**
	 * @see #72
	 */
	@Test
	public void usesUtf8ToDecodePayload() {

		RestTemplate template = new RestTemplate();

		MultiValueMap<Object, Object> parameters = new LinkedMultiValueMap<>();
		parameters.add("value", "äöü€");

		template.postForLocation(String.format("http://localhost:%s/encoding", port), parameters);
	}
}
