package org.salespointframework;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Application configuration for Salespoint.
 * 
 * @author Oliver Gierke
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan(excludeFilters = @Filter(value = Configuration.class))
public class Salespoint {

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
