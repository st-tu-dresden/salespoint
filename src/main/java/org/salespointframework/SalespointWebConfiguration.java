package org.salespointframework;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.HttpEncodingProperties;
import org.springframework.boot.context.web.OrderedCharacterEncodingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

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
	@Autowired HttpEncodingProperties httpEncodingProperties;

	/**
	 * Special dialect to support Java 8 type formatting.
	 * 
	 * @return
	 */
	@Bean
	public Java8TimeDialect java8TimeDialect() {
		return new Java8TimeDialect();
	}

	/**
	 * Workaround for https://github.com/spring-projects/spring-boot/issues/3912
	 * 
	 * @return
	 */
	@Bean
	public OrderedCharacterEncodingFilter characterEncodingFilter() {

		OrderedCharacterEncodingFilter filter = new OrderedCharacterEncodingFilter();

		filter.setEncoding(this.httpEncodingProperties.getCharset().name());
		filter.setForceEncoding(this.httpEncodingProperties.isForce());
		filter.setOrder(Ordered.HIGHEST_PRECEDENCE);

		return filter;
	}

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

	static class PriorityOrderedCharacterEncodingFilter extends CharacterEncodingFilter implements Ordered {

		/* 
		 * (non-Javadoc)
		 * @see org.springframework.core.Ordered#getOrder()
		 */
		@Override
		public int getOrder() {
			return Ordered.HIGHEST_PRECEDENCE;
		}
	}
}
