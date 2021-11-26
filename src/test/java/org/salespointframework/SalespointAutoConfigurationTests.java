/*
 * Copyright 2021 the original author or authors.
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

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.salespointframework.SalespointApplicationConfigurationTests.SalespointSample;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;

/**
 * Integration tests for Salespoint's auto-configuration
 *
 * @author Oliver Drotbohm
 * @since 7.5
 */
class SalespointAutoConfigurationTests {

	ApplicationContextRunner runner = new ApplicationContextRunner()
			.withUserConfiguration(SalespointSample.class);

	@Test // #371
	void doesNotEnableSchedulingByDefault() {

		runner.run(context -> {
			assertThat(context).doesNotHaveBean(ScheduledAnnotationBeanPostProcessor.class);
		});
	}

	@Test // #371
	void enablesSchedulingIfPropertyIsSet() {

		runner.withPropertyValues("salespoint.scheduling.enabled=true")
				.run(context -> {
					assertThat(context).hasSingleBean(ScheduledAnnotationBeanPostProcessor.class);
				});
	}
}
