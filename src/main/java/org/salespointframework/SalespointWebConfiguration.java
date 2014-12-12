package org.salespointframework;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Web-specific configuration for Salespoint. See the individual {@code @Bean} methods for details.
 *
 * @author Paul Henke
 * @author Oliver Gierke
 */
@Configuration
public class SalespointWebConfiguration extends WebMvcConfigurerAdapter {

	@Autowired List<? extends Converter<?, ?>> converters;
	@Autowired List<HandlerMethodArgumentResolver> argumentResolvers;

	/**
	 * Registers the {@link Salespoint} specific {@link HandlerMethodArgumentResolver} with Spring MVC.
	 * 
	 * @see org.salespointframework.useraccount.web.LoggedInUserAccountArgumentResolver
	 */
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.addAll(this.argumentResolvers);
	}

	/**
	 * Registers the {@link Salespoint} specific {@link Converter}s with Spring MVC.
	 * 
	 * @see org.salespointframework.support.SalespointIdentifierConverter
	 * @see org.salespointframework.support.JpaEntityConverter
	 * @see org.salespointframework.support.StringToRoleConverter
	 */
	@Override
	public void addFormatters(FormatterRegistry registry) {

		for (Converter<?, ?> converter : converters) {
			registry.addConverter(converter);
		}
	}
}
