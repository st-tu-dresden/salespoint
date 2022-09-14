/*
 * Copyright 2019-2022 the original author or authors.
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
package org.salespointframework.inventory;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.salespointframework.Salespoint;
import org.salespointframework.inventory.InventoryListeners.InventoryOrderEventListener;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

/**
 * Integration tests for configuration of the inventory module.
 *
 * @author Oliver Drotbohm
 */
class InventoryConfigurationIntegrationTests {

	@Test
	void doesNotRegisterEventListenerIfDisabledViaConfiguration() {

		new ApplicationContextRunner()
				.withUserConfiguration(Salespoint.class)
				.withPropertyValues("salespoint.inventory.disable-updates=true")
				.run(context -> {
					assertThat(context).doesNotHaveBean(InventoryOrderEventListener.class);
				});
	}
}
