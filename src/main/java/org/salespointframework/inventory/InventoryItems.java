/*
 * Copyright 2018-2019 the original author or authors.
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

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Iterator;

import org.salespointframework.catalog.Product;
import org.salespointframework.quantity.Quantity;
import org.springframework.data.util.Streamable;

/**
 * An abstraction over a collection of {@link InventoryItem}s. Offers convenience methods to handle cases in which a
 * lookup of {@link InventoryItem}s for a {@link Product} return multiple items or a {@link UniqueInventoryItem}.
 *
 * @author Oliver Drotbohm
 * @since 7.2
 */
@RequiredArgsConstructor(staticName = "of")
public class InventoryItems<T extends InventoryItem<?>> implements Streamable<T> {

	private final @NonNull Streamable<T> items;

	/**
	 * Returns the total quantity of all the {@link InventoryItem}s contained.
	 *
	 * @return will never be {@literal null}.
	 */
	public Quantity getTotalQuantity() {

		return stream() //
				.map(InventoryItem::getQuantity) //
				.reduce(Quantity::add) //
				.orElse(Quantity.NONE);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<T> iterator() {
		return items.iterator();
	}
}
