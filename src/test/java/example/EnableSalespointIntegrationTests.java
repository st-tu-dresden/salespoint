package example;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.salespointframework.EnableSalespoint;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.catalog.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

/**
 * Integration tests for {@link EnableSalespoint}.
 * 
 * @author Oliver Gierke
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration
public class EnableSalespointIntegrationTests {

	@Configuration
	@EnableSalespoint
	static class TestConfiguration {}

	@Autowired ApplicationContext context;
	@Autowired Catalog<Product> catalog;
	@Autowired DummyRepository repository;
	@Autowired Java8TimeDialect dialect;
	@Autowired MethodSecurityMetadataSource securityMetadataSource;

	@Test
	public void componentsFromSalespointAndExampleAreAvailable() {

		assertThat(context, is(notNullValue()));
		assertThat(repository, is(notNullValue()));
		assertThat(catalog, is(notNullValue()));

		// Make sure the web configuration is included if not declared explicitly
		assertThat(dialect, is(notNullValue()));

		// Assert that @EnableGlobalMethodSecurity works
		assertThat(securityMetadataSource, is(notNullValue()));
	}
}
