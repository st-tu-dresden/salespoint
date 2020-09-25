/*
 * Copyright 2019-2020 the original author or authors.
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

import lombok.Value;

import org.salespointframework.quantity.Quantity;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.lang.Nullable;

/**
 * Properties to configure Salespoint's inventory. Declare {@code salespoint.inventory.…} in application properties to
 * tweak settings.
 *
 * @author Martin Morgenstern
 * @author Oliver Drotbohm
 * @since 7.2.2
 */
@Value
@ConstructorBinding
@ConfigurationProperties("salespoint.inventory")
class InventoryProperties {

	/**
	 * Disable inventory updates completely, defaults to {@literal false}.
	 */
	private boolean disableUpdates;

	/**
	 * The threshold at which a {@link InventoryEvents.StockShort} is supposed to be triggered during inventory updates.
	 */
	private Quantity restockThreshold;

	InventoryProperties(boolean disableUpdates, @Nullable Quantity restockThreshold) {

		this.disableUpdates = disableUpdates;
		this.restockThreshold = restockThreshold == null ? Quantity.NONE : restockThreshold;
	}
}
