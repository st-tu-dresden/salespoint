/*
 * Copyright 2017-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.salespointframework;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web-specific configuration for Salespoint. See the individual {@code @Bean} methods for details.
 *
 * @author Paul Henke
 * @author Oliver Gierke
 */
@Configuration
public class SalespointWebConfiguration implements WebMvcConfigurer {

	private @Autowired(required = false) List<? extends Converter<?, ?>> converters = new ArrayList<>();
	private @Autowired(required = false) List<? extends Formatter<?>> formatters = new ArrayList<>();
	private @Autowired(required = false) List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<>();

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
	 * @see org.salespointframework.support.QuantityFormatter
	 */
	@Override
	public void addFormatters(FormatterRegistry registry) {

		converters.forEach(registry::addConverter);
		formatters.forEach(registry::addFormatter);
	}
}
