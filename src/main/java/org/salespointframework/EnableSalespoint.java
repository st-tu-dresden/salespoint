/*
 * Copyright 2017-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.salespointframework;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.salespointframework.EnableSalespoint.SalespointSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import org.springframework.modulith.Modulithic;

/**
 * Annotation to enable Salespoint for a Spring Boot application. Will make sure that all necessary Salespoint
 * configuration is activated:
 * <ul>
 * <li>{@code Salespoint} - to bootstrap core application components, repositories and services.</li>
 * <li>{@code SalespointSecurityConfiguration} - security specific configuration.</li>
 * </ul>
 * On top of that the annotation also enables the following functionality for the package of the annotated class:
 * <ul>
 * <li>{@code @ComponentScan} - component scanning for classes annotated with {@code @Component}, {@code @Service} and
 * {@code @Repository}.</li>
 * <li>{@code @EnableAutoConfiguration} - Spring auto-configuration, in particular Spring Data JPA repositories and
 * entities.</li>
 * </ul>
 * To customize the security configuration setup, declare classes extending {@code SalespointSecurityConfiguration}
 * inside your configuration class. See the Videoshop sample for details.
 *
 * @author Oliver Gierke
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(Salespoint.class)
@ImportAutoConfiguration(SalespointSecurityAutoConfiguration.class)
@EntityScan
@Modulithic( //
		sharedModules = { //
				"org.salespointframework.core", //
				"org.salespointframework.support", //
				"org.salespointframework.quantity", //
				"org.salespointframework.useraccount" }, //
		additionalPackages = "org.salespointframework", //
		useFullyQualifiedModuleNames = true)
@SpringBootApplication
public @interface EnableSalespoint {

	@AliasFor(annotation = Modulithic.class, attribute = "systemName")
	String value() default "";

	@Import(SalespointWebSecurityConfiguration.class)
	static class SalespointSecurityAutoConfiguration {}
}
