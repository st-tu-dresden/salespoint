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
package org.salespointframework.order;

import java.util.Map;

import javax.persistence.Embeddable;

import org.salespointframework.core.SalespointIdentifier;

/**
 * {@link OrderIdentifier} serves as an identifier type for {@link Order} objects. The main reason for its existence is
 * type safety for identifier across the Salespoint Framework. <br />
 * {@link OrderIdentifier} instances serve as primary key attribute in {@link Order}, but can also be used as a key for
 * non-persistent, {@link Map}-based implementations.
 * 
 * @author Thomas Dedek
 */
@Embeddable
public final class OrderIdentifier extends SalespointIdentifier {

	private static final long serialVersionUID = -5109304499529387062L;

	/**
	 * Creates a new unique identifier for {@link Order}.
	 */
	OrderIdentifier() {
		super();
	}

	/**
	 * Only needed for property editor, shouldn't be used otherwise.
	 * 
	 * @param orderIdentifier The string representation of the identifier.
	 */
	OrderIdentifier(String orderIdentifier) {
		super(orderIdentifier);
	}
}
