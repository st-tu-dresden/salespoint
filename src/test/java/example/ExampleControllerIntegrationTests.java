package example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.salespointframework.EnableSalespoint;
import org.salespointframework.SalespointSecurityConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Integration tests for sample components.
 * 
 * @author Oliver Gierke
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class ExampleControllerIntegrationTests {

	@Value("${local.server.port}") int port;

	/**
	 * Sample configuration bootstrapping Salespoint as well as the components in the {@code example} package and
	 * disabling security.
	 *
	 * @author Oliver Gierke
	 */
	@Configuration
	@EnableSalespoint
	static class Config extends SalespointSecurityConfiguration {

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
