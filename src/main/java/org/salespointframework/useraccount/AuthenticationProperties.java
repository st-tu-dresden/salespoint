/*
 * Copyright 2018-2022 the original author or authors.
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
package org.salespointframework.useraccount;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

/**
 * Properties to configure Salespoint authentication. Declare {@code salespoint.authentication.…} in application
 * properties to tweak settings.
 *
 * @author Oliver Gierke
 * @since 7.1
 */
@Value
@ConstructorBinding
@ConfigurationProperties("salespoint.authentication")
class AuthenticationProperties {

	/**
	 * Enables the login procedure to use the email address to lookup a user instead of their username. Defaults to
	 * {@literal false}.
	 */
	private boolean loginViaEmail;
}
