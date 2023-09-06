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
package org.salespointframework.order;

import javax.money.MonetaryAmount;

import org.salespointframework.core.Currencies;
import org.springframework.data.util.Streamable;
import org.springframework.util.Assert;

/**
 * Interface for any priced item to ease summing up priced items.
 *
 * @author Oliver Gierke
 */
interface Priced {

	/**
	 * Returns the price of the item.
	 * 
	 * @return
	 */
	MonetaryAmount getPrice();

	/**
	 * Sums up the prices of all given {@link Priced} instances.
	 * 
	 * @param priced must not be {@literal null}.
	 * @return
	 */
	static MonetaryAmount sumUp(Iterable<? extends Priced> priced) {

		Assert.notNull(priced, "Iterable must not be null!");

		return Streamable.of(priced).stream()//
				.map(Priced::getPrice)//
				.reduce(MonetaryAmount::add) //
				.orElse(Currencies.ZERO_EURO);
	}
}
