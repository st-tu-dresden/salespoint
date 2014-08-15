package org.salespointframework;

import java.util.List;

import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@Import(Salespoint.class)
public class SalespointWebConfiguration extends WebMvcConfigurerAdapter {

	@Autowired List<? extends Converter<?, ?>> converters;
	@Autowired UserAccountManager userAccountManager;
	@Autowired List<HandlerMethodArgumentResolver> argumentResolvers;

	@Bean
	public CharacterEncodingFilter characterEncodingFilter() {
		final CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("UTF-8");
		characterEncodingFilter.setForceEncoding(true);
		return characterEncodingFilter;
	}
	
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.addAll(this.argumentResolvers);
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		for (Converter<?, ?> converter : converters) {
			registry.addConverter(converter);
		}
	}
}
