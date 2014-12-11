package org.salespointframework;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Annotation to enable Salespoint for a Spring Boot application. Will make sure that all necessary Salespoint
 * configuration is activated:
 * <ul>
 * <li>{@code Salespoint} - to bootstrap core application components, repositories and services.</li>
 * <li>{@code SalespointWebConfiguration} - web specific configuration, extensions to Spring MVC.</li>
 * <li>{@code SalespointSecurityConfiguration} - security specific configuration.</li>
 * </ul>
 * On top of that the annotation also enables the following functionality for the package of the annotated class:
 * <ul>
 * <li>{@code @ComponentScan} - component scanning for classes annotated with {@code @Component}, {@code @Service} and
 * {@code @Repository}.</li>
 * <li>{@code @EnableAutoConfiguration} - Spring auto-configuration, in particular Spring Data JPA repositories and
 * entities.</li>
 * </ul>
 * To customize the web or security configuration setup, declare classes extending {@link SalespointWebConfiguration} or
 * {@link SalespointSecurityConfiguration} inside your configuration class. See the Videoshop sample for details.
 * 
 * @author Oliver Gierke
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Configuration
@Import(Salespoint.class)
@ComponentScan
@EnableAutoConfiguration
public @interface EnableSalespoint {

	@Configuration
	@ConditionalOnMissingBean(SalespointWebConfiguration.class)
	@Import(SalespointWebConfiguration.class)
	static class SalespointWebAutoConfiguration {}

	@Configuration
	@ConditionalOnMissingBean(SalespointSecurityConfiguration.class)
	@Import(SalespointSecurityConfiguration.class)
	static class SalespointSecurityAutoConfiguration {}
}
