/*
 * Copyright 2018 the original author or authors.
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

import java.util.Iterator;

import javax.money.MonetaryAmount;

import org.springframework.data.util.Streamable;
import org.springframework.util.Assert;

/**
 * An extension of{@link Streamable} of {@link Priced} objects that expose a {@link #getTotal()} method so that the
 * combined price can be calculated easily.
 * 
 * @author Oliver Gierke*
 * @since 7.1
 */
public interface Totalable<T extends Priced> extends Streamable<T> {

	/**
	 * Creates a new {@link Totalable} for the given {@link Iterable} of {@link Priced} elements.
	 * 
	 * @param priced must not be {@literal null}.
	 * @return
	 */
	static <T extends Priced> Totalable<T> of(Iterable<T> priced) {

		Assert.notNull(priced, "Priced must not be null!");

		return new Totalable<T>() {

			/* 
			 * (non-Javadoc)
			 * @see org.salespointframework.order.Totalable#getTotal()
			 */
			@Override
			public MonetaryAmount getTotal() {
				return Priced.sumUp(priced);
			}

			@Override
			public Iterator<T> iterator() {
				return priced.iterator();
			}
		};
	}

	/**
	 * Returns the total of all the {@link Priced} elements contained in this {@link Totalable}.
	 * 
	 * @return
	 */
	MonetaryAmount getTotal();

	/**
	 * Creates a new {@link Totalable} with the given one added to the current one.
	 * 
	 * @param priced must not be {@literal null}.
	 * @return
	 */
	default Totalable<T> and(Iterable<? extends T> priced) {

		Assert.notNull(priced, "Priced must not be null!");

		return Totalable.of(Streamable.super.and(priced));
	}
}
