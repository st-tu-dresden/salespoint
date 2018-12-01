/*
 * Copyright 2017-2019 the original author or authors.
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
package org.salespointframework.inventory;

import javax.persistence.Embeddable;

import org.salespointframework.core.SalespointIdentifier;

/**
 * {@code InventoryItemIdentifier} serves as an identifier type for {@link UniqueInventoryItem} objects. The main reason for
 * its existence is type safety for identifier across the Salespoint Framework. <br />
 * {@code InventoryItemIdentifier} instances serve as primary key attribute in {@link UniqueInventoryItem}, but can also be
 * used as a key for non-persistent, <code>Map</code>-based implementations.
 * 
 * @author Paul Henke
 * @author Oliver Gierke
 */
@Embeddable
public class InventoryItemIdentifier extends SalespointIdentifier {

	private static final long serialVersionUID = -5195493076944614L;

	/**
	 * Creates a new unique identifier for {@link UniqueInventoryItem}s.
	 */
	InventoryItemIdentifier() {
		super();
	}

	/**
	 * Only needed for property editor, shouldn't be used otherwise.
	 * 
	 * @param inventoryItemIdentifier The string representation of the identifier.
	 */
	InventoryItemIdentifier(String inventoryItemIdentifier) {
		super(inventoryItemIdentifier);
	}
}
