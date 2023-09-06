/*
 * Copyright 2021-2023 the original author or authors.
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

import lombok.RequiredArgsConstructor;
import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Fundamental configuration properties for the {@code salespoint} namespace.
 *
 * @author Oliver Drotbohm
 * @since 7.5
 */
@Value
@RequiredArgsConstructor(onConstructor = @__(@ConstructorBinding))
@ConfigurationProperties("salespoint")
class SalespointProperties {

	Scheduling scheduling;

	@Value
	static class Scheduling {

		/**
		 * Set this to {@literal true} to enable Spring's scheduling functionality. Spring bean methods annotated with
		 * {@link Scheduled} will be invoked according to their configuration.
		 */
		boolean enabled;
	}
}
